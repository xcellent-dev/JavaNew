/*
 */
package com.gtanalysis.gtexcel.jni;

/**
 *
 * @author nkabiliravi
 */
public class BasicFunctions extends AbstractFunctionSet {
    
    
    @ExcelFunction(description = "Use this function to retrieve the 'sigma' value of an array of double numbers")
    public native double sigma(double[] arr);
    
    @ExcelFunction(description = "Use this function to retrieve the 'average' value of an array double numbers")
    public native double average(double[] arr);
    
    @ExcelFunction(description = "Use this function to retrieve the 'variance' value of an array double numbers")
    public native double variance(double[] arr);

    static {
        try {
        //get the library	
        System.loadLibrary("gt-functions");
        } catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public String getName() {
        return "Basic Functions";
    }

}
