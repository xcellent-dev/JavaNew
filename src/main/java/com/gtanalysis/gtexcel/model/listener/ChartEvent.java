/*
 */
package com.gtanalysis.gtexcel.model.listener;

import com.gtanalysis.gtexcel.model.ExcelChartModel;

/**
 *
 * @author nkabiliravi
 */
public class ChartEvent {
    
    private final Object source;
    
    private final ExcelChartModel excelChartModel;

    public ChartEvent(Object source, ExcelChartModel excelChartModel) {
        this.source = source;
        this.excelChartModel = excelChartModel;
    }

    public Object getSource() {
        return source;
    }

    public ExcelChartModel getExcelChartModel() {
        return excelChartModel;
    }
    
    
}
