/*
 */
package com.gtanalysis.gtexcel.provider;

import com.gtanalysis.gtexcel.jni.BasicFunctions;
import com.gtanalysis.gtexcel.jni.FunctionSet;
import com.gtanalysis.gtexcel.jni.NativeFunction;
import com.gtanalysis.gtexcel.jni.StringFunctions;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
import org.apache.poi.ss.formula.udf.DefaultUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;

/**
 *
 * @author nkabiliravi
 */
public class JNIFunctionSetProvider {

    private static final FunctionSet[] FUNCTION_SETS = new FunctionSet[]{
        new BasicFunctions(),
        new StringFunctions()
    };

    private static JNIFunctionSetProvider instance = null;

    public static JNIFunctionSetProvider getInstance() {
        if (instance == null) {
            instance = new JNIFunctionSetProvider();
        }
        return instance;
    }

    private JNIFunctionSetProvider() {

    }

    public FunctionSet[] getFunctionSets() {
        return FUNCTION_SETS;
    }

    public String[] getCategories() {
        List<String> list = new ArrayList<>();
        for (FunctionSet functionSet : FUNCTION_SETS) {
            list.add(functionSet.getName());
        }
        String[] reuslt = new String[list.size()];
        return list.toArray(reuslt);
    }

    public List<NativeFunction> getFunctions(String category) {
        List<NativeFunction> result = new ArrayList<>();
        for (FunctionSet functionSet : FUNCTION_SETS) {
            if (ProviderConstants.ALL.equalsIgnoreCase(category)
                    || category.equalsIgnoreCase(functionSet.getName())) {
                result.addAll(functionSet.getNativeFunctions());
            }
        }
        return result;
    }

    public UDFFinder getToolPack() {
        List<String> functionNameList = new ArrayList<>();
        List<FreeRefFunction> freeRefFunctionList = new ArrayList<>();
        for (FunctionSet functionSet : FUNCTION_SETS) {
            functionSet.getNativeFunctions().stream().forEach((nativeFunction) -> {
                functionNameList.add(nativeFunction.getName());
                freeRefFunctionList.add(nativeFunction);
            });

        }
        String[] functionNames = new String[functionNameList.size()];
        functionNameList.toArray(functionNames);
        FreeRefFunction[] functionImpls = new FreeRefFunction[freeRefFunctionList.size()];
        freeRefFunctionList.toArray(functionImpls);

        return new DefaultUDFFinder(functionNames, functionImpls);
    }

}
