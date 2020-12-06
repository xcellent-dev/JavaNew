package com.gtanalysis.gtexcel.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

public class ExcelTransformer {

    /**
     * Identifies that the CSV file should obey Excel's formatting conventions
     * with regard to escaping certain embedded characters - the field
     * separator, speech mark and end of line (EOL) character
     */
    public static final int EXCEL_STYLE_ESCAPING = 0;

    /**
     * Identifies that the CSV file should obey UNIX formatting conventions with
     * regard to escaping certain embedded characters - the field separator and
     * end of line (EOL) character
     */
    public static final int UNIX_STYLE_ESCAPING = 1;

    private static final String DEFAULT_SEPARATOR = ",";

    private int lastColumn = 0;
    private int maxRowWidth = 0;
    private final Map<Integer, CellStyle> styleMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter(true);

    public void transform(Workbook workbookOld, Workbook workbookNew) {
        String call = "transform ";
        System.out.println(call + "Workbook");
        Sheet sheetNew;
        Sheet sheetOld;
        workbookNew.setMissingCellPolicy(workbookOld.getMissingCellPolicy());

        for (int i = 0; i < workbookOld.getNumberOfSheets(); i++) {
            sheetOld = workbookOld.getSheetAt(i);
            sheetNew = workbookNew.createSheet(sheetOld.getSheetName());
            this.transform(workbookOld, workbookNew, sheetOld, sheetNew);
        }
        System.out.println(call + "Styles size: " + this.styleMap.size());
        System.out.println(call + "abgeschlossen");
    }

    public List<List<String>> convertToCSV(Workbook workbook) {
        Sheet sheet = null;
        Row row = null;
        int lastRowNum = 0;
        List<List<String>> csvData = new ArrayList<List<String>>();

        System.out.println("Converting files contents to CSV format.");

        // Discover how many sheets there are in the workbook....
        int numSheets = workbook.getNumberOfSheets();

        // and then iterate through them.
        for (int i = 0; i < numSheets; i++) {

            // Get a reference to a sheet and check to see if it contains
            // any rows.
            sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {

                // Note down the index number of the bottom-most row and
                // then iterate through all of the rows on the sheet starting
                // from the very first row - number 1 - even if it is missing.
                // Recover a reference to the row and then call another method
                // which will strip the data from the cells and build lines
                // for inclusion in the resylting CSV file.
                lastRowNum = sheet.getLastRowNum();
                for (int j = 0; j <= lastRowNum; j++) {
                    row = sheet.getRow(j);
                    rowToCSV(row, csvData);
                }
            }
        }
        return csvData;
    }

    private void transform(Workbook workbookOld, Workbook workbookNew, Sheet sheetOld, Sheet sheetNew) {
        System.out.println("transform Sheet");

        sheetNew.setDisplayFormulas(sheetOld.isDisplayFormulas());
        sheetNew.setDisplayGridlines(sheetOld.isDisplayGridlines());
        sheetNew.setDisplayGuts(sheetOld.getDisplayGuts());
        sheetNew.setDisplayRowColHeadings(sheetOld.isDisplayRowColHeadings());
        sheetNew.setDisplayZeros(sheetOld.isDisplayZeros());
        sheetNew.setFitToPage(sheetOld.getFitToPage());
        //
        //TODO::sheetNew.setForceFormulaRecalculation(sheetOld.getForceFormulaRecalculation());
        sheetNew.setHorizontallyCenter(sheetOld.getHorizontallyCenter());
        sheetNew.setMargin(Sheet.BottomMargin,
                sheetOld.getMargin(Sheet.BottomMargin));
        sheetNew.setMargin(Sheet.FooterMargin,
                sheetOld.getMargin(Sheet.FooterMargin));
        sheetNew.setMargin(Sheet.HeaderMargin,
                sheetOld.getMargin(Sheet.HeaderMargin));
        sheetNew.setMargin(Sheet.LeftMargin,
                sheetOld.getMargin(Sheet.LeftMargin));
        sheetNew.setMargin(Sheet.RightMargin,
                sheetOld.getMargin(Sheet.RightMargin));
        sheetNew.setMargin(Sheet.TopMargin, sheetOld.getMargin(Sheet.TopMargin));
        sheetNew.setPrintGridlines(sheetNew.isPrintGridlines());
        sheetNew.setRightToLeft(sheetNew.isRightToLeft());
        sheetNew.setRowSumsBelow(sheetNew.getRowSumsBelow());
        sheetNew.setRowSumsRight(sheetNew.getRowSumsRight());
        sheetNew.setVerticallyCenter(sheetOld.getVerticallyCenter());

        Row rowNew;
        for (Row row : sheetOld) {
            rowNew = sheetNew.createRow(row.getRowNum());
            if (rowNew != null) {
                this.transform(workbookOld, workbookNew, row, rowNew);
            }
        }

        for (int i = 0; i < this.lastColumn; i++) {
            sheetNew.setColumnWidth(i, sheetOld.getColumnWidth(i));
            sheetNew.setColumnHidden(i, sheetOld.isColumnHidden(i));
        }

        for (int i = 0; i < sheetOld.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheetOld.getMergedRegion(i);
            sheetNew.addMergedRegion(merged);
        }
    }

