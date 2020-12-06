/*
 */
package com.gtanalysis.gtexcel.provider;

import com.gtanalysis.gtexcel.model.ExcelTransformer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class ExcelDataProvider {

    public static final String CHARTS_METADATA_SHEET_NAME = "$$!!CHARTS_METADATA!!$$";
    private static final int DEFAULT_WIDTH_SIZE = 65 * 80;
    private static final int DEFAULT_HEIGHT_SIZE = 16 * 20;

    private Workbook workbook;
    private final ExcelTransformer excelTransformer = new ExcelTransformer();

    public ExcelDataProvider() {

    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void newWorkbook() {
        workbook = new XSSFWorkbook();
        initWorkbook();
    }

    public void loadWorkbook(File file) throws IOException {
        if (file.getName().endsWith(".xlsx")) {
            workbook = loadDocumentXSSF(new FileInputStream(file));
        } else if (file.getName().endsWith(".xls")) {
            workbook = loadDocumentHSSF(new FileInputStream(file));
        } else {
            throw new RuntimeException("File type is not supported");
        }
        initWorkbook();
    }

    public void saveWorkbook(File file) throws IOException {
        if (workbook != null) {
            if (file.getName().endsWith(".xls")) {
                if (workbook instanceof XSSFWorkbook) {
                    HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                    excelTransformer.transform(workbook, hssfWorkbook);
                    workbook = hssfWorkbook;
                }
            } else if (file.getName().endsWith(".xlsx")) {
                if (workbook instanceof HSSFWorkbook) {
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                    excelTransformer.transform(workbook, xssfWorkbook);
                    workbook = xssfWorkbook;
                }
            } else {
                throw new RuntimeException("File type is not supported");
            }
            try (FileOutputStream fileOS = new FileOutputStream(file)) {
                workbook.write(fileOS);
            }
        }
    }

    public void closeWorkbook() throws IOException {
        if (workbook != null) {
            workbook.close();
            workbook = null;
        }
    }

    private void exportToCSV(File file, int formattingConvention) throws IOException {
        if (workbook != null) {
            List<List<String>> csvData = excelTransformer.convertToCSV(workbook);
            FileWriter fileWriter = new FileWriter(file);
            excelTransformer.saveCSVFile(fileWriter, csvData, formattingConvention);
        }
    }

    public void exportToExcelCSV(File file) throws IOException {
        exportToCSV(file, ExcelTransformer.EXCEL_STYLE_ESCAPING);
    }

    public void exportToUnixCSV(File file) throws IOException {
        exportToCSV(file, ExcelTransformer.UNIX_STYLE_ESCAPING);
    }

    private XSSFWorkbook loadDocumentXSSF(InputStream inputStream) throws IOException {
        return new XSSFWorkbook(inputStream);
    }

    private HSSFWorkbook loadDocumentHSSF(InputStream inputStream) throws IOException {
        return new HSSFWorkbook(inputStream);
    }

    public Sheet getChartsMetadataSheet() {
        return workbook != null ? workbook.getSheet(CHARTS_METADATA_SHEET_NAME) : null;
    }

    public void initChartSheet() {
        if (workbook.getSheetIndex(CHARTS_METADATA_SHEET_NAME) < 0) {
            createSheet(CHARTS_METADATA_SHEET_NAME);
        }
        int lastIndex = workbook.getNumberOfSheets() - 1;
        workbook.setSheetOrder(CHARTS_METADATA_SHEET_NAME, lastIndex);
        workbook.setSheetHidden(lastIndex, true);
    }

    private void initWorkbook() {
        workbook.addToolPack(JNIFunctionSetProvider.getInstance().getToolPack());
        initChartSheet();
    }

    public Sheet createSheet(String sheetName) {
        Sheet sheet = this.getWorkbook().createSheet(sheetName);
        sheet.setDefaultColumnWidth(8 * 100);
        sheet.setDefaultRowHeight((short) (DEFAULT_HEIGHT_SIZE));
        return sheet;
    }

    public Row createRow(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.setHeight((short) (DEFAULT_HEIGHT_SIZE));
        return row;
    }
        
    public Row createChartRow(Sheet sheet) {
        int numberOfRows = getChartsMetadataSheet().getPhysicalNumberOfRows();
        for(int i = 0; i < numberOfRows; i++) {
            Row row = sheet.getRow(i);
            if(row == null || row.getPhysicalNumberOfCells() == 0) {
                return createRow(sheet, i);
            }
        }
        return createRow(sheet, numberOfRows);
    }


    public Cell createCell(Row row, int cellIndex) {
        Cell cell = row.createCell(cellIndex);
        row.getSheet().setColumnWidth(cellIndex, DEFAULT_WIDTH_SIZE);
        return cell;
    }

    public void initModel(Sheet sheet, int rowCount, int columnCount) {
        for (int i = 0; i < rowCount; i++) {
            Row row = createRow(sheet, i);
            for (int j = 0; j < columnCount; j++) {
                createCell(row, j);
            }
        }
    }

}
