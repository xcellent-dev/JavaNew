/*
 */
package com.gtanalysis.gtexcel.model;

import java.net.URL;

/**
 *
 * @author nkabiliravi
 */
public enum ExcelChartType {

    PIE("Pie", ExcelChartType.class.getResource("/icon/32x32/office-chart-pie.png")), 
    COLUMN("Column", ExcelChartType.class.getResource("/icon/32x32/office-chart-column.png")), 
    BAR("Bar", ExcelChartType.class.getResource("/icon/32x32/office-chart-bar.png")), 
    LINE("Line", ExcelChartType.class.getResource("/icon/32x32/office-chart-line.png")), 
    AREA("Area", ExcelChartType.class.getResource("/icon/32x32/office-chart-area.png")),
    XYLINE("XYLine", ExcelChartType.class.getResource("/icon/32x32/office-chart-line-stacked.png")), 
    XYAREA("XYArea", ExcelChartType.class.getResource("/icon/32x32/office-chart-area-stacked.png"));

    private final String value;
    
    private final URL iconUrl;

    private ExcelChartType(String value, URL iconUrl) {
        this.value = value;
        this.iconUrl = iconUrl;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public String getValue() {
        return value;
    }

}
