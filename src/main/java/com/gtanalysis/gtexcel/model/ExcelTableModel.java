package com.gtanalysis.gtexcel.model;

import com.gtanalysis.gtexcel.exception.CellNotFoundException;
import com.gtanalysis.gtexcel.model.listener.ExcelTableModelChangeCellEvent;
import com.gtanalysis.gtexcel.model.listener.ExcelTableModelListener;
import com.gtanalysis.gtexcel.provider.ExcelDataProvider;
import com.gtanalysis.gtexcel.util.WorkbookUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author nkabiliravi
 */
public class ExcelTableModel extends AbstractTableModel {

    private final ExcelDataProvider excelDataProvider;
    private final Sheet sheet;
    private final List<ExcelTableModelListener> listeners;

    public ExcelTableModel(ExcelDataProvider excelDataProvider, Sheet sheet) {
        this.excelDataProvider = excelDataProvider;
        this.sheet = sheet;
        listeners = new ArrayList<>();
    }

    public void addExcelTableModelListener(ExcelTableModelListener listener) {
        listeners.add(listener);
    }

    public void removeExcelTableModelListener(ExcelTableModelListener listener) {
        listeners.remove(listener);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return this.sheet.getPhysicalNumberOfRows();
    }

    @Override
    public int getColumnCount() {
        if (this.sheet.getPhysicalNumberOfRows() > 0) {
            return this.sheet.getRow(0).getPhysicalNumberOfCells();
        } else {
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = this.sheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        return cell.getStringCellValue();
                    case Cell.CELL_TYPE_NUMERIC:
                        return cell.getNumericCellValue();
                    case Cell.CELL_TYPE_BOOLEAN:
                        return cell.getBooleanCellValue();
                    case Cell.CELL_TYPE_ERROR:
                        return cell.getErrorCellValue();
                    case Cell.CELL_TYPE_FORMULA:
                        FormulaEvaluator evaluator = this.sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
                        return WorkbookUtil.getEvaluatedCellValue(evaluator, cell);
                    default:
                        return null;
                }
            }
        }
        return null;
    }

    public CellModel getCellModel(int rowIndex, int columnIndex) {
        Row row = this.sheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(columnIndex);
            return WorkbookUtil.getCellModel(cell);
        }
        return null;
    }

    public void setCellModel(CellModel cellModel, int rowIndex, int columnIndex) {
        Row row = this.sheet.getRow(rowIndex);
        if (row != null) {
            Cell targetCell = row.getCell(columnIndex);
            WorkbookUtil.copyCellValue(cellModel, targetCell);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object currentValue = getValueAt(rowIndex, columnIndex);
        if (aValue == null && currentValue == null
                || aValue != null && StringUtils.isEmpty(aValue.toString()) && currentValue == null
                || aValue != null && aValue.equals(currentValue)) {
            return;
        }
        Row row = this.sheet.getRow(rowIndex);
        if (row == null) {
            row = this.sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = this.excelDataProvider.createCell(row, columnIndex);
        }
        CellModel oldCellModel = WorkbookUtil.getCellModel(cell);
        if (aValue != null) {
            String strValue = aValue.toString();
            try {
                cell.setCellFormula(null);
                long longValue = Long.parseLong(strValue);
                cell.setCellValue(longValue);
            } catch (NumberFormatException doubleEx) {
                try {
                    cell.setCellFormula(null);
                    double doubleValue = Double.parseDouble(strValue);
                    cell.setCellValue(doubleValue);
                } catch (NumberFormatException ex) {
                    try {
                        FormulaEvaluator evaluator = this.sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
                        cell.setCellFormula(strValue);
                        evaluator.evaluate(cell);
                    } catch (FormulaParseException formulaEx) {
                        cell.setCellFormula(null);
                        cell.setCellValue(strValue);
                    }
                }
            }
        } else {
            cell.setCellValue((String) null);
            cell.setCellType(Cell.CELL_TYPE_BLANK);
        }
        fireTableDataChanged();
        fireTableCellUpdated(rowIndex, columnIndex);
        fireExcelTableModelChangeCellEvent(columnIndex, rowIndex, cell, oldCellModel);
    }

    public String getEditableCellValue(int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        String cellValue = null;
        if (row != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                cellValue = WorkbookUtil.getCellStringValue(cell);
            }
        }
        return cellValue;
    }

    public void insertColumn(int columnIndex) {
        for (int i = 0; i < getSheet().getPhysicalNumberOfRows(); i++) {
            Row row = getSheet().getRow(i);
            int lastCellNum = getSheet().getRow(i).getPhysicalNumberOfCells();
            this.excelDataProvider.createCell(row, lastCellNum);
            shiftColumnsToRight(row, columnIndex);
            if (columnIndex < lastCellNum) {
                this.excelDataProvider.createCell(row, columnIndex);
            }
        }
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    private void shiftColumn(Row row, int sourceIndex, int targetIndex) throws FormulaParseException {
        Cell sourceCell = row.getCell(sourceIndex);
        Cell targetCell = row.getCell(targetIndex);
        if (sourceCell == null) {
            sourceCell = this.excelDataProvider.createCell(row, sourceIndex);
        }
        if (targetCell == null) {
            targetCell = this.excelDataProvider.createCell(row, targetIndex);
        }
        WorkbookUtil.copyCellValue(sourceCell, targetCell);
    }

    public void insertRow(int rowIndex) {
        int columnCount = getColumnCount();
        int lastRowNum = getRowCount();
        Row row = this.excelDataProvider.createRow(getSheet(), lastRowNum);
        for (int j = 0; j < columnCount; j++) {
            this.excelDataProvider.createCell(row, j);
        }
        shiftRowsDown(columnCount, rowIndex);
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    private void shiftColumnsToRight(Row row, int startingColumnIndex) throws FormulaParseException {
        for (int z = getColumnCount() - 1; z > startingColumnIndex; z--) {
            shiftColumn(row, z - 1, z);
        }
    }

    private void shiftColumnsToLeft(Row row, int startingColumnIndex) throws FormulaParseException {
        for (int z = startingColumnIndex; z < getColumnCount() - 1; z++) {
            shiftColumn(row, z + 1, z);
        }
    }

    private void shiftRowsDown(int columnCount, int startingRowIndex) {
        for (int z = getRowCount() - 1; z >= startingRowIndex; z--) {
            shiftRow(columnCount, z - 1, z);
        }
        Row row = getSheet().getRow(startingRowIndex);
        for (int i = row.getFirstCellNum(); i < columnCount; i++) {
            row.createCell(i);
        }
    }

    private void shiftRowsUp(int columnCount, int startingRowIndex) {
        for (int z = startingRowIndex; z < getRowCount() - 1; z++) {
            shiftRow(columnCount, z + 1, z);
        }
    }

    public void removeColumn(int columnIndex) {
        if (columnIndex >= 0) {
            int lastCellNum = getColumnCount();
            if (lastCellNum > 0) {
                // We need to remove cell from last row to first because the getColumnCount dependends on 
                // first row count
                for (int i = getRowCount() - 1; i >= 0; i--) {
                    Row row = getSheet().getRow(i);
                    shiftColumnsToLeft(row, columnIndex);
                    Cell cell = row.getCell(lastCellNum - 1);
                    if (cell != null) {
                        row.removeCell(cell);
                    }
                }
                fireTableStructureChanged();
                fireTableDataChanged();
            }
        }
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0) {
            int lastRowNum = getRowCount();
            if (lastRowNum > 0) {
                int columnCount = getColumnCount();
                shiftRowsUp(columnCount, rowIndex);
                Row lastRow = getSheet().getRow(lastRowNum - 1);
                getSheet().removeRow(lastRow);
                fireTableStructureChanged();
                fireTableDataChanged();
            }
        }
    }

    private void shiftRow(int columnCount, int sourceIndex, int targetIndex) {
        for (int j = 0; j < columnCount; j++) {
            Row sourceRow = getSheet().getRow(sourceIndex);
            Row targetRow = getSheet().getRow(targetIndex);
            Cell sourceCell = sourceRow.getCell(j);
            if (sourceCell == null) {
                sourceCell = this.excelDataProvider.createCell(sourceRow, j);
            }
            Cell targetCell = targetRow.getCell(j);
            if (targetCell == null) {
                targetCell = this.excelDataProvider.createCell(targetRow, j);
            }
            WorkbookUtil.copyCellValue(sourceCell, targetCell);
        }
    }

    public void alignCell(int selectedColumn, int selectedRow, CellAlignment alignment) {
        Row row = getSheet().getRow(selectedRow);
        Cell cell = row.getCell(selectedColumn);

        CellStyle oldCellStyle = cell.getCellStyle();
        CellStyle newCellStyle = getSheet().getWorkbook().createCellStyle();
        newCellStyle.cloneStyleFrom(oldCellStyle);
        cell.setCellStyle(newCellStyle);
        if (alignment instanceof VerticalCellAlignment) {
            cell.getCellStyle().setVerticalAlignment((short) alignment.getModelValue());
        } else if (alignment instanceof HorizontalCellAlignment) {
            cell.getCellStyle().setAlignment((short) alignment.getModelValue());
        }
        fireTableCellUpdated(selectedRow, selectedColumn);
    }

    private boolean isDefaultCellStyle(Sheet sheet, CellStyle cellStyle) {
        boolean result = false;
        short numberOfCellStyles = sheet.getWorkbook().getNumCellStyles();
        for (short i = 0; i < numberOfCellStyles; i++) {
            CellStyle defaultCellStyle = sheet.getWorkbook().getCellStyleAt(i);
            if (defaultCellStyle.equals(cellStyle)) {
                result = true;
            }
        }
        return result;
    }

    public void sort(int columnIndex, SortType sortType, boolean allColumns) {
        if (allColumns) {
            sortAllColumns(columnIndex, sortType);
        } else {
            sortSingleColumn(columnIndex, sortType);
        }
    }

    private void sortAllColumns(int columnIndex, SortType sortType) {
        List<List<CellModel>> sheetCellModel = WorkbookUtil.populateSheetCellModels(getSheet());
        List<CellModel> sortedList = sheetCellModel.get(columnIndex);
        Collections.sort(sortedList, new CellComparator(sortType));

        for (int i = 0; i < sortedList.size(); i++) {
            Row row = getSheet().getRow(i);

            // Assign the selected column cells with new sorted values
            CellModel selectedColumnSourceCell = sortedList.get(i);
            Cell selectedColumnTargetCell = this.excelDataProvider.createCell(row, columnIndex);
            WorkbookUtil.copyCellValue(selectedColumnSourceCell, selectedColumnTargetCell);
            fireTableCellUpdated(i, columnIndex);

            // Assign other column cells values according to the selected column cells index
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                if (j != columnIndex) {
                    List<CellModel> otherColumnCellModels = sheetCellModel.get(j);
                    CellModel otherColumnSourceCell = otherColumnCellModels.get(selectedColumnSourceCell.getRowIndex());
                    Cell otherColumnTargetCell = this.excelDataProvider.createCell(row, j);
                    WorkbookUtil.copyCellValue(otherColumnSourceCell, otherColumnTargetCell);
                    fireTableCellUpdated(i, j);
                }
            }
        }
    }

    private void sortSingleColumn(int columnIndex, SortType sortType) {
        List<CellModel> sortedList = WorkbookUtil.populateCellModels(getSheet(), columnIndex);
        Collections.sort(sortedList, new CellComparator(sortType));

        for (int i = 0; i < sortedList.size(); i++) {
            CellModel sourceCell = sortedList.get(i);
            Row row = getSheet().getRow(i);
            Cell targetCell = this.excelDataProvider.createCell(row, columnIndex);
            WorkbookUtil.copyCellValue(sourceCell, targetCell);
            fireTableCellUpdated(i, columnIndex);
        }
    }

    public void styleFont(int selectedColumn, int selectedRow, FontStyle fontSytle) {
        Row row = getSheet().getRow(selectedRow);
        Cell cell = row.getCell(selectedColumn);
        CellStyle oldCellStyle = cell.getCellStyle();
        CellStyle newCellStyle = getSheet().getWorkbook().createCellStyle();
        newCellStyle.cloneStyleFrom(oldCellStyle);
        cell.setCellStyle(newCellStyle);
        Font font;
        if (newCellStyle.getFontIndex() > 0) {
            font = row.getSheet().getWorkbook().getFontAt(newCellStyle.getFontIndex());
        } else {
            font = row.getSheet().getWorkbook().createFont();
            newCellStyle.setFont(font);
        }
        switch (fontSytle) {
            case BOLD:
                font.setBold(!font.getBold());
                break;
            case ITALIC:
                font.setItalic(!font.getItalic());
                break;
            case NORMAL:
                font.setBold(false);
                font.setItalic(false);
                font.setUnderline(Font.U_NONE);
                break;
            case UNDERLINE:
                if (font.getUnderline() == Font.U_SINGLE) {
                    font.setUnderline(Font.U_NONE);
                } else {
                    font.setUnderline(Font.U_SINGLE);
                }
                break;
        }
        fireTableCellUpdated(selectedRow, selectedColumn);
    }

    public List<String> retrieveCellNamesList() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < getColumnCount(); i++) {
            String columnName = getColumnName(i);
            for (int j = 0; j < getRowCount(); j++) {
                result.add(columnName + (j + 1));
            }
        }
        return result;
    }

    public String[] retrieveCellNames() {
        List<String> sheetNameList = retrieveCellNamesList();
        String[] result = new String[sheetNameList.size()];
        sheetNameList.toArray(result);
        return result;
    }

    private void fireExcelTableModelChangeCellEvent(int column, int row, Cell cell, CellModel oldCellValue) {
        listeners.stream().forEach((listener) -> {
            listener.cellChanged(new ExcelTableModelChangeCellEvent(this, column, row, cell, oldCellValue));
        });
    }

    public CellModel findAndReplace(String searchFor,
            String replaceText,
            boolean matchCase,
            boolean regex,
            int currentRowIndex,
            int currentColumnIndex,
            boolean searchHorizontally,
            boolean backward,
            boolean replace) {
        CellModel result = null;
        currentRowIndex = currentRowIndex >= 0 ? currentRowIndex : 0;
        currentColumnIndex = currentColumnIndex >= 0 ? currentColumnIndex : 0;

        if (searchHorizontally) {
            if (backward) {
                result = findBackwardHorizontally(currentRowIndex, currentColumnIndex, searchFor, regex, matchCase);
            } else {
                result = findForwardHorizontally(currentRowIndex, currentColumnIndex, searchFor, regex, matchCase);
            }
        } else if (!searchHorizontally) {
            if (backward) {
                result = findBackwardVertically(currentRowIndex, currentColumnIndex, searchFor, regex, matchCase);
            } else {
                result = findForwardVertically(currentRowIndex, currentColumnIndex, searchFor, regex, matchCase);
            }
        }
        if (result != null) {
            if (replace && replaceText != null) {
                replaceCell(result, regex, searchFor, replaceText);
            }
            return result;
        } else {
            throw new CellNotFoundException();
        }
    }

    private void replaceCell(CellModel result, boolean regex, String searchFor, String replaceText) {
        String replacedValue = result.getValue() != null ? result.getValue().toString() : "";
        if (regex) {
            replacedValue = replacedValue.replaceAll(searchFor, replaceText);
        } else {
            replacedValue = replacedValue.replace(searchFor, replaceText);
        }
        setValueAt(replacedValue, result.getRowIndex(), result.getColumnIndex());
    }

    private CellModel findForwardHorizontally(int currentRowIndex, int currentColumnIndex, String searchFor, boolean regex, boolean matchCase) {
        for (int i = currentRowIndex; i < getRowCount(); i++) {
            Row row = getSheet().getRow(i);
            if (row != null) {
                for (int j = 0; j < getColumnCount(); j++) {
                    Cell cell = row.getCell(j);
                    CellModel cellModel = matchCell(cell, searchFor, regex, matchCase);
                    if (cellModel != null) {
                        if (cellModel.getColumnIndex() > currentColumnIndex
                                || cellModel.getRowIndex() > currentRowIndex) {
                            return cellModel;
                        }
                    }
                }
            }
        }
        return null;
    }

    private CellModel findForwardVertically(int currentRowIndex, int currentColumnIndex, String searchFor, boolean regex, boolean matchCase) {
        for (int j = currentColumnIndex; j < getColumnCount(); j++) {
            for (int i = 0; i < getRowCount(); i++) {
                Row row = getSheet().getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(j);
                    CellModel cellModel = matchCell(cell, searchFor, regex, matchCase);
                    if (cellModel != null) {
                        if (cellModel.getColumnIndex() > currentColumnIndex
                                || cellModel.getRowIndex() > currentRowIndex) {
                            return cellModel;
                        }
                    }
                }
            }
        }
        return null;
    }

    private CellModel findBackwardHorizontally(int currentRowIndex, int currentColumnIndex, String searchFor, boolean regex, boolean matchCase) {
        for (int i = currentRowIndex; i >= 0; i--) {
            Row row = getSheet().getRow(i);
            if (row != null) {
                for (int j = getColumnCount() - 1; j >= 0; j--) {
                    Cell cell = row.getCell(j);
                    CellModel cellModel = matchCell(cell, searchFor, regex, matchCase);
                    if (cellModel != null) {
                        if (cellModel.getColumnIndex() < currentColumnIndex
                                || cellModel.getRowIndex() < currentRowIndex) {
                            return cellModel;
                        }
                    }
                }
            }
        }
        return null;
    }

    private CellModel findBackwardVertically(int currentRowIndex, int currentColumnIndex, String searchFor, boolean regex, boolean matchCase) {
        for (int j = currentColumnIndex; j >= 0; j--) {
            for (int i = getRowCount() - 1; i >= 0; i--) {
                Row row = getSheet().getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(j);
                    CellModel cellModel = matchCell(cell, searchFor, regex, matchCase);
                    if (cellModel != null) {
                        if (cellModel.getColumnIndex() < currentColumnIndex
                                || cellModel.getRowIndex() < currentRowIndex) {
                            return cellModel;
                        }
                    }
                }
            }
        }
        return null;
    }

    private CellModel matchCell(Cell cell, String searchFor, boolean regex, boolean matchCase) {
        if (cell != null) {
            Object cellValue = WorkbookUtil.getCellValue(cell);
            if (cellValue != null) {
                String strCellValue = cellValue.toString();
                if (!matchCase) {
                    strCellValue = strCellValue.toLowerCase();
                    searchFor = searchFor.toLowerCase();
                }
                if (regex) {
                    if (strCellValue.matches(searchFor)) {
                        return new CellModel(cell.getColumnIndex(), cell.getRowIndex(), cellValue, cell.getCellType());
                    }
                } else if (strCellValue.contains(searchFor)) {
                    return new CellModel(cell.getColumnIndex(), cell.getRowIndex(), cellValue, cell.getCellType());
                }
            }
        }
        return null;
    }

    public void replaceAll(String searchFor,
            String replaceText,
            boolean matchCase,
            boolean regex) {
        for (int i = 0; i < getRowCount(); i++) {
            Row row = getSheet().getRow(i);
            if (row != null) {
                for (int j = 0; j < getColumnCount(); j++) {
                    Cell cell = row.getCell(j);
                    CellModel cellModel = matchCell(cell, searchFor, regex, matchCase);
                    if (cellModel != null) {
                        replaceCell(cellModel, regex, searchFor, replaceText);
                    }
                }
            }
        }
    }

}
