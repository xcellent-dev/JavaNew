/*
 */
package com.gtanalysis.gtexcel.model.listener;

import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author nkabiliravi
 */
public class WorkbookEvent {

    private final Object source;

    private final Workbook workbook;

    public WorkbookEvent(Object source, Workbook workbook) {
        this.source = source;
        this.workbook = workbook;
    }

    public Object getSource() {
        return source;
    }

    public Workbook getWorkbook() {
        return workbook;
    }
    
}
