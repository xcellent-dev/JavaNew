/*
 */
package com.gtanalysis.gtexcel.ui;

import com.gtanalysis.gtexcel.model.ExcelChartType;
import com.gtanalysis.gtexcel.util.TextUtil;
import javax.swing.JRadioButton;

/**
 *
 * @author nkabiliravi
 */
public class ChartRadioButton extends JRadioButton {

    private static final String HTML_TEXT = TextUtil.loadFromClassPath("/iconedtext.html");
    private final ExcelChartType chartType;

    public ChartRadioButton(ExcelChartType chartType) {
        this.chartType = chartType;
        init();
    }

    public ExcelChartType getChartType() {
        return chartType;
    }

    private void init() {
        setText(String.format(HTML_TEXT, 
                this.chartType.getIconUrl(), 
                this.chartType.getValue()));
    }

}
