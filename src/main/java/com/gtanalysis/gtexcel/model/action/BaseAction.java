package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.WorkbookModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public abstract class BaseAction implements Action {
    
    protected final WorkbookModel workbookModel;
    protected final XSSFWorkbook workbook;

    public BaseAction(WorkbookModel workbookModel, XSSFWorkbook workbook) {
        this.workbookModel = workbookModel;
        this.workbook = workbook;
    }
    
    
}
