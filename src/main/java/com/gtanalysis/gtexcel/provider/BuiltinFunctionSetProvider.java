/*
 */
package com.gtanalysis.gtexcel.provider;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.formula.eval.FunctionEval;

/**
 *
 * @author nkabiliravi
 */
public class BuiltinFunctionSetProvider {

    private static BuiltinFunctionSetProvider instance = null;
    
    public static BuiltinFunctionSetProvider getInstance() {
        if(instance == null) {
            instance = new BuiltinFunctionSetProvider();
        }
        return instance;
    }
    
    private List<String> functions;
    
    private BuiltinFunctionSetProvider() {
        
    }
    
    public List getFunctionSets(String category) {
        if(functions == null) {
            initFunctionsList();
        }
        return functions;
    }

    private void initFunctionsList() {
        functions = new ArrayList<>();
        functions.addAll(FunctionEval.getSupportedFunctionNames());
    }
}
