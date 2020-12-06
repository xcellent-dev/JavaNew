/*
 */
package com.gtanalysis.gtexcel.model;

import com.gtanalysis.gtexcel.model.action.ActionModel;
import com.gtanalysis.gtexcel.model.listener.ExcelTableModelChangeCellEvent;
import com.gtanalysis.gtexcel.model.listener.ExcelTableModelListener;
import com.gtanalysis.gtexcel.model.listener.WorkbookEvent;
import com.gtanalysis.gtexcel.model.listener.WorkbookListener;
import com.gtanalysis.gtexcel.provider.ExcelDataProvider;
import com.gtanalysis.gtexcel.util.ChartUtil;
import com.gtanalysis.gtexcel.util.WorkbookUtil;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author nkabiliravi
 */
public class WorkbookModel implements ExcelTableModelListener {

    private static final String SHEET_NAME_PATTERN = "\\w+[\\s\\w]*";

    private final List<WorkbookListener> workbookListeners;

    private List<ExcelTableModel> tableModels;
    private final List<ExcelChartModel> chartModels;

    private final ExcelDataProvider excelDataProvider;

    private final ActionModel actionModel;

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public WorkbookModel() {
        workbookListeners = new ArrayList<>();
        chartModels = new ArrayList<>();
        excelDataProvider = new ExcelDataProvider();
        this.actionModel = new ActionModel(this);
    }

