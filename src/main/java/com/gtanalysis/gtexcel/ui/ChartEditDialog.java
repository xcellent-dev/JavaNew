/*
 */
package com.gtanalysis.gtexcel.ui;

import com.gtanalysis.gtexcel.model.ExcelCellRange;
import com.gtanalysis.gtexcel.model.ExcelChartModel;
import com.gtanalysis.gtexcel.model.ExcelChartType;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author nkabiliravi
 */
public class ChartEditDialog extends javax.swing.JDialog {

    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private final SheetPanel sheetPanel;

    private ExcelChartModel excelChartModel;
    private RangePanel xRangePanel;
    private RangePanel yRangePanel;
    private DataRangePickerPanel xlabelRangePanel;
    private DataRangePickerPanel ylabelRangePanel;
    private final boolean editChart;

    /**
     * Creates new form ChartEditDialog
     *
     * @param sheetPanel
     * @param modal
     * @param editChart
     */
    public ChartEditDialog(SheetPanel sheetPanel, boolean modal, boolean editChart) {
        super((Frame) null, modal);
        this.editChart = editChart;
        this.sheetPanel = sheetPanel;
        initComponents();
        initCustom();
    }

    private void initXLabelRangePanel() {

        xlabelRangePanel = new DataRangePickerPanel("X Labels", sheetPanel, editChart);

        xlabelRangePanel.setDeleteButtonVisible(false);

        if (editChart) {
            ExcelCellRange xCategoryCellRange = this.sheetPanel.getSelectedChartModel().getXCategoryRange();
            xlabelRangePanel.setExcelCellRange(xCategoryCellRange);
        }

    }

