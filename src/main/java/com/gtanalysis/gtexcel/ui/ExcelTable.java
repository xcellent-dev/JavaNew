/*
 */
package com.gtanalysis.gtexcel.ui;

import com.gtanalysis.gtexcel.model.HorizontalCellAlignment;
import com.gtanalysis.gtexcel.model.CellAlignment;
import com.gtanalysis.gtexcel.model.CellModel;
import com.gtanalysis.gtexcel.model.ExcelTableModel;
import com.gtanalysis.gtexcel.model.VerticalCellAlignment;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import static javax.swing.Action.ACTION_COMMAND_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author nkabiliravi
 */
public class ExcelTable extends JTable {

    private static final int DEFAULT_WIDTH = 65;
    private RowHeader rowHeader;
    private TableRowSorter<ExcelTableModel> rowSorter;

    public ExcelTable() {
        getTableHeader().setResizingAllowed(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        UIManager.getDefaults().putDefaults(new Object[]{
            "TableHeader.selectionForeground", Color.black,
            "TableHeader.selectionBackground", Color.orange
        });
        getTableHeader().setDefaultRenderer(
                new HeaderRenderer(HeaderRenderer.Axis.HORIZONTAL));

        setSurrendersFocusOnKeystroke(true);
        getActionMap().put(EditActiveCellAction.EDIT_ACTIVE_CELL_ACTION, new EditActiveCellAction());
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), EditActiveCellAction.EDIT_ACTIVE_CELL_ACTION);

