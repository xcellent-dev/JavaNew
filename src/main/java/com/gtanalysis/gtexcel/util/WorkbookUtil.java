/*
 */
package com.gtanalysis.gtexcel.util;

import com.gtanalysis.gtexcel.model.CellModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author nkabiliravi
 */
public class WorkbookUtil {

    public static Object getEvaluatedCellValue(FormulaEvaluator evaluator, Cell cell) {
        CellValue cellValue = evaluator.evaluate(cell);

        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return cellValue.getBooleanValue();
            case Cell.CELL_TYPE_NUMERIC:
                return cellValue.getNumberValue();
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
            default:
                return null;
        }
    }

    public static Object getCellValue(Cell cell) {
        Object cellValue;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = cell.getErrorCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = null;
        }
        return cellValue;
    }

    public static CellModel getCellModel(Cell cell) {
        CellModel cellModel = new CellModel();
        cellModel.setColumnIndex(cell.getColumnIndex());
        cellModel.setRowIndex(cell.getRowIndex());
        cellModel.setValueType(cell.getCellType());
        cellModel.setValue(getCellValue(cell));
        return cellModel;
    }

    public static String getCellStringValue(Cell cell) {
        Object cellValue = getCellValue(cell);
        return cellValue != null ? cellValue.toString() : "";
    }

    public static void copyCellValue(Cell sourceCell, Cell targetCell) throws FormulaParseException {
        switch (sourceCell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK:
                targetCell.setCellType(Cell.CELL_TYPE_BLANK);
                targetCell.setCellFormula(null);
                break;
            default:
                targetCell.setCellType(sourceCell.getCellType());

        }
    }

    public static void copyCellValue(CellModel sourceCell, Cell targetCell) throws FormulaParseException {
        switch (sourceCell.getValueType()) {
            case Cell.CELL_TYPE_STRING:
                targetCell.setCellValue((String) sourceCell.getValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                targetCell.setCellValue((Double) sourceCell.getValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                targetCell.setCellValue((Boolean) sourceCell.getValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_ERROR:
                targetCell.setCellErrorValue((Byte) sourceCell.getValue());
                targetCell.setCellFormula(null);
                break;
            case Cell.CELL_TYPE_FORMULA:
                targetCell.setCellFormula((String) sourceCell.getValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                targetCell.setCellType(Cell.CELL_TYPE_BLANK);
                targetCell.setCellFormula(null);
                break;
            default:
                targetCell.setCellType(sourceCell.getValueType());

        }
    }

    public static List<CellModel> populateCellModels(Sheet sheet, int columnIndex) {
        List<CellModel> result = new ArrayList<>();
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex);
            Object cellValue = WorkbookUtil.getCellValue(cell);
            result.add(new CellModel(cell.getColumnIndex(), row.getRowNum(), cellValue, cell.getCellType()));
        }
        return result;
    }

    public static List<List<CellModel>> populateSheetCellModels(Sheet sheet) {
        List<List<CellModel>> result = new ArrayList<>();
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
        if (columnCount > 0) {
            for (int i = 0; i < columnCount; i++) {
                result.add(populateCellModels(sheet, i));
            }
        }
        return result;
    }
}
