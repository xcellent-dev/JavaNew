package com.gtanalysis.gtexcel.model.action;

import com.gtanalysis.gtexcel.model.CellModel;
import com.gtanalysis.gtexcel.model.WorkbookModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nkabiliravi
 */
public class ActionModel {

    private final WorkbookModel workbookModel;
    private final XSSFWorkbook workbook;
    private final List<Action> actions;
    private final List<Action> redoActions;

    public ActionModel(WorkbookModel workbookModel) {
        this.workbookModel = workbookModel;
        workbook = new XSSFWorkbook();
        actions = new ArrayList<>();
        redoActions = new ArrayList<>();
    }

    public void createChangeCellValueAction(Cell changedCell, CellModel oldValue) {
        ChangeCellValueAction action = new ChangeCellValueAction(workbookModel, workbook, changedCell, oldValue);
        actions.add(action);
    }

    public void clear() {
        actions.clear();
        redoActions.clear();
    }

    private void undo(int actionIndex) {
        Action action = actions.get(actionIndex);
        action.undo();
       
        actions.remove(actionIndex);
        redoActions.add(action);
    }

    public boolean isUndoEnabled() {
        return !actions.isEmpty();
    }

    public boolean isRedoEnabled() {
        return !redoActions.isEmpty();
    }
    
    public void undo() {
        if (!actions.isEmpty()) {
            undo(actions.size() - 1);
        }
    }

    public void redo() {
        if (!redoActions.isEmpty()) {
            Action redoAction = redoActions.get(0);
            redoAction.redo();
            actions.add(redoAction);
            redoActions.remove(0);
        }
    }

}