    private void initYLabelRangePanel() {

        ylabelRangePanel = new DataRangePickerPanel("Y Labels", sheetPanel, editChart);

        ylabelRangePanel.setDeleteButtonVisible(false);

        if (editChart) {
            ExcelCellRange xCategoryCellRange = this.sheetPanel.getSelectedChartModel().getYCategoryRange();
            ylabelRangePanel.setExcelCellRange(xCategoryCellRange);
        }
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public ExcelChartModel getExcelChartModel() {
        return excelChartModel;
    }

    public String getChartTitle() {
        return chartTitleTextField.getText();
    }

    public ExcelChartType getChartType() {
        ExcelChartType chartType = null;
        Enumeration<AbstractButton> elements = chartButtonGroup.getElements();
        while (elements.hasMoreElements()) {
            ChartRadioButton button = (ChartRadioButton) elements.nextElement();
            if (button.isSelected()) {
                chartType = button.getChartType();
                break;
            }
        }
        return chartType;
    }

    private void initCustom() {
        if (editChart) {
            chartTitleTextField.setText(this.sheetPanel.getSelectedChartModel().getName());
        }
        initChartRadioGroupPanel();
        initLabelRangePanels();
        initRangePanels();
        initSelectedComponents();
    }

    private ChartPanel getSelectedChartPanel() {
        return sheetPanel.getSelectedChartPanel();
    }

    private ExcelChartModel getSelectedChartModel() {
        return sheetPanel.getSelectedChartModel();
    }

    private void initChartRadioGroupPanel() {
        for (ExcelChartType chartType : ExcelChartType.values()) {
            createChartRadioButton(chartType);
        }
    }

    private void initSelectedComponents() {
        if (editChart) {
            Enumeration<AbstractButton> radioButtons = chartButtonGroup.getElements();
            while (radioButtons.hasMoreElements()) {
                ChartRadioButton radioButton = (ChartRadioButton) radioButtons.nextElement();
                ExcelChartType chartType = getSelectedChartModel().getChartType();
                if (radioButton.getChartType().equals(chartType)) {
                    radioButton.setSelected(true);
                    break;
                }
            }
        } else {
            AbstractButton radioButton = chartButtonGroup.getElements().nextElement();
            chartButtonGroup.setSelected(radioButton.getModel(), true);
        }
    }

    private void initLabelRangePanels() {
        initXLabelRangePanel();
        initYLabelRangePanel();
    }

    private void initRangePanels() {
        xRangePanel = new RangePanel("X Ranges", this.sheetPanel, editChart);
        yRangePanel = new RangePanel("Y Ranges", this.sheetPanel, editChart);
        if (editChart) {
            List<ExcelCellRange> xDataRanges = this.sheetPanel.getSelectedChartModel().getXDataRanges();
            xRangePanel.setDataRanges(xDataRanges);
        }
    }

    private void createChartRadioButton(ExcelChartType chartType) {
        ChartRadioButton chartRadioButton = new ChartRadioButton(chartType);
        chartRadioButton.addItemListener((ItemEvent e) -> {
            updatePanels((ChartRadioButton) e.getSource());
        });
        chartButtonGroup.add(chartRadioButton);
        chartRadioGroupPanel.add(chartRadioButton);
    }

    private void updatePanels(ChartRadioButton chartRadioButton) {
        switch (chartRadioButton.getChartType()) {
            case PIE:
                showPieLabelRangePanels();
                showPieRangePanels();
                break;
            case BAR:
            case COLUMN:
            case LINE:
            case AREA:
                showTwoCategorizedLabelRangePanels();
                showTwoCategorizedRangePanels();
                break;
            case XYLINE:
            case XYAREA:
                showXYLabelRangePanels();
                showXYRangePanels();
                break;
            default:
                showDefaultLabelRangePanels();
                showDefaultRangePanels();
        }
    }

    private void showPieRangePanels() {
        rightPanel.removeAll();
        rightPanel.add(xRangePanel);
//        if (!editChart) {
//            int[] selectedColumns = this.sheetPanel.getExcelTable().getSelectedColumns();
//            int[] selectedRows = this.sheetPanel.getExcelTable().getSelectedRows();
//            xRangePanel.setDataRanges(dataRanges);
//        }
        rightPanel.updateUI();
    }

    private void showPieLabelRangePanels() {
        labelRangesPanel.removeAll();
        labelRangesPanel.add(xlabelRangePanel);
        labelRangesPanel.updateUI();
    }

    private void showTwoCategorizedLabelRangePanels() {
        labelRangesPanel.removeAll();
        labelRangesPanel.add(xlabelRangePanel);
        labelRangesPanel.add(ylabelRangePanel);
        labelRangesPanel.updateUI();
    }

    private void showTwoCategorizedRangePanels() {
        rightPanel.removeAll();
        rightPanel.add(xRangePanel);
        rightPanel.updateUI();
    }

    private void showXYLabelRangePanels() {
        labelRangesPanel.removeAll();
        labelRangesPanel.add(xlabelRangePanel);
        labelRangesPanel.updateUI();
    }

    private void showXYRangePanels() {
        rightPanel.removeAll();
        rightPanel.add(xRangePanel);
        rightPanel.add(yRangePanel);
        rightPanel.updateUI();
    }

    private void showDefaultLabelRangePanels() {
        labelRangesPanel.removeAll();
        labelRangesPanel.updateUI();
    }

    private void showDefaultRangePanels() {
        rightPanel.removeAll();
        rightPanel.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        chartButtonGroup = new javax.swing.ButtonGroup();
        chartTitleLabel = new javax.swing.JLabel();
        chartTitleTextField = new javax.swing.JTextField();
        leftPanel = new javax.swing.JPanel();
        chartTypePanel = new javax.swing.JPanel();
        selectChartLabel = new javax.swing.JLabel();
        chartRadioGroupPanel = new javax.swing.JPanel();
        labelRangesPanel = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        actionsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        buttonsFillerPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 400));
        setPreferredSize(new java.awt.Dimension(620, 420));
        setSize(new java.awt.Dimension(620, 420));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        chartTitleLabel.setText("Chart Title:");
        chartTitleLabel.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        getContentPane().add(chartTitleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        getContentPane().add(chartTitleTextField, gridBagConstraints);

        leftPanel.setLayout(new java.awt.GridBagLayout());

        chartTypePanel.setLayout(new java.awt.GridBagLayout());

        selectChartLabel.setText("Chart Type:");
        selectChartLabel.setMinimumSize(new java.awt.Dimension(216, 40));
        selectChartLabel.setPreferredSize(new java.awt.Dimension(216, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        chartTypePanel.add(selectChartLabel, gridBagConstraints);

        chartRadioGroupPanel.setAutoscrolls(true);
        chartRadioGroupPanel.setMinimumSize(new java.awt.Dimension(200, 60));
        chartRadioGroupPanel.setPreferredSize(new java.awt.Dimension(200, 60));
        chartRadioGroupPanel.setLayout(new java.awt.GridLayout(4, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        chartTypePanel.add(chartRadioGroupPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        leftPanel.add(chartTypePanel, gridBagConstraints);

        labelRangesPanel.setLayout(new javax.swing.BoxLayout(labelRangesPanel, javax.swing.BoxLayout.PAGE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        leftPanel.add(labelRangesPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(leftPanel, gridBagConstraints);

        rightPanel.setLayout(new javax.swing.BoxLayout(rightPanel, javax.swing.BoxLayout.PAGE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(rightPanel, gridBagConstraints);

        actionsPanel.setMinimumSize(new java.awt.Dimension(0, 50));
        actionsPanel.setPreferredSize(new java.awt.Dimension(0, 50));
        actionsPanel.setLayout(new java.awt.GridBagLayout());

        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        actionsPanel.add(cancelButton, gridBagConstraints);

        javax.swing.GroupLayout buttonsFillerPanelLayout = new javax.swing.GroupLayout(buttonsFillerPanel);
        buttonsFillerPanel.setLayout(buttonsFillerPanelLayout);
        buttonsFillerPanelLayout.setHorizontalGroup(
            buttonsFillerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );
        buttonsFillerPanelLayout.setVerticalGroup(
            buttonsFillerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        actionsPanel.add(buttonsFillerPanel, gridBagConstraints);

        okButton.setMnemonic('K');
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        actionsPanel.add(okButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(actionsPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dialogResult = JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {
            validateData();
            if (editChart) {
                sheetPanel.editChart(getChartTitle(),
                        getChartType(),
                        xlabelRangePanel.getExcelCellRange(),
                        ylabelRangePanel.getExcelCellRange(),
                        xRangePanel.getDataRanges(),
                        yRangePanel.getDataRanges());
            } else {
                sheetPanel.createChart(getChartTitle(),
                        getChartType(),
                        xlabelRangePanel.getExcelCellRange(),
                        ylabelRangePanel.getExcelCellRange(),
                        xRangePanel.getDataRanges(),
                        yRangePanel.getDataRanges());
            }
            this.dialogResult = JOptionPane.OK_OPTION;
            this.setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void validateData() throws RuntimeException {
        if (StringUtils.isEmpty(getChartTitle())) {
            throw new RuntimeException("Chart title is empty");
        }
        if (getChartType() == null) {
            throw new RuntimeException("Chart type is not selected");
        }
        xlabelRangePanel.validateRange();
        ylabelRangePanel.validateRange();
        xRangePanel.validateRanges();
        yRangePanel.validateRanges();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JPanel buttonsFillerPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.ButtonGroup chartButtonGroup;
    private javax.swing.JPanel chartRadioGroupPanel;
    private javax.swing.JLabel chartTitleLabel;
    private javax.swing.JTextField chartTitleTextField;
    private javax.swing.JPanel chartTypePanel;
    private javax.swing.JPanel labelRangesPanel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JLabel selectChartLabel;
    // End of variables declaration//GEN-END:variables

}