    public void addPropertyChangeListener(
            String propertyName,
            PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(
            String propertyName,
            PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public ExcelDataProvider getExcelDataProvider() {
        return excelDataProvider;
    }

    public ActionModel getActionModel() {
        return actionModel;
    }

    public void addWorkbookListener(WorkbookListener listener) {
        workbookListeners.add(listener);
    }

    public void removeWorkbookListener(WorkbookListener listener) {
        workbookListeners.remove(listener);
    }

    public List<ExcelTableModel> getTableModels() {
        return tableModels;
    }

    public List<ExcelChartModel> getChartModels() {
        return chartModels;
    }

    public void newWorkbook() {
        excelDataProvider.newWorkbook();
        fireWorkbookCreated();
        clearActionModel();
    }

    private void clearActionModel() {
        this.actionModel.clear();
        firePropertyChange("undoEnabled", !isUndoEnabled(), isUndoEnabled());
        firePropertyChange("redoEnabled", !isRedoEnabled(), isRedoEnabled());
    }

    public void loadWorkbook(File file) {
        try {
            excelDataProvider.loadWorkbook(file);
            initTableModels();
            initChartModel();
            fireWorkbookLoaded();
            clearActionModel();
        } catch (IOException ex) {
            Logger.getLogger(ExcelTableModel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void saveWorkbook(File file) {
        try {
            excelDataProvider.saveWorkbook(file);
            fireWorkbookSaved();
            clearActionModel();
        } catch (IOException ex) {
            Logger.getLogger(WorkbookModel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void closeWorkbook() {
        try {
            excelDataProvider.closeWorkbook();
            fireWorkbookClosed();
            clearActionModel();
        } catch (IOException ex) {
            Logger.getLogger(WorkbookModel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public Workbook getWorkbook() {
        return excelDataProvider.getWorkbook();
    }

    public String generateNewDefaultSheetName() {
        return "Sheet " + (tableModels.size() + 1);
    }

    public ExcelTableModel createExcelTableModel(String sheetName) {
        if (!sheetName.matches(SHEET_NAME_PATTERN)) {
            throw new RuntimeException("Invalid character in sheet name");
        }
        Sheet sheet = this.excelDataProvider.createSheet(sheetName);
        this.excelDataProvider.initModel(sheet, 8, 10);
        ExcelTableModel excelTableModel = new ExcelTableModel(this.excelDataProvider, sheet);
        excelTableModel.addExcelTableModelListener(this);
        return excelTableModel;
    }

    public void exportToExcelCSV(File file) {
        try {
            excelDataProvider.exportToExcelCSV(file);
        } catch (IOException ex) {
            Logger.getLogger(WorkbookModel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void exportToUnixCSV(File file) {
        try {
            excelDataProvider.exportToUnixCSV(file);
        } catch (IOException ex) {
            Logger.getLogger(WorkbookModel.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<String> retrieveSheetNameList() {
        List<String> result = new ArrayList<>();
        for (Sheet sheet : getWorkbook()) {
            int sheetIndex = getWorkbook().getSheetIndex(sheet);
            if (!getWorkbook().isSheetHidden(sheetIndex)
                    && !getWorkbook().isSheetVeryHidden(sheetIndex)) {
                result.add(sheet.getSheetName());
            }
        }
        return result;
    }

    public String[] retrieveSheetNames() {
        List<String> sheetNameList = retrieveSheetNameList();
        String[] result = new String[sheetNameList.size()];
        sheetNameList.toArray(result);
        return result;
    }

    private void initTableModels() {
        int numberOfSheets = excelDataProvider.getWorkbook().getNumberOfSheets();
        if (numberOfSheets > 0) {
            tableModels = new ArrayList<>();
            for (int i = 0; i < numberOfSheets; i++) {
                ExcelTableModel excelTableModel = new ExcelTableModel(this.excelDataProvider,
                        excelDataProvider.getWorkbook().getSheetAt(i));
                excelTableModel.addExcelTableModelListener(this);
                tableModels.add(excelTableModel);
            }
        }
    }

    private Sheet getChartsMetadataSheet() {
        return excelDataProvider.getChartsMetadataSheet();
    }

    private Row findChartRow(String chartUuid) {
        Row chartRow = null;
        int lastRowNum = getChartsMetadataSheet().getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = getChartsMetadataSheet().getRow(i);
            Cell idCell = row.getCell(0);
            if (idCell.getStringCellValue().equals(chartUuid)) {
                chartRow = row;
            }
        }
        return chartRow;
    }

    private void initChartModel() {
        chartModels.clear();
        Sheet sheet = getChartsMetadataSheet();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (ChartUtil.validateChartMetaDatatRow(row)) {
                chartModels.add(new ExcelChartModel(row, this));
            }
        }
    }

    private void fireWorkbookLoaded() {
        workbookListeners.stream().forEach((workbookListener) -> {
            workbookListener.workbookLoaded(new WorkbookEvent(this, getWorkbook()));
        });
    }

    private void fireWorkbookSaved() {
        workbookListeners.stream().forEach((workbookListener) -> {
            workbookListener.workbookSaved(new WorkbookEvent(this, getWorkbook()));
        });
    }

    private void fireWorkbookClosed() {
        workbookListeners.stream().forEach((workbookListener) -> {
            workbookListener.workbookClosed(new WorkbookEvent(this, getWorkbook()));
        });
    }

    private void fireWorkbookCreated() {
        workbookListeners.stream().forEach((workbookListener) -> {
            workbookListener.workbookCreated(new WorkbookEvent(this, getWorkbook()));
        });
    }

    public ExcelChartModel addChartModel(String chartTitle,
            ExcelChartType chartType,
            String containerSheetName,
            ExcelCellRange xLabelRange,
            ExcelCellRange yLabelRange,
            List<ExcelCellRange> xDataRanges,
            List<ExcelCellRange> yDataRanges) {
        Sheet chartsMetadataSheet = excelDataProvider.getChartsMetadataSheet();
        Row metaDataRow = excelDataProvider.createChartRow(chartsMetadataSheet);
        ExcelChartModel excelChartModel = new ExcelChartModel(
                metaDataRow, this);
        excelChartModel.setUuid(UUID.randomUUID().toString());
        excelChartModel.setName(chartTitle);
        excelChartModel.setChartType(chartType);
        excelChartModel.setContainerSheetName(containerSheetName);
        excelChartModel.setXCategoryRangeValue(xLabelRange.toString());
        excelChartModel.setYCategoryRangeValue(yLabelRange.toString());
        for (int i = 0; i < xDataRanges.size(); i++) {
            ExcelCellRange xDataRange = xDataRanges.get(i);
            excelChartModel.setXDataRange(i, xDataRange.toString());
        }
        for (int i = 0; i < yDataRanges.size(); i++) {
            ExcelCellRange yDataRange = yDataRanges.get(i);
            excelChartModel.setYDataRange(i, yDataRange.toString());
        }
        chartModels.add(excelChartModel);
        return excelChartModel;
    }

    public void removeChartModel(ExcelChartModel excelChartModel) {
        Row metaDataRow = excelChartModel.getMetaDataRow();
        Sheet chartSheet = metaDataRow.getSheet();
        chartSheet.removeRow(metaDataRow);
        chartModels.remove(excelChartModel);
    }

    private void firePropertyChange(String propertyName,
            boolean oldValue, boolean newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void undo() {
        this.actionModel.undo();
        firePropertyChange("undoEnabled", !isUndoEnabled(), isUndoEnabled());
    }

    public void redo() {
        this.actionModel.redo();
        firePropertyChange("redoEnabled", !isRedoEnabled(), isRedoEnabled());
    }

    public boolean isUndoEnabled() {
        return this.actionModel.isUndoEnabled();
    }

    public boolean isRedoEnabled() {
        return this.actionModel.isRedoEnabled();
    }

    @Override
    public void cellChanged(ExcelTableModelChangeCellEvent e) {
        ExcelTableModel excelTableModel = (ExcelTableModel) e.getSource();
        System.out.println(excelTableModel.getSheet().getSheetName());
        System.out.println("Old Cell Type = " + e.getOldCellModel().getValueType());
        System.out.println("Old Cell Value = " + e.getOldCellModel().getValue());
        System.out.println("New Cell Type = " + e.getCell().getCellType());
        System.out.println("New Cell Value = " + WorkbookUtil.getCellStringValue(e.getCell()));
        System.out.println("Column = " + e.getColumn());
        System.out.println("First Row = " + e.getRow());
        actionModel.createChangeCellValueAction(e.getCell(),
                e.getOldCellModel());
        firePropertyChange("undoEnabled", !isUndoEnabled(), isUndoEnabled());
        firePropertyChange("redoEnabled", !isRedoEnabled(), isRedoEnabled());
    }

}
