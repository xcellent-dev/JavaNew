/*
 */
package com.gtanalysis.gtexcel.model;

import com.gtanalysis.gtexcel.provider.BuiltinFunctionSetProvider;
import com.gtanalysis.gtexcel.provider.ProviderConstants;
import com.gtanalysis.gtexcel.provider.JNIFunctionSetProvider;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;

/**
 *
 * @author nkabiliravi
 */
public class FunctionDialogModel {

    private final JNIFunctionSetProvider jNIFunctionSetProvider = JNIFunctionSetProvider.getInstance();
    private final BuiltinFunctionSetProvider builtinFunctionSetProvider = BuiltinFunctionSetProvider.getInstance();

    private FunctionSetType functionSetType = FunctionSetType.COMMON;
    private ListModel currentListModel;
    private DefaultComboBoxModel<String> categoryListModel;

    public FunctionDialogModel() {
        initModel();
    }

    public FunctionSetType getFunctionSetType() {
        return functionSetType;
    }

    public void setFunctionSetType(FunctionSetType functionSetType) {
        this.functionSetType = functionSetType;
        initModel();
    }

    public DefaultComboBoxModel<String> getCategoryListModel() {
        return categoryListModel;
    }

    public ListModel getCurrentListModel() {
        return currentListModel;
    }

    private void initModel() {
        initComboModel();
        initListModel(ProviderConstants.ALL);
    }

    private void initComboModel() {
        if (this.functionSetType == FunctionSetType.COMMON) {
            categoryListModel = new DefaultComboBoxModel<>(new String[]{ProviderConstants.ALL});
        } else if (this.functionSetType == FunctionSetType.NATIVE) {
            categoryListModel = new DefaultComboBoxModel<>(jNIFunctionSetProvider.getCategories());
            categoryListModel.insertElementAt(ProviderConstants.ALL, 0);
            categoryListModel.setSelectedItem(ProviderConstants.ALL);
        }
    }

    public void initListModel(String category) {
        if (this.functionSetType == FunctionSetType.COMMON) {
            currentListModel = new BuiltinListModel(builtinFunctionSetProvider.getFunctionSets(category));
        } else if (this.functionSetType == FunctionSetType.NATIVE) {
            currentListModel = new NativeListModel(jNIFunctionSetProvider.getFunctions(category));
        }
    }
}
