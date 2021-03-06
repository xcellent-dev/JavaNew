/*
 */
package com.gtanalysis.gtexcel.ui;

import com.gtanalysis.gtexcel.jni.NativeFunction;
import com.gtanalysis.gtexcel.model.FunctionDialogModel;
import com.gtanalysis.gtexcel.model.FunctionSetType;
import com.gtanalysis.gtexcel.model.NativeListModel;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 *
 * @author nkabiliravi
 */
public class FunctionDialog extends javax.swing.JDialog {

    private final FunctionDialogModel functionDialogModel = new FunctionDialogModel();

    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    /**
     * Creates new form FunctionDialog
     *
     * @param expressionInitialValue
     * @param modal
     */
    public FunctionDialog(String expressionInitialValue, boolean modal) {
        super((Frame) null, modal);
        initComponents();
        initCustom(expressionInitialValue);
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public String getExpression() {
        return this.expressionTextArea.getText();
    }

    public void setExpression(String expressionText) {
        this.expressionTextArea.setText(expressionText);
    }

    private void initCustom(String expressionInitialValue) {
        this.builtinToggleButton.setSelected(true);
        this.functionDialogModel.setFunctionSetType(FunctionSetType.COMMON);
        this.categoryComboBox.setModel(functionDialogModel.getCategoryListModel());
        this.functionsList.setModel(functionDialogModel.getCurrentListModel());
        setExpression(expressionInitialValue);
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

        functionSetButtonGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        functionListPanel = new javax.swing.JPanel();
        functionsToolBarPanel = new javax.swing.JPanel();
        builtinToggleButton = new javax.swing.JToggleButton();
        nativeToggleButton = new javax.swing.JToggleButton();
        categoryLabel = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        functionsScrollPane = new javax.swing.JScrollPane();
        functionsList = new javax.swing.JList<>();
        expressionEditorPanel = new javax.swing.JPanel();
        descriptionTitleLabel = new javax.swing.JLabel();
        descriptionValueLabel = new javax.swing.JLabel();
        expressionLabel = new javax.swing.JLabel();
        expressionScrollPane = new javax.swing.JScrollPane();
        expressionTextArea = new javax.swing.JTextArea();
        buttonsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        buttonsFillerPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        mainPanel.setLayout(new java.awt.GridBagLayout());

        functionListPanel.setLayout(new java.awt.GridBagLayout());

        functionsToolBarPanel.setMinimumSize(new java.awt.Dimension(0, 40));
        functionsToolBarPanel.setPreferredSize(new java.awt.Dimension(0, 40));
        functionsToolBarPanel.setLayout(new java.awt.GridBagLayout());

        functionSetButtonGroup.add(builtinToggleButton);
        builtinToggleButton.setText("Built-in");
        builtinToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                builtinToggleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        functionsToolBarPanel.add(builtinToggleButton, gridBagConstraints);

        functionSetButtonGroup.add(nativeToggleButton);
        nativeToggleButton.setText("Native");
        nativeToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nativeToggleButtonActionPerformed(evt);
            }
        });
        functionsToolBarPanel.add(nativeToggleButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        functionListPanel.add(functionsToolBarPanel, gridBagConstraints);

        categoryLabel.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        functionListPanel.add(categoryLabel, gridBagConstraints);

        categoryComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                categoryComboBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        functionListPanel.add(categoryComboBox, gridBagConstraints);

        functionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                functionsListMouseClicked(evt);
            }
        });
        functionsList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                functionsListKeyReleased(evt);
            }
        });
        functionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                functionsListValueChanged(evt);
            }
        });
        functionsScrollPane.setViewportView(functionsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        functionListPanel.add(functionsScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        mainPanel.add(functionListPanel, gridBagConstraints);

        expressionEditorPanel.setLayout(new java.awt.GridBagLayout());

        descriptionTitleLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        descriptionTitleLabel.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        expressionEditorPanel.add(descriptionTitleLabel, gridBagConstraints);

        descriptionValueLabel.setText("<none>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        expressionEditorPanel.add(descriptionValueLabel, gridBagConstraints);

        expressionLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        expressionLabel.setText("Expression:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        expressionEditorPanel.add(expressionLabel, gridBagConstraints);

        expressionTextArea.setColumns(20);
        expressionTextArea.setRows(5);
        expressionScrollPane.setViewportView(expressionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 221;
        gridBagConstraints.ipady = 61;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        expressionEditorPanel.add(expressionScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        mainPanel.add(expressionEditorPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(mainPanel, gridBagConstraints);

        buttonsPanel.setMinimumSize(new java.awt.Dimension(0, 50));
        buttonsPanel.setPreferredSize(new java.awt.Dimension(0, 50));
        buttonsPanel.setLayout(new java.awt.GridBagLayout());

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
        buttonsPanel.add(cancelButton, gridBagConstraints);

        javax.swing.GroupLayout buttonsFillerPanelLayout = new javax.swing.GroupLayout(buttonsFillerPanel);
        buttonsFillerPanel.setLayout(buttonsFillerPanelLayout);
        buttonsFillerPanelLayout.setHorizontalGroup(
            buttonsFillerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
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
        buttonsPanel.add(buttonsFillerPanel, gridBagConstraints);

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
        buttonsPanel.add(okButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(buttonsPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void builtinToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_builtinToggleButtonActionPerformed
        toggleFunctionSet(FunctionSetType.COMMON);
    }//GEN-LAST:event_builtinToggleButtonActionPerformed

    private void nativeToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nativeToggleButtonActionPerformed
        toggleFunctionSet(FunctionSetType.NATIVE);
    }//GEN-LAST:event_nativeToggleButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dialogResult = JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.dialogResult = JOptionPane.OK_OPTION;
        this.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void categoryComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_categoryComboBoxItemStateChanged
        functionDialogModel.initListModel((String) evt.getItem());
        this.functionsList.setModel(functionDialogModel.getCurrentListModel());
    }//GEN-LAST:event_categoryComboBoxItemStateChanged

    private void functionsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_functionsListMouseClicked
        if (evt.getClickCount() == 2) {
            addFunctionText();
        }
    }//GEN-LAST:event_functionsListMouseClicked

    private void functionsListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_functionsListKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addFunctionText();
        }
    }//GEN-LAST:event_functionsListKeyReleased

    private void functionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_functionsListValueChanged
        this.descriptionValueLabel.setText("<none>");
        if (evt.getFirstIndex() > -1) {
            ListModel listModel = this.functionsList.getModel();
            if (listModel instanceof NativeListModel) {
                NativeFunction nativeFunction = ((NativeListModel) listModel).getElementAt(evt.getFirstIndex());
                this.descriptionValueLabel.setText(nativeFunction.getDescription());
            }
        }
    }//GEN-LAST:event_functionsListValueChanged

    private void addFunctionText() {
        Object selectedFunction = functionsList.getSelectedValue();
        String selectedFunctionName = "";
        if (selectedFunction instanceof String) {
            selectedFunctionName = (String) selectedFunction;
        } else if(selectedFunction instanceof NativeFunction) {
            selectedFunctionName = ((NativeFunction) selectedFunction).getName();
        }
        if (selectedFunctionName != null) {
            selectedFunctionName = selectedFunctionName + "(" + ")";
            String expr = this.expressionTextArea.getText();
            expr = (expr != null && !expr.trim().equals("")) ? (expr + "+" + selectedFunctionName) : selectedFunctionName;
            this.expressionTextArea.setText(expr);
        }
    }

    private void toggleFunctionSet(FunctionSetType functionSetType) {
        this.functionDialogModel.setFunctionSetType(functionSetType);
        this.categoryComboBox.setModel(functionDialogModel.getCategoryListModel());
        this.functionsList.setModel(functionDialogModel.getCurrentListModel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton builtinToggleButton;
    private javax.swing.JPanel buttonsFillerPanel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JLabel descriptionTitleLabel;
    private javax.swing.JLabel descriptionValueLabel;
    private javax.swing.JPanel expressionEditorPanel;
    private javax.swing.JLabel expressionLabel;
    private javax.swing.JScrollPane expressionScrollPane;
    private javax.swing.JTextArea expressionTextArea;
    private javax.swing.JPanel functionListPanel;
    private javax.swing.ButtonGroup functionSetButtonGroup;
    private javax.swing.JList<String> functionsList;
    private javax.swing.JScrollPane functionsScrollPane;
    private javax.swing.JPanel functionsToolBarPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JToggleButton nativeToggleButton;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
