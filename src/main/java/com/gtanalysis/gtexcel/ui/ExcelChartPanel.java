/*
 */
package com.gtanalysis.gtexcel.ui;

import com.gtanalysis.gtexcel.model.ExcelCellRange;
import com.gtanalysis.gtexcel.model.ExcelChartModel;
import com.gtanalysis.gtexcel.model.ExcelChartType;
import com.gtanalysis.gtexcel.model.listener.ChartEvent;
import com.gtanalysis.gtexcel.model.listener.ChartListener;
import com.gtanalysis.gtexcel.util.ChartUtil;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author nkabiliravi
 */
public class ExcelChartPanel extends javax.swing.JPanel {

    private final WorkbookPanel workbookPanel;
    private ChartPanel selectedChartPanel = null;
    private List<ChartListener> chartListeners = new ArrayList<>();

    /**
     * Creates new form ChartPanel
     *
     * @param workbookPanel
     */
    public ExcelChartPanel(WorkbookPanel workbookPanel) {
        this.workbookPanel = workbookPanel;
        initComponents();
    }

    public void addChartListener(ChartListener listener) {
        chartListeners.add(listener);
    }

    public void removeChartListener(ChartListener listener) {
        chartListeners.remove(listener);
    }

    public void createChart(String chartTitle,
            ExcelChartType chartType,
            String containerSheetName,
            ExcelCellRange xLabelRange,
            ExcelCellRange yLabelRange,
            List<ExcelCellRange> xDataRanges,
            List<ExcelCellRange> yDataRanges) {
        ExcelChartModel chartModel = workbookPanel.getWorkbookModel().addChartModel(chartTitle,
                chartType,
                containerSheetName,
                xLabelRange,
                yLabelRange,
                xDataRanges,
                yDataRanges);
        ChartPanel chartPanel = createChartPanel(chartModel);
        add(chartPanel);
    }

    private JFreeChart createChart(ExcelChartModel chartModel) {
        switch (chartModel.getChartType()) {
            case PIE:
                return ChartFactory.createPieChart(chartModel.getName(), chartModel);
            case COLUMN:
                return ChartFactory.createBarChart(chartModel.getName(),
                        "Category",
                        "Value",
                        chartModel);
            case BAR:
                return ChartFactory.createBarChart(chartModel.getName(),
                        "Category",
                        "Value",
                        chartModel,
                        PlotOrientation.HORIZONTAL,
                        true,
                        true,
                        false);
            case LINE:
                return ChartFactory.createLineChart(chartModel.getName(),
                        "Category",
                        "Value",
                        chartModel);
            case AREA:
                return ChartFactory.createAreaChart(chartModel.getName(),
                        "Category",
                        "Value",
                        chartModel);
            case XYLINE:
                return ChartFactory.createXYLineChart(chartModel.getName(),
                        "Category",
                        "Value",
                        chartModel);
            case XYAREA:
                return ChartFactory.createXYAreaChart(chartModel.getName(),
                        "Category",
                        "Value",
                        chartModel);
            default:
                throw new RuntimeException("The chart type is not supported");
        }
    }

    public void editChart(String chartTitle,
            ExcelChartType chartType,
            ExcelCellRange xLabelRange,
            ExcelCellRange yLabelRange,
            List<ExcelCellRange> xDataRanges,
            List<ExcelCellRange> yDataRanges) {
        getSelectedChartModel().setName(chartTitle);
        getSelectedChartModel().setChartType(chartType);
        getSelectedChartModel().setXCategoryRangeValue(xLabelRange.toString());
        getSelectedChartModel().setYCategoryRangeValue(yLabelRange.toString());
        getSelectedChartModel().setXDataRages(xDataRanges);
        getSelectedChartModel().setYDataRages(yDataRanges);
        JFreeChart chart = createChart(getSelectedChartModel());
        selectedChartPanel.setChart(chart);
    }

    private ChartPanel createChartPanel(ExcelChartModel chartModel) {
        JFreeChart chart = createChart(chartModel);
        final ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) {
                return super.print(g, pf, 0);
            }
            
        };

        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                resetSelection();
                selectedChartPanel = chartPanel;
                selectedChartPanel.getChart().setBackgroundPaint(ChartColor.LIGHT_CYAN);
                selectedChartPanel.getChart().setBorderVisible(true);
                selectedChartPanel.updateUI();
                fireChartSelected();
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
            }
        });
        return chartPanel;
    }

    public void loadCharts(String containerSheetName) {
        List<ExcelChartModel> chartModels = workbookPanel.getWorkbookModel().getChartModels();
        if (chartModels.size() > 0) {
            chartModels.stream().filter((chartModel)
                    -> (containerSheetName.equals(chartModel.getContainerSheetName()))).map((chartModel)
                    -> createChartPanel(chartModel)).forEach((chartPanel)
                    -> {
                add(chartPanel);
            });
        }
    }

    private void resetSelection() {
        for (int i = 0; i < getComponentCount(); i++) {
            ChartPanel chartPanel = (ChartPanel) getComponent(i);
            chartPanel.setBackground(this.getBackground());
            chartPanel.getChart().setBorderVisible(false);
            chartPanel.getChart().setBackgroundPaint(null);
            chartPanel.updateUI();
        }
    }

    public boolean isSelected() {
        return selectedChartPanel != null;
    }

    public ChartPanel getSelectedChartPanel() {
        return selectedChartPanel;
    }

    public ExcelChartModel getSelectedChartModel() {
        if (selectedChartPanel != null) {
            Plot plot = selectedChartPanel.getChart().getPlot();
            return ChartUtil.extractExcelChartModel(plot);
        }
        return null;
    }

    public String getSelectedChartTitle() {
        return this.selectedChartPanel != null ? this.selectedChartPanel.getChart().getTitle().getText() : null;
    }

    public void removeSelectedChart() {
        ExcelChartModel excelChartModel = getSelectedChartModel();
        if (excelChartModel != null) {
            this.workbookPanel.getWorkbookModel().removeChartModel(excelChartModel);
            remove(selectedChartPanel);
            updateUI();
        }
    }

    private void fireChartSelected() {
        for (ChartListener chartListener : chartListeners) {
            chartListener.chartSelected(new ChartEvent(this, this.getSelectedChartModel()));
        }
    }
    
    public List<Printable> getPrintableChartPanels() {
        List<Printable> printables = new ArrayList<>();
        for(Component component : getComponents()) {
            printables.add((ChartPanel) component);
        }
        return printables;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAutoscrolls(true);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
