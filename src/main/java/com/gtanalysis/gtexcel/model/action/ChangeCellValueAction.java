package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.CellModel;
import com.gtanalysis.gtexcel.model.WorkbookModel;
import com.gtanalysis.gtexcel.util.WorkbookUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class ChangeCellValueAction extends BaseAction {
    
    private final Cell cell;
    private CellModel oldValue;

    public ChangeCellValueAction(WorkbookModel workbookModel, 
            XSSFWorkbook workbook, 
            Cell cell, CellModel oldValue) {
        super(workbookModel, workbook);
        this.cell = cell;
        this.oldValue = oldValue;
    }

    public Cell getCell() {
        return cell;
    }

    public CellModel getOldValue() {
        return oldValue;
    }
    
    @Override
    public void redo() {
        CellModel currentCellModel = WorkbookUtil.getCellModel(cell);
        WorkbookUtil.copyCellValue(oldValue, cell);
        this.oldValue = currentCellModel;
    }

    @Override
    public void undo() {
        CellModel currentCellModel = WorkbookUtil.getCellModel(cell);
        WorkbookUtil.copyCellValue(oldValue, cell);
        this.oldValue = currentCellModel;
    }
    
    
}
