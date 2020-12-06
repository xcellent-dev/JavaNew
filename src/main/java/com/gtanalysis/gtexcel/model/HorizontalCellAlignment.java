/*
 */
package com.gtanalysis.gtexcel.model;

import javax.swing.SwingConstants;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author nkabiliravi
 */
public enum HorizontalCellAlignment implements CellAlignment {

    LEFT(CellStyle.ALIGN_LEFT, SwingConstants.LEFT), 
    RIGHT(CellStyle.ALIGN_RIGHT, SwingConstants.RIGHT), 
    HORIZONTAL_CENTER(CellStyle.ALIGN_CENTER, SwingConstants.CENTER);

    private final int modelValue;
    private final int viewValue;

    private HorizontalCellAlignment(short modelValue, int viewValue) {
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

    public static HorizontalCellAlignment fromModelValue(int modelValue) {
        for(HorizontalCellAlignment item : values()) {
            if(item.getModelValue() == modelValue) {
                return item;
            }
        }
        return HORIZONTAL_CENTER;
    }
    
    public static HorizontalCellAlignment fromViewValue(int viewValue) {
        for(HorizontalCellAlignment item : values()) {
            if(item.getViewValue() == viewValue) {
                return item;
            }
        }
        return HORIZONTAL_CENTER;
    }
    
}