        setDefaultRenderer(Object.class, new ExcelCellRenderer());

        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTableHeader tableHeader = (JTableHeader) e.getComponent();
                if (e.getClickCount() == 2) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (e.isShiftDown()) {
                            int index = tableHeader.getColumnModel().getColumnIndexAtX(e.getX());
                            expandSiblingColumns(index);

                        } else {
                            int index = tableHeader.getColumnModel().getColumnIndexAtX(e.getX());
                            collapseColumn(index);
                        }
                    }
                }
            }

        });
        getTableHeader().getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
                for (int i = e.getFromIndex(); i <= e.getToIndex(); i++) {
                    getTableHeader().getColumnModel().getColumn(i).setMinWidth(0);
                }
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                // storeCellsSize();
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });

    }

    private Clipboard getClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public void collapseColumn(int index) {
        TableColumn tableColumn = this.getTableHeader().getColumnModel().getColumn(index);
        tableColumn.setWidth(0);
        tableColumn.setPreferredWidth(0);
        updateUI();
    }

    private void expandSiblingColumns(int index) {
        if (index + 1 < this.getTableHeader().getColumnModel().getColumnCount()) {
            TableColumn nextTableColumn = this.getTableHeader().getColumnModel().getColumn(index + 1);
            nextTableColumn.setWidth(DEFAULT_WIDTH);
            nextTableColumn.setPreferredWidth(DEFAULT_WIDTH);
        }

        if (index - 1 >= 0) {
            TableColumn prevTableColumn = this.getTableHeader().getColumnModel().getColumn(index - 1);
            prevTableColumn.setWidth(DEFAULT_WIDTH);
            prevTableColumn.setPreferredWidth(DEFAULT_WIDTH);
        }
        updateUI();
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
    }

    public ExcelTableModel getExcelTableModel() {
        return (ExcelTableModel) getModel();
    }

    public RowHeader getRowHeader() {
        return rowHeader;
    }

    @Override
    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        if (rowHeader == null) {
            rowHeader = new RowHeader(this);
            rowHeader.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                }

            });
        }
        setEnclosingScrollPaneRowHeaderView(rowHeader);
    }

    @Override
    protected void unconfigureEnclosingScrollPane() {
        super.unconfigureEnclosingScrollPane();
        setEnclosingScrollPaneRowHeaderView(null);
    }

    private void setEnclosingScrollPaneRowHeaderView(JComponent header) {
        if (getParent() instanceof JViewport) {
            if (getParent().getParent() instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) (getParent().getParent());
                JViewport viewport = scrollPane.getViewport();
                if (viewport != null && viewport.getView() == this) {
                    scrollPane.setRowHeaderView(header);
                }
            }
        }
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        Component component = super.prepareEditor(editor, row, column);
        if (component instanceof JTextField) {
            ExcelTableModel excelTableModel = (ExcelTableModel) getModel();
            String cellValue = excelTableModel.getEditableCellValue(row, column);

            ((JTextField) component).setText(cellValue);
        }
        return component;
    }

    public void findAndReplace(String searchFor,
            String replaceText,
            boolean matchCase,
            boolean regex,
            boolean searchHorizontally,
            boolean backward,
            boolean replace) {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();
        CellModel cellModel = getExcelTableModel().findAndReplace(searchFor, replaceText, matchCase, regex,
                selectedRow, selectedColumn, searchHorizontally, backward, replace);
        getSelectionModel().setSelectionInterval(cellModel.getRowIndex(), cellModel.getRowIndex());
        getColumnModel().getSelectionModel().setSelectionInterval(cellModel.getColumnIndex(), cellModel.getColumnIndex());
    }

    public void replaceAll(String searchFor,
            String replaceText,
            boolean matchCase,
            boolean regex) {
        getExcelTableModel().replaceAll(searchFor, replaceText, matchCase, regex);
    }

    protected class EditActiveCellAction extends AbstractAction {

        public static final String EDIT_ACTIVE_CELL_ACTION = "Edit Active Cell";

        public EditActiveCellAction() {
            putValue(NAME, EDIT_ACTIVE_CELL_ACTION);
            putValue(SHORT_DESCRIPTION, EDIT_ACTIVE_CELL_ACTION);
            putValue(ACTION_COMMAND_KEY, EDIT_ACTIVE_CELL_ACTION);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = getSelectionModel().getAnchorSelectionIndex();
            int column = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
            if (row >= 0 && column >= 0) {
                editCellAt(row, column, e);
            }
        }
    }

    public void loadCellsSize() {
        ExcelTableModel excelTableModel = (ExcelTableModel) getModel();
        Sheet sheet = excelTableModel.getSheet();
        loadCellsSize(excelTableModel, sheet);
    }

    private void loadCellsSize(ExcelTableModel excelTableModel, Sheet sheet) {
        TableColumnModel tableColumnModel = this.getTableHeader().getColumnModel();
        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            int sheetColumnWidth = sheet.getColumnWidth(i) / 80;
            tableColumn.setPreferredWidth(sheetColumnWidth);
        }
        for (int j = 0; j < excelTableModel.getRowCount(); j++) {
            Row row = sheet.getRow(j);
            if (row == null) {
                row = excelTableModel.getSheet().createRow(j);
            }
            int sheetRowHeight = row.getHeight() / 20;
            sheetRowHeight = sheetRowHeight <= 0 ? 1 : sheetRowHeight;
            this.setRowHeight(j, sheetRowHeight);
        }
        updateUI();
    }

    public void storeCellsSize() {
        ExcelTableModel excelTableModel = (ExcelTableModel) getModel();
        Sheet sheet = excelTableModel.getSheet();
        TableColumnModel tableColumnModel = this.getTableHeader().getColumnModel();
        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            int sheetColumnWidth = tableColumn.getWidth();
            sheetColumnWidth = sheetColumnWidth >= 255 ? 254 : sheetColumnWidth;
            sheetColumnWidth = sheetColumnWidth * 80;
            sheet.setColumnWidth(i, sheetColumnWidth);
        }
        for (int j = 0; j < excelTableModel.getRowCount(); j++) {
            int tableRowHeight = this.getRowHeight(j);
            if (tableRowHeight <= 1) {
                tableRowHeight = 0;
            }
            int sheetRowHeight = tableRowHeight * 20;
            Row row = sheet.getRow(j);
            if (row == null) {
                row = sheet.createRow(j);
            }
            row.setHeight((short) sheetRowHeight);
        }
    }

    public void alignCell(int selectedColumn, int selectedRow, CellAlignment alignment) {
        storeCellsSize();
        TableCellRenderer tableCellRenderer = getCellRenderer(selectedRow, selectedColumn);
        if (tableCellRenderer instanceof DefaultTableCellRenderer) {
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tableCellRenderer;
            if (alignment instanceof VerticalCellAlignment) {
                renderer.setVerticalAlignment(alignment.getViewValue());
            } else if (alignment instanceof HorizontalCellAlignment) {
                renderer.setHorizontalAlignment(alignment.getViewValue());
            }
        }
        updateUI();
    }

    public void copy() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        if (row > -1 && column > -1) {
            try {
                CellModel cellModel = getExcelTableModel().getCellModel(row, column);
                StringSelection selectedValue = new StringSelection(cellModel.toXml());
                getClipboard().setContents(selectedValue, selectedValue);
            } catch (IOException ex) {
                Logger.getLogger(ExcelTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cut() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        if (row > -1 && column > -1) {
            try {
                CellModel cellModel = getExcelTableModel().getCellModel(row, column);
                StringSelection selectedValue = new StringSelection(cellModel.toXml());
                getClipboard().setContents(selectedValue, selectedValue);
                setValueAt(null, row, column);
            } catch (IOException ex) {
                Logger.getLogger(ExcelTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void paste() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        if (row > -1 && column > -1) {
            Transferable clipData = getClipboard().getContents(getClipboard());
            if (clipData != null) {
                try {
                    if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String pasteData = (String) (clipData.getTransferData(
                                DataFlavor.stringFlavor));
                        CellModel cellModel = new CellModel();
                        cellModel.setXml(pasteData);
                        getExcelTableModel().setCellModel(cellModel, row, column);
                    }
                } catch (UnsupportedFlavorException ufe) {
                    System.err.println("Flavor unsupported: " + ufe);
                } catch (IOException ioe) {
                    System.err.println("Data not available: " + ioe);
                }
            }
        }
    }

    public boolean isPasteEnabled() {
        return getClipboard().getContents(getClipboard()) != null;
    }
}
