/*
 */
package com.gtanalysis.gtexcel.util;

import com.gtanalysis.gtexcel.model.ExcelCellRange;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author nkabiliravi
 */
public class RangeUtil {

    private static final Logger LOG = Logger.getLogger(RangeUtil.class.getName());

    public static final String RANGES_REGEX = "\\$(?<sheetName>\\w+[\\s\\w]*){1}\\.(\\$(?<fromColumn>[A-Za-z]+)\\$(?<fromRow>[0-9]+)){1}\\:{0,1}(\\$(?<toColumn>[A-Za-z]+)\\$(?<toRow>[0-9]+)){0,1}";
    public static final Pattern RANGES_PATTERN = Pattern.compile(RANGES_REGEX);

    public static ExcelCellRange parseCellRange(String rangeString) {
        Matcher macher = RANGES_PATTERN.matcher(rangeString);
        macher.find();
        String sheetName = macher.group("sheetName");
        String fromColumn = macher.group("fromColumn");
        String fromRow = macher.group("fromRow");
        String toColumn = macher.group("toColumn");
        String toRow = macher.group("toRow");

        return new ExcelCellRange(sheetName,
                fromColumn,
                fromRow != null ? Integer.parseInt(fromRow) : null,
                toColumn,
                toRow != null ? Integer.parseInt(toRow) : null);
    }

    public static List<ExcelCellRange> parseCellRanges(String rangesString) {
        List<ExcelCellRange> excelCellRanges = new ArrayList<>();
        if (StringUtils.isNotEmpty(rangesString)) {
            try {
                String[] rangesStringArr = rangesString.split(";");
                for (String rangeString : rangesStringArr) {
                    excelCellRanges.add(parseCellRange(rangeString));
                }
            } catch (IllegalStateException ex) {
                LOG.log(Level.WARNING, "'{0}' is not valid range", rangesString);
            }
        }
        return excelCellRanges;
    }

}
