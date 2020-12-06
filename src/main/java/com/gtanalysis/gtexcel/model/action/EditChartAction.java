package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.ExcelChartModel;
import com.gtanalysis.gtexcel.model.WorkbookModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class EditChartAction extends BaseAction {

    private final ExcelChartModel excelChartModel;

    public EditChartAction(WorkbookModel workbookModel, XSSFWorkbook workbook, ExcelChartModel excelChartModel) {
        super(workbookModel, workbook);
        this.excelChartModel = excelChartModel;
    }

    public ExcelChartModel getExcelChartModel() {
        return excelChartModel;
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
