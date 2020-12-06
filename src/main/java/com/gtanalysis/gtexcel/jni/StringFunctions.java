/*
 */
package com.gtanalysis.gtexcel.jni;

/**
 *
 * @author nkabiliravi
 */
public class StringFunctions extends AbstractFunctionSet {
    
    @ExcelFunction(description = "Return the count of array of strings")
    public native int arrlength(String[] str);
    
    static {
        System.loadLibrary("gt-functions");
    }

    @Override
    public String getName() {
        return "String Functions";
    }
}
