package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.ExcelChartModel;
import com.gtanalysis.gtexcel.model.WorkbookModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class AddChartAction extends BaseAction {

    private final ExcelChartModel excelChartModel;

    public AddChartAction(WorkbookModel workbookModel, XSSFWorkbook workbook, ExcelChartModel excelChartModel) {
        super(workbookModel, workbook);
        this.excelChartModel = excelChartModel;
    }

    public ExcelChartModel getExcelChartModel() {
        return excelChartModel;
    }

    @Override
    public void redo() {
        
    }

    @Override
    public void undo() {
    }

}
