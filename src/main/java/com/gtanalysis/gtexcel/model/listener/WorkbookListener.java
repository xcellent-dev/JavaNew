/*
 */
package com.gtanalysis.gtexcel.model.listener;

/**
 *
 * @author nkabiliravi
 */
public interface WorkbookListener {

    public void workbookCreated(WorkbookEvent evt);

    public void workbookLoaded(WorkbookEvent evt);

    public void workbookSaved(WorkbookEvent evt);

    public void workbookClosed(WorkbookEvent evt);

}
