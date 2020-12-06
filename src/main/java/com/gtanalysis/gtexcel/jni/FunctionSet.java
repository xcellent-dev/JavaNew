/*
 */
package com.gtanalysis.gtexcel.jni;

import java.util.List;

/**
 *
 * @author nkabiliravi
 */
public interface FunctionSet {
    
    String getName();
    
    List<NativeFunction> getNativeFunctions();
}
