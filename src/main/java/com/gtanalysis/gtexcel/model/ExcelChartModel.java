/*
 */
package com.gtanalysis.gtexcel.model;

import com.gtanalysis.gtexcel.util.RangeUtil;
import com.gtanalysis.gtexcel.util.WorkbookUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.data.DomainOrder;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author nkabiliravi
 */
public class ExcelChartModel extends AbstractDataset implements PieDataset, CategoryDataset, XYDataset {

    private static final int DATA_RANGE_GAP_CELLS_NUM = 40;
    private static final int DATA_RANGE_MAX_NUM = 100;
    private static final int X_DATA_RANGE_STARTING_INDEX = DATA_RANGE_GAP_CELLS_NUM;
    private static final int Y_DATA_RANGE_STARTING_INDEX = X_DATA_RANGE_STARTING_INDEX + DATA_RANGE_MAX_NUM + DATA_RANGE_GAP_CELLS_NUM;

    private final Row metaDataRow;
    private final WorkbookModel workbookModel;

    public ExcelChartModel(Row metaDataRow, WorkbookModel workbookModel) {
        this.metaDataRow = metaDataRow;
        this.workbookModel = workbookModel;
    }

    public Row getMetaDataRow() {
        return metaDataRow;
    }

    public List<ExcelTableModel> getTableModels() {
        return workbookModel.getTableModels();
    }

    private Sheet getMetaDataSheet() {
        return metaDataRow.getSheet();
    }

    private Workbook getWorkbook() {
        return getMetaDataSheet().getWorkbook();
    }

    public String getUuid() {
        return getCellStringValue(0);
    }

    public void setUuid(String uuid) {
        setCellStringValue(0, uuid);
    }

    public String getName() {
        return getCellStringValue(1);
    }

    public void setName(String name) {
        setCellStringValue(1, name);
    }

    public ExcelChartType getChartType() {
        String cellValue = getCellStringValue(2);
        return cellValue != null ? ExcelChartType.valueOf(cellValue.toUpperCase()) : null;
    }

    public void setChartType(ExcelChartType chartType) {
        setCellStringValue(2, chartType.name());
    }

    public String getContainerSheetName() {
        return getCellStringValue(3);
    }

    public void setContainerSheetName(String containerSheetName) {
        setCellStringValue(3, containerSheetName);
    }

    public String getXCategoryRangeValue() {
        return getCellStringValue(4);
    }

    public void setXCategoryRangeValue(String categoryRange) {
        setCellStringValue(4, categoryRange);
    }

    public String getYCategoryRangeValue() {
        return getCellStringValue(5);
    }

    public void setYCategoryRangeValue(String categoryRange) {
        setCellStringValue(5, categoryRange);
    }

    public String getXDataRange(int index) {
        if (index > DATA_RANGE_MAX_NUM) {
            throw new RuntimeException("X data range index cannot be more than 100");
        }
        return getCellStringValue(index + X_DATA_RANGE_STARTING_INDEX);
    }

    public void setXDataRange(int index, String xDataRange) {
        if (index > DATA_RANGE_MAX_NUM) {
            throw new RuntimeException("X data range index cannot be more than 100");
        }
        setCellStringValue(index + X_DATA_RANGE_STARTING_INDEX, xDataRange);
    }

    public String getYDataRange(int index) {
        if (index >= DATA_RANGE_MAX_NUM) {
            throw new RuntimeException("Y data range index cannot be more than 100");
        }
        return getCellStringValue(index + Y_DATA_RANGE_STARTING_INDEX);
    }

    public void setYDataRange(int index, String yDataRange) {
        if (index >= DATA_RANGE_MAX_NUM) {
            throw new RuntimeException("X data range index cannot be more than 100");
        }
        setCellStringValue(index + Y_DATA_RANGE_STARTING_INDEX, yDataRange);
    }

    public String getCellStringValue(int index) {
        Cell cell = metaDataRow.getCell(index);
        if (cell == null) {
            cell = workbookModel.getExcelDataProvider().createCell(metaDataRow, index);
        }
        return cell.getStringCellValue();
    }

    public void setCellStringValue(int index, String value) {
        Cell cell = metaDataRow.getCell(index);
        if (cell == null) {
            cell = workbookModel.getExcelDataProvider().createCell(metaDataRow, index);
        }
        cell.setCellValue(value);
    }

    public double getCellNumericValue(int index) {
        Cell cell = metaDataRow.getCell(index);
        if (cell == null) {
            cell = workbookModel.getExcelDataProvider().createCell(metaDataRow, index);
        }
        return cell.getNumericCellValue();
    }

    public void setCellNumericValue(int index, double value) {
        Cell cell = metaDataRow.getCell(index);
        if (cell == null) {
            cell = workbookModel.getExcelDataProvider().createCell(metaDataRow, index);
        }
        cell.setCellValue(value);
    }
    
