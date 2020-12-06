/*
 */
package com.gtanalysis.gtexcel.util;

import com.gtanalysis.gtexcel.model.ExcelCellRange;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author nkabiliravi
 */
public class RangeUtilTest {

    @Test
    public void testSingleCell_parseCellRange() {
        String rangeString = "$Sheet Test_1.$BA$12";
        ExcelCellRange excelCellRange = RangeUtil.parseCellRange(rangeString);
        assertEquals("Sheet Test_1", excelCellRange.getSheetName());
        assertEquals("BA", excelCellRange.getFromColumn());
        assertEquals(12, excelCellRange.getFromRow().intValue());
        assertNull(excelCellRange.getToColumn());
        assertNull(excelCellRange.getToRow());
    }

    @Test
    public void testSingleCell_parseCellRanges() {
        String rangeString = "$Sheet Test_1.$BA$12";
        List<ExcelCellRange> list = RangeUtil.parseCellRanges(rangeString);
        assertNotNull(list);
        assertTrue(list.size() == 1);
        ExcelCellRange excelCellRange = list.get(0);
        assertEquals("Sheet Test_1", excelCellRange.getSheetName());
        assertEquals("BA", excelCellRange.getFromColumn());
        assertEquals(12, excelCellRange.getFromRow().intValue());
        assertNull(excelCellRange.getToColumn());
        assertNull(excelCellRange.getToRow());
    }

    @Test
    public void testSingleRange_parseCellRanges() {
        String rangeString = "$Sheet 1.$A$1:$E$3";
        List<ExcelCellRange> list = RangeUtil.parseCellRanges(rangeString);
        assertNotNull(list);
        assertTrue(list.size() == 1);
        ExcelCellRange excelCellRange = list.get(0);
        assertEquals("Sheet 1", excelCellRange.getSheetName());
        assertEquals("A", excelCellRange.getFromColumn());
        assertEquals(1, excelCellRange.getFromRow().intValue());
        assertEquals("E", excelCellRange.getToColumn());
        assertEquals(3, excelCellRange.getToRow().intValue());
    }

    @Test
    public void testMutltipleRanges_parseCellRanges() {
        String rangeString = "$Sheet 1.$A$1:$E$3;$Sheet Test 2.$XW$21:$EQ$100";
        List<ExcelCellRange> list = RangeUtil.parseCellRanges(rangeString);
        assertNotNull(list);
        assertTrue(list.size() == 2);
        ExcelCellRange excelCellRange = list.get(0);
        assertEquals("Sheet 1", excelCellRange.getSheetName());
        assertEquals("A", excelCellRange.getFromColumn());
        assertEquals(1, excelCellRange.getFromRow().intValue());
        assertEquals("E", excelCellRange.getToColumn());
        assertEquals(3, excelCellRange.getToRow().intValue());

        excelCellRange = list.get(1);
        assertEquals("Sheet Test 2", excelCellRange.getSheetName());
        assertEquals("XW", excelCellRange.getFromColumn());
        assertEquals(21, excelCellRange.getFromRow().intValue());
        assertEquals("EQ", excelCellRange.getToColumn());
        assertEquals(100, excelCellRange.getToRow().intValue());
    }
    
    @Test
    public void testGeneratedRange() {
        ExcelCellRange excelCellRange = new ExcelCellRange("Sheet 1", 
                "F", 1, "F", 5);
        assertEquals("$Sheet 1.$F$1:$F$5", excelCellRange.toString());
        assertTrue(excelCellRange.toString().matches(RangeUtil.RANGES_REGEX));
    }
}
