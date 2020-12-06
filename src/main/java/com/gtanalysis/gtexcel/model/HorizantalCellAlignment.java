/*
 */
package com.gtanalysis.gtexcel.model;

import javax.swing.SwingConstants;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author nkabiliravi
 */
public enum HorizantalCellAlignment implements CellAlignment {

    LEFT(CellStyle.ALIGN_LEFT, SwingConstants.LEFT), 
    RIGHT(CellStyle.ALIGN_RIGHT, SwingConstants.RIGHT), 
    HORIZANTAL_CENTER(CellStyle.ALIGN_CENTER, SwingConstants.CENTER);

    private final int modelValue;
    private final int viewValue;

    private HorizantalCellAlignment(short modelValue, int viewValue) {
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

    public static HorizantalCellAlignment fromModelValue(int modelValue) {
        for(HorizantalCellAlignment item : values()) {
            if(item.getModelValue() == modelValue) {
                return item;
            }
        }
        return HORIZANTAL_CENTER;
    }
    
    public static HorizantalCellAlignment fromViewValue(int viewValue) {
        for(HorizantalCellAlignment item : values()) {
            if(item.getViewValue() == viewValue) {
                return item;
            }
        }
        return HORIZANTAL_CENTER;
    }
    
}
