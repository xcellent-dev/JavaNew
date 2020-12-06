package com.gtanalysis.gtexcel.model.listener;

import com.gtanalysis.gtexcel.model.CellModel;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author nkabiliravi
 */
public class ExcelTableModelChangeCellEvent {

    private final Object source;
    
    private final int column;
    
    private final int row;
    
    private final Cell cell;
    
    private final CellModel oldCellModel;

    public ExcelTableModelChangeCellEvent(Object source, int column, int row, 
            Cell cell,
            CellModel oldCellModel) {
        this.source = source;
        this.column = column;
        this.row = row;
        this.cell = cell;
        this.oldCellModel = oldCellModel;
    }

    public Object getSource() {
        return source;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Cell getCell() {
        return cell;
    }

    public CellModel getOldCellModel() {
        return oldCellModel;
    }


}
