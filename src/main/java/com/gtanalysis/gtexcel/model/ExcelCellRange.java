/*
 */
package com.gtanalysis.gtexcel.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nkabiliravi
 */
public class ExcelCellRange {

    public static final String CELL_IDENTIFIER_REGEX = "(?<columnName>[A-Za-z]+)(?<rowIndex>[0-9]+)";
    public static final Pattern CELL_IDENTIFIER_PATTERN = Pattern.compile(CELL_IDENTIFIER_REGEX);

    private String sheetName;

    private String fromColumn;

    private Integer fromRow;

    private String toColumn;

    private Integer toRow;

    public ExcelCellRange() {
    }

    public ExcelCellRange(String sheetName, String fromColumn, Integer fromRow, String toColumn, Integer toRow) {
        this.sheetName = sheetName;
        this.fromColumn = fromColumn;
        this.fromRow = fromRow;
        this.toColumn = toColumn;
        this.toRow = toRow;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(String fromColumn) {
        this.fromColumn = fromColumn;
    }

    public Integer getFromRow() {
        return fromRow;
    }

    public void setFromRow(Integer fromRow) {
        this.fromRow = fromRow;
    }

    public String getToColumn() {
        return toColumn;
    }

    public void setToColumn(String toColumn) {
        this.toColumn = toColumn;
    }

    public Integer getToRow() {
        return toRow;
    }

    public void setToRow(Integer toRow) {
        this.toRow = toRow;
    }

    public String getFromCell() {
        if (getFromColumn() != null && getFromRow() != null) {
            return getFromColumn() + getFromRow();
        }
        return null;
    }

    public void setFromCell(String value) {
        Matcher matcher = CELL_IDENTIFIER_PATTERN.matcher(value);
        matcher.find();
        setFromColumn(matcher.group("columnName"));
        setFromRow(Integer.parseInt(matcher.group("rowIndex")));
    }

    public String getToCell() {
        if (getToColumn() != null && getToRow() != null) {
            return getToColumn() + getToRow();
        }
        return null;
    }

    public void setToCell(String value) {
        Matcher matcher = CELL_IDENTIFIER_PATTERN.matcher(value);
        matcher.find();
        setToColumn(matcher.group("columnName"));
        setToRow(Integer.parseInt(matcher.group("rowIndex")));
    }

    @Override
    public String toString() {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append("$");
        keyBuilder.append(sheetName);
        keyBuilder.append(".");
        keyBuilder.append("$");
        keyBuilder.append(fromColumn);
        keyBuilder.append("$");
        keyBuilder.append(fromRow);
        if (toColumn != null && toRow != null) {
            keyBuilder.append(":");
            keyBuilder.append("$");
            keyBuilder.append(toColumn);
            keyBuilder.append("$");
            keyBuilder.append(toRow);
        }
        return keyBuilder.toString();
    }

}
