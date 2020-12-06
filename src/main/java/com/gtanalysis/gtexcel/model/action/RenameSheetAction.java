package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.WorkbookModel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class RenameSheetAction extends BaseAction {

    private final Sheet sheet;
    
    private final String oldSheetName;

    public RenameSheetAction(WorkbookModel workbookModel, XSSFWorkbook workbook, Sheet sheet, String oldSheetName) {
        super(workbookModel, workbook);
        this.sheet = sheet;
        this.oldSheetName = oldSheetName;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public String getOldSheetName() {
        return oldSheetName;
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