    private void transform(Workbook workbookOld, Workbook workbookNew, Row rowOld, Row rowNew) {
        Cell cellNew;
        rowNew.setHeight(rowOld.getHeight());
        for (Cell cell : rowOld) {
            cellNew = rowNew.createCell(cell.getColumnIndex(),
                    cell.getCellType());
            if (cellNew != null) {
                this.transform(workbookOld, workbookNew, cell, cellNew);
            }
        }
        this.lastColumn = Math.max(this.lastColumn, rowOld.getLastCellNum());
    }

    private void transform(Workbook workbookOld, Workbook workbookNew, Cell cellOld, Cell cellNew) {
        cellNew.setCellComment(cellOld.getCellComment());

        Integer hash = cellOld.getCellStyle().hashCode();
        if (this.styleMap != null && !this.styleMap.containsKey(hash)) {
            this.transform(workbookOld, workbookNew, hash, cellOld.getCellStyle(), workbookNew.createCellStyle());
        }
        cellNew.setCellStyle(this.styleMap.get(hash));

        switch (cellOld.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellNew.setCellValue(cellOld.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                cellNew.setCellErrorValue(cellOld.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellNew.setCellFormula(cellOld.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellNew.setCellValue(cellOld.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellNew.setCellValue(cellOld.getStringCellValue());
                break;
            default:
                System.out.println("transform: Unbekannter Zellentyp "
                        + cellOld.getCellType());
        }
    }

    private void transform(Workbook workbookOld, Workbook workbookNew, Integer hash, CellStyle styleOld,
            CellStyle styleNew) {
        styleNew.setAlignment(styleOld.getAlignment());
        styleNew.setBorderBottom(styleOld.getBorderBottom());
        styleNew.setBorderLeft(styleOld.getBorderLeft());
        styleNew.setBorderRight(styleOld.getBorderRight());
        styleNew.setBorderTop(styleOld.getBorderTop());
        styleNew.setDataFormat(this.transform(workbookOld, workbookNew, styleOld.getDataFormat()));
        styleNew.setFillBackgroundColor(styleOld.getFillBackgroundColor());
        styleNew.setFillForegroundColor(styleOld.getFillForegroundColor());
        styleNew.setFillPattern(styleOld.getFillPattern());
        if (workbookNew instanceof HSSFWorkbook) {
            styleNew.setFont(this.transform(workbookNew, ((XSSFCellStyle) styleOld).getFont()));
        } else if (workbookNew instanceof XSSFWorkbook) {
            styleNew.setFont(this.transform(workbookNew, ((HSSFCellStyle) styleOld).getFont(workbookOld)));
        }
        styleNew.setHidden(styleOld.getHidden());
        styleNew.setIndention(styleOld.getIndention());
        styleNew.setLocked(styleOld.getLocked());
        styleNew.setVerticalAlignment(styleOld.getVerticalAlignment());
        styleNew.setWrapText(styleOld.getWrapText());
        this.styleMap.put(hash, styleNew);
    }

    private short transform(Workbook workbookOld, Workbook workbookNew, short index) {
        DataFormat formatOld = workbookOld.createDataFormat();
        DataFormat formatNew = workbookNew.createDataFormat();
        return formatNew.getFormat(formatOld.getFormat(index));
    }

    private Font transform(Workbook workbookNew, Font fontOld) {
        Font fontNew = workbookNew.createFont();
        fontNew.setBoldweight(fontOld.getBoldweight());
        fontNew.setCharSet(fontOld.getCharSet());
        fontNew.setColor(fontOld.getColor());
        fontNew.setFontName(fontOld.getFontName());
        fontNew.setFontHeight(fontOld.getFontHeight());
        fontNew.setItalic(fontOld.getItalic());
        fontNew.setStrikeout(fontOld.getStrikeout());
        fontNew.setTypeOffset(fontOld.getTypeOffset());
        fontNew.setUnderline(fontOld.getUnderline());
        return fontNew;
    }

    /**
     * Called to convert a row of cells into a line of data that can later be
     * output to the CSV file.
     *
     * @param row An instance of either the HSSFRow or XSSFRow classes that
     * encapsulates information about a row of cells recovered from an Excel
     * workbook.
     */
    private void rowToCSV(Row row, List<List<String>> csvData) {
        Cell cell = null;
        int lastCellNum = 0;
        ArrayList<String> csvLine = new ArrayList<String>();

        // Check to ensure that a row was recovered from the sheet as it is
        // possible that one or more rows between other populated rows could be
        // missing - blank. If the row does contain cells then...
        if (row != null) {

            // Get the index for the right most cell on the row and then
            // step along the row from left to right recovering the contents
            // of each cell, converting that into a formatted String and
            // then storing the String into the csvLine ArrayList.
            lastCellNum = row.getLastCellNum();
            for (int i = 0; i <= lastCellNum; i++) {
                cell = row.getCell(i);
                if (cell == null) {
                    csvLine.add("");
                } else if (cell.getCellType() != Cell.CELL_TYPE_FORMULA) {
                    csvLine.add(this.formatter.formatCellValue(cell));
                } else {
                    FormulaEvaluator evaluator = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    csvLine.add(this.formatter.formatCellValue(cell, evaluator));
                }
            }
            // Make a note of the index number of the right most cell. This value
            // will later be used to ensure that the matrix of data in the CSV file
            // is square.
            if (lastCellNum > this.maxRowWidth) {
                this.maxRowWidth = lastCellNum;
            }
        }
        csvData.add(csvLine);
    }

    /**
     * Checks to see whether the field - which consists of the formatted
     * contents of an Excel worksheet cell encapsulated within a String -
     * contains any embedded characters that must be escaped. The method is able
     * to comply with either Excel's or UNIX formatting conventions in the
     * following manner;
     *
     * With regard to UNIX conventions, if the field contains any embedded field
     * separator or EOL characters they will each be escaped by prefixing a
     * leading backspace character. These are the only changes that have yet
     * emerged following some research as being required.
     *
     * Excel has other embedded character escaping requirements, some that
     * emerged from empirical testing, other through research. Firstly, with
     * regards to any embedded speech marks ("), each occurrence should be
     * escaped with another speech mark and the whole field then surrounded with
     * speech marks. Thus if a field holds <em>"Hello" he said</em> then it
     * should be modified to appear as <em>"""Hello"" he said"</em>.
     * Furthermore, if the field contains either embedded separator or EOL
     * characters, it should also be surrounded with speech marks. As a result
     * <em>1,400</em> would become
     * <em>"1,400"</em> assuming that the comma is the required field separator.
     * This has one consequence in, if a field contains embedded speech marks
     * and embedded separator characters, checks for both are not required as
     * the additional set of speech marks that should be placed around ay field
     * containing embedded speech marks will also account for the embedded
     * separator.
     *
     * It is worth making one further note with regard to embedded EOL
     * characters. If the data in a worksheet is exported as a CSV file using
     * Excel itself, then the field will be surounded with speech marks. If the
     * resulting CSV file is then re-imports into another worksheet, the EOL
     * character will result in the original simgle field occupying more than
     * one cell. This same 'feature' is replicated in this classes behaviour.
     *
     * @param field An instance of the String class encapsulating the formatted
     * contents of a cell on an Excel worksheet.
     * @return A String that encapsulates the formatted contents of that Excel
     * worksheet cell but with any embedded separator, EOL or speech mark
     * characters correctly escaped.
     */
    private String escapeEmbeddedCharacters(String field,
            int formattingConvention,
            String separator) {
        StringBuffer buffer = null;

        // If the fields contents should be formatted to confrom with Excel's
        // convention....
        if (formattingConvention == EXCEL_STYLE_ESCAPING) {

            // Firstly, check if there are any speech marks (") in the field;
            // each occurrence must be escaped with another set of spech marks
            // and then the entire field should be enclosed within another
            // set of speech marks. Thus, "Yes" he said would become
            // """Yes"" he said"
            if (field.contains("\"")) {
                buffer = new StringBuffer(field.replaceAll("\"", "\\\"\\\""));
                buffer.insert(0, "\"");
                buffer.append("\"");
            } else {
                // If the field contains either embedded separator or EOL
                // characters, then escape the whole field by surrounding it
                // with speech marks.
                buffer = new StringBuffer(field);
                if ((buffer.indexOf(separator)) > -1
                        || (buffer.indexOf("\n")) > -1) {
                    buffer.insert(0, "\"");
                    buffer.append("\"");
                }
            }
            return (buffer.toString().trim());
        } // The only other formatting convention this class obeys is the UNIX one
        // where any occurrence of the field separator or EOL character will
        // be escaped by preceding it with a backslash.
        else {
            if (field.contains(separator)) {
                field = field.replaceAll(separator, ("\\\\" + separator));
            }
            if (field.contains("\n")) {
                field = field.replaceAll("\n", "\\\\\n");
            }
            return (field);
        }
    }

    public void saveExcelStyleCSVFile(FileWriter fw,
            List<List<String>> csvData) throws IOException {
        saveCSVFile(fw, csvData, EXCEL_STYLE_ESCAPING);
    }

    public void saveUnixStyleCSVFile(FileWriter fw,
            List<List<String>> csvData) throws IOException {
        saveCSVFile(fw, csvData, UNIX_STYLE_ESCAPING);
    }

    public void saveCSVFile(FileWriter fw,
            List<List<String>> csvData,
            int formattingConvention) throws IOException {
        BufferedWriter bw = null;
        List<String> line;
        StringBuffer buffer;
        String csvLineElement;
        try {
            // System.out.println("Saving the CSV file [" + file.getName() + "]");
            // Open a writer onto the CSV file.
            //fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            // Step through the elements of the ArrayList that was used to hold
            // all of the data recovered from the Excel workbooks' sheets, rows
            // and cells.
            for (int i = 0; i < csvData.size(); i++) {
                buffer = new StringBuffer();

                // Get an element from the ArrayList that contains the data for
                // the workbook. This element will itself be an ArrayList
                // containing Strings and each String will hold the data recovered
                // from a single cell. The for() loop is used to recover elements
                // from this 'row' ArrayList one at a time and to write the Strings
                // away to a StringBuffer thus assembling a single line for inclusion
                // in the CSV file. If a row was empty or if it was short, then
                // the ArrayList that contains it's data will also be shorter than
                // some of the others. Therefore, it is necessary to check within
                // the for loop to ensure that the ArrayList contains data to be
                // processed. If it does, then an element will be recovered and
                // appended to the StringBuffer.
                line = csvData.get(i);
                for (int j = 0; j < maxRowWidth; j++) {
                    if (line.size() > j) {
                        csvLineElement = line.get(j);
                        if (csvLineElement != null) {
                            buffer.append(escapeEmbeddedCharacters(
                                    csvLineElement,
                                    formattingConvention,
                                    DEFAULT_SEPARATOR));
                        }
                    }
                    if (j < (maxRowWidth - 1)) {
                        buffer.append(DEFAULT_SEPARATOR);
                    }
                }

                // Once the line is built, write it away to the CSV file.
                bw.write(buffer.toString().trim());

                // Condition the inclusion of new line characters so as to
                // avoid an additional, superfluous, new line at the end of
                // the file.
                if (i < (csvData.size() - 1)) {
                    bw.newLine();
                }
            }
        } finally {
            if (bw != null) {
                bw.flush();
                bw.close();
            }
        }
    }
}
