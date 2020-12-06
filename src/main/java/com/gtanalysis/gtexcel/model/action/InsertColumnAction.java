package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.WorkbookModel;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class InsertColumnAction extends BaseAction {

    private final List<Cell> column;

    public InsertColumnAction(WorkbookModel workbookModel, XSSFWorkbook workbook, List<Cell> column) {
        super(workbookModel, workbook);
        this.column = column;
    }

    public List<Cell> getColumn() {
        return column;
    }

    @Override
    public void redo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
