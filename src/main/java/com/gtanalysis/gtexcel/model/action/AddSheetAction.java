package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.WorkbookModel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class AddSheetAction extends BaseAction {

    private final Sheet sheet;

    public AddSheetAction(WorkbookModel workbookModel, XSSFWorkbook workbook, Sheet sheet) {
        super(workbookModel, workbook);
        this.sheet = sheet;
    }

    public Sheet getSheet() {
        return sheet;
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
