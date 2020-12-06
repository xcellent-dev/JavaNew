/*
 */
package com.gtanalysis.gtexcel.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author nkabiliravi
 */
public class BuiltinListModel implements ListModel<String>{

    private final List<String> functions;
    
    public BuiltinListModel(List<String> functions) {
        this.functions = functions;
    }

    @Override
    public int getSize() {
        return this.functions.size();
    }

    @Override
    public String getElementAt(int index) {
        return this.functions.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
    
}
