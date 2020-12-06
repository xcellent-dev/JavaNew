/*
 */
package com.gtanalysis.gtexcel.model;

import com.gtanalysis.gtexcel.jni.NativeFunction;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author nkabiliravi
 */
public class NativeListModel implements ListModel<NativeFunction>{

    private final List<NativeFunction> functions;
    
    public NativeListModel(List<NativeFunction> functions) {
        this.functions = functions;
    }

    @Override
    public int getSize() {
        return this.functions.size();
    }

    @Override
    public NativeFunction getElementAt(int index) {
        return this.functions.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
    
}