    public ExcelCellRange getXCategoryRange() {
        return RangeUtil.parseCellRange(getXCategoryRangeValue());
    }

    public ExcelCellRange getYCategoryRange() {
        return RangeUtil.parseCellRange(getYCategoryRangeValue());
    }
    
    public List<ExcelCellRange> getXDataRanges() {
        List<ExcelCellRange> result = new ArrayList<>();
        for (int i = 0; i < DATA_RANGE_MAX_NUM; i++) {
            String dataRange = getXDataRange(i);
            if (StringUtils.isNotEmpty(dataRange)) {
                result.add(RangeUtil.parseCellRange(dataRange));
            }
        }
        return result;
    }
    
    public void setXDataRages(List<ExcelCellRange> cellRanges) {
        for (int i = 0; i < DATA_RANGE_MAX_NUM; i++) {
            if(i < cellRanges.size()) {
                setXDataRange(i, cellRanges.get(i).toString());
            } else {
                setXDataRange(i, null);
            }
        }
    }

    public List<ExcelCellRange> getYDataRanges() {
        List<ExcelCellRange> result = new ArrayList<>();
        for (int i = 0; i < DATA_RANGE_MAX_NUM; i++) {
            String dataRange = getYDataRange(i);
            if (StringUtils.isNotEmpty(dataRange)) {
                result.add(RangeUtil.parseCellRange(dataRange));
            }
        }
        return result;
    }

    public void setYDataRages(List<ExcelCellRange> cellRanges) {
        for (int i = 0; i < DATA_RANGE_MAX_NUM; i++) {
            if(i < cellRanges.size()) {
                setYDataRange(i, cellRanges.get(i).toString());
            } else {
                setYDataRange(i, null);
            }
        }
    }
    
    public int getXDataRangesCount() {
        return getXDataRanges().size();
    }

    public int getYDataRangesCount() {
        return getYDataRanges().size();
    }

    // =========================================================================
    // Pie chart dataset begins
    @Override
    public Comparable getKey(int index) {
        return getXCategoryName(index);
    }

