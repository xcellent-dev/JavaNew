package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.WorkbookModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class ChangeCellStyleAction extends BaseAction {

    private final Cell cell;

    public ChangeCellStyleAction(WorkbookModel workbookModel, XSSFWorkbook workbook, Cell cell) {
        super(workbookModel, workbook);
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
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
