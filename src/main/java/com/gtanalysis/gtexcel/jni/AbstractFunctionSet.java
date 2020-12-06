/*
 */
package com.gtanalysis.gtexcel.jni;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nkabiliravi
 */
public abstract class AbstractFunctionSet implements FunctionSet {

    @Override
    public List<NativeFunction> getNativeFunctions() {
        List<NativeFunction> result = new ArrayList<>();
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            int mod = method.getModifiers();
            if ((mod & Modifier.NATIVE) != 0) {
                String description = null;
                ExcelFunction excelAnnotation = method.getAnnotation(ExcelFunction.class);
                if(excelAnnotation != null) {
                    description = excelAnnotation.description();
                }
                result.add(new NativeFunction(this, method.getName(), description, method));
            }
        }
        return result;
    }

}