    @Override
    public int getIndex(Comparable key) {
        List<Cell> list = getXDataRangeCells(0);
        for (int i = 0; i < list.size(); i++) {
            Comparable cellKey = getXCategoryName(i);
            if (cellKey.compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List getKeys() {
        List result = new ArrayList();
        List<Cell> list = getXDataRangeCells(0);
        for (int i = 0; i < list.size(); i++) {
            result.add(getXCategoryName(i));
        }
        return result;
    }

    @Override
    public Number getValue(Comparable key) {
        List<Cell> list = getXDataRangeCells(0);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = list.get(i);
            String cellKey = getXCategoryName(i);
            if (key.compareTo(cellKey) == 0) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    return cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    FormulaEvaluator evaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
                    Object obj = WorkbookUtil.getEvaluatedCellValue(evaluator, cell);
                    return obj instanceof Double ? (Double) obj : 0;
                }
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        List<Cell> list = getXDataRangeCells(0);
        return list.size();
    }

    @Override
    public Number getValue(int index) {
        List<Cell> list = getXDataRangeCells(0);
        Cell cell = list.get(index);
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            FormulaEvaluator evaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
            Object obj = WorkbookUtil.getEvaluatedCellValue(evaluator, cell);
            return obj instanceof Double ? (Double) obj : 0;
        }
        return 0;
    }

    // Pie chart dataset ends
    // =========================================================================
    // =========================================================================
    //Bar chart dataset begins
    @Override
    public Comparable getRowKey(int row) {
        return getXCategoryName(row);
    }

    @Override
    public int getRowIndex(Comparable key) {
        List<Cell> list = getXCategoryRangeCells();
        for (int i = 0; i < list.size(); i++) {
            Comparable cellKey = getXCategoryName(i);
            if (cellKey.compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List getRowKeys() {
        List result = new ArrayList();
        List<Cell> list = getXCategoryRangeCells();
        for (int i = 0; i < list.size(); i++) {
            result.add(getXCategoryName(i));
        }
        return result;
    }

    @Override
    public Comparable getColumnKey(int column) {
        return getYCategoryName(column);
    }

    @Override
    public int getColumnIndex(Comparable key) {
        List<Cell> list = getYCategoryRangeCells();
        for (int i = 0; i < list.size(); i++) {
            Comparable cellKey = getYCategoryName(i);
            if (cellKey.compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List getColumnKeys() {
        List result = new ArrayList();
        List<Cell> list = getYCategoryRangeCells();
        for (int i = 0; i < list.size(); i++) {
            result.add(getYCategoryName(i));
        }
        return result;
    }

    @Override
    public Number getValue(Comparable rowKey, Comparable columnKey) {
        int columnIndex = getColumnIndex(columnKey);
        int rowIndex = getRowIndex(rowKey);
        return getValue(rowIndex, columnIndex);
    }

    @Override
    public int getRowCount() {
        return getXCategoryRangeCells().size();
    }

    @Override
    public int getColumnCount() {
        return getYCategoryRangeCells().size();
    }

    @Override
    public Number getValue(int row, int column) {
        return getXCellValue(column, row);
    }

    //Bar chart dataset ends
    // =========================================================================
    // =========================================================================
    //XY chart dataset begins
    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.NONE;
    }

    @Override
    public int getItemCount(int series) {
        List<Cell> xDataRangeCells = getXDataRangeCells(series);
        return xDataRangeCells.size();
    }

    @Override
    public Number getX(int series, int item) {
        return getXCellValue(series, item);
    }

    @Override
    public double getXValue(int series, int item) {
        return getXCellValue(series, item).doubleValue();
    }

    @Override
    public Number getY(int series, int item) {
        return getYCellValue(series, item);
    }

    @Override
    public double getYValue(int series, int item) {
        return getYCellValue(series, item).doubleValue();
    }

    @Override
    public int getSeriesCount() {
        return getXDataRangesCount();
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return getXCategoryName(series);
    }

    @Override
    public int indexOf(Comparable seriesKey) {
        List<Cell> categoryCells = getXCategoryRangeCells();
        for (int i = 0; i < categoryCells.size(); i++) {
            String categoryName = getXCategoryName(i);
            if (categoryName.equals(seriesKey)) {
                return i;
            }
        }
        return -1;
    }

    // XY chart dataset ends
    // =========================================================================
    private Number getXCellValue(int column, int row) {
        List<Cell> xlist = getXDataRangeCells(column);
        if (row > -1 && row < xlist.size()) {
            Cell xcell = xlist.get(row);
            if (xcell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return xcell.getNumericCellValue();
            } else if (xcell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                FormulaEvaluator evaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
                Object obj = WorkbookUtil.getEvaluatedCellValue(evaluator, xcell);
                return obj instanceof Double ? (Double) obj : 0;
            }
        }
        return 0;
    }

    private Number getYCellValue(int column, int row) {
        List<Cell> ylist = getYDataRangeCells(column);
        if (row > -1 && row < ylist.size()) {
            Cell ycell = ylist.get(row);
            if (ycell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return ycell.getNumericCellValue();
            } else if (ycell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                FormulaEvaluator evaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
                Object obj = WorkbookUtil.getEvaluatedCellValue(evaluator, ycell);
                return obj instanceof Double ? (Double) obj : 0;
            }
        }
        return 0;
    }

    private String retrieveCategoryName(int index, List<Cell> categoryCells) {
        String result;
        if (index < categoryCells.size()) {
            Cell categoryCell = categoryCells.get(index);
            result = WorkbookUtil.getCellStringValue(categoryCell);
        } else {
            result = "" + 1;
        }
        return result;
    }

    private List<Cell> retrieveRangeCells(String dataRange) {
        List<Cell> result = new ArrayList<>();
        List<ExcelCellRange> excelCellRanges = RangeUtil.parseCellRanges(dataRange);
        excelCellRanges.stream().forEach((excelCellRange) -> {
            Sheet sheet = getWorkbook().getSheet(excelCellRange.getSheetName());
            if (sheet != null) {
                int sheetIndex = getWorkbook().getSheetIndex(sheet);
                ExcelTableModel tableModel = getTableModels().get(sheetIndex);
                int fromColumn = tableModel.getColumnIndex(excelCellRange.getFromColumn());
                int fromRow = excelCellRange.getFromRow() - 1;
                int toColumn = tableModel.getColumnIndex(excelCellRange.getToColumn());
                int toRow = excelCellRange.getToRow() - 1;
                for (int i = fromColumn; i <= toColumn; i++) {
                    for (int j = fromRow; j <= toRow; j++) {
                        Row row = sheet.getRow(j);
                        result.add(row.getCell(i));
                    }
                }
            }
        });
        return result;
    }

    private String getXCategoryName(int index) {
        List<Cell> categoryCells = getXCategoryRangeCells();
        return retrieveCategoryName(index, categoryCells);
    }

    private String getYCategoryName(int index) {
        List<Cell> categoryCells = getYCategoryRangeCells();
        return retrieveCategoryName(index, categoryCells);
    }

    private List<Cell> getXCategoryRangeCells() {
        String categoryRange = getXCategoryRangeValue();
        return retrieveRangeCells(categoryRange);
    }

    private List<Cell> getYCategoryRangeCells() {
        String categoryRange = getYCategoryRangeValue();
        return retrieveRangeCells(categoryRange);
    }

    private List<Cell> getXDataRangeCells(int index) {
        String dataRange = getXDataRange(index);
        return retrieveRangeCells(dataRange);
    }

    private List<Cell> getYDataRangeCells(int index) {
        String dataRange = getYDataRange(index);
        return retrieveRangeCells(dataRange);
    }

}
