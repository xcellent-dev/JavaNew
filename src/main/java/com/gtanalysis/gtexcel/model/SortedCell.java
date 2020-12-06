package com.gtanalysis.gtexcel.model;

/**
 *
 * @author Nasim
 */
public class SortedCell {
    
    private int rowIndex;
    private int columnIndex;

    public SortedCell() {
    }

    public SortedCell(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }    
    
}
