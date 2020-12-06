/*
 */
package com.gtanalysis.gtexcel.ui;

import com.gtanalysis.gtexcel.model.HorizontalCellAlignment;
import com.gtanalysis.gtexcel.model.ExcelTableModel;
import com.gtanalysis.gtexcel.model.VerticalCellAlignment;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author nkabiliravi
 */
public class ExcelCellRenderer extends DefaultTableCellRenderer {

    public ExcelCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ExcelTableModel excelTableModel = (ExcelTableModel) table.getModel();
        Row sheetRow = excelTableModel.getSheet().getRow(row);
        if (sheetRow != null) {
            Cell sheetCell = sheetRow.getCell(column);
            if (sheetCell != null) {
                CellStyle cellStyle = sheetCell.getCellStyle();
                JLabel label = ((JLabel) component);

                setAlignment(cellStyle, label);

                setFontStyle(sheetCell, cellStyle, label);
            }
        }
        return component;
    }

    private void setAlignment(CellStyle cellStyle, JLabel label) {
        int horizontalAlignment = cellStyle.getAlignment();
        HorizontalCellAlignment horizontalCellAlignment = HorizontalCellAlignment
                .fromModelValue((short) horizontalAlignment);
        label.setHorizontalAlignment(horizontalCellAlignment.getViewValue());

        short verticalAlignment = cellStyle.getVerticalAlignment();
        VerticalCellAlignment verticalCellAlignment = VerticalCellAlignment
                .fromModelValue(verticalAlignment);
        label.setVerticalAlignment(verticalCellAlignment.getViewValue());
    }

    private void setFontStyle(Cell sheetCell, CellStyle cellStyle, JLabel label) {
        org.apache.poi.ss.usermodel.Font font = sheetCell.getSheet().getWorkbook().getFontAt(cellStyle.getFontIndex());
        int sytle = Font.PLAIN;
        if (font.getBold()) {
            sytle = Font.BOLD;
        }

        if (font.getItalic()) {
            sytle += Font.ITALIC;
        }

        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();

        if (font.getUnderline() == org.apache.poi.ss.usermodel.Font.U_SINGLE) {
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        } else {
            fontAttributes.put(TextAttribute.UNDERLINE, -1);
        }

        Font viewFont = new Font(label.getFont().getFamily(),
                sytle, label.getFont().getSize());
        viewFont = viewFont.deriveFont(fontAttributes);
        label.setFont(viewFont);

    }

}
