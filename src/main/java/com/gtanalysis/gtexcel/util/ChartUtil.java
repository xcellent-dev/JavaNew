/*
 */
package com.gtanalysis.gtexcel.util;

import com.gtanalysis.gtexcel.model.ExcelChartModel;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;

/**
 *
 * @author nkabiliravi
 */
public class ChartUtil {

    public static ExcelChartModel extractExcelChartModel(Plot plot) {
        if (plot instanceof PiePlot) {
            return (ExcelChartModel) ((PiePlot) plot).getDataset();
        } else if (plot instanceof CategoryPlot) {
            return (ExcelChartModel) ((CategoryPlot) plot).getDataset();
        } else if (plot instanceof XYPlot) {
            return (ExcelChartModel) ((XYPlot) plot).getDataset();
        }
        throw new RuntimeException("This chart does not assigned to a " + ExcelChartModel.class.getName());
    }

    public static boolean validateChartMetaDatatRow(Row row) {
        if (row != null && row.getPhysicalNumberOfCells() > 3) {
            boolean result = true;
            for (int i = 0; i < 4; i++) {
                result &= StringUtils.isNotEmpty(WorkbookUtil.getCellStringValue(row.getCell(i)));
            }
            return result;
        }
        return false;
    }
}
