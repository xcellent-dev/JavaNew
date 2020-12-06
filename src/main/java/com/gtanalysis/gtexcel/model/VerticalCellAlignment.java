package com.gtanalysis.gtexcel.model;

import javax.swing.SwingConstants;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author nkabiliravi
 */
public enum VerticalCellAlignment implements CellAlignment {

    TOP(CellStyle.VERTICAL_TOP, SwingConstants.TOP), 
    BOTTOM(CellStyle.VERTICAL_BOTTOM, SwingConstants.BOTTOM), 
    VERTICAL_CENTER(CellStyle.VERTICAL_CENTER, SwingConstants.CENTER);

    private final short modelValue;
    private final int viewValue;

    private VerticalCellAlignment(short modelValue, int viewValue) {
        this.modelValue = modelValue;
        this.viewValue = viewValue;
    }

    @Override
    public int getModelValue() {
        return modelValue;
    }

    @Override
    public int getViewValue() {
        return viewValue;
    }
    
    
    public static VerticalCellAlignment fromModelValue(int modelValue) {
        for(VerticalCellAlignment item : values()) {
            if(item.getModelValue() == modelValue) {
                return item;
            }
        }
        return VERTICAL_CENTER;
    }
    
    public static VerticalCellAlignment fromViewValue(int viewValue) {
        for(VerticalCellAlignment item : values()) {
            if(item.getViewValue() == viewValue) {
                return item;
            }
        }
        return VERTICAL_CENTER;
    }
}
