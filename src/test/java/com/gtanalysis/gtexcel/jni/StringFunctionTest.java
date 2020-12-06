/*
 */
package com.gtanalysis.gtexcel.jni;

import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author nkabiliravi
 */
public class StringFunctionTest {
    
    private StringFunctions sringFunction;
    
    @BeforeClass
    public static void setupClass() {
    }
    
    @Before
    public void setup() {
        sringFunction = new StringFunctions();
    }
    
    @Test
    public void testArrayLength() {
        String[] arr = new String[7];
        arr[0] = "Sunday";
        arr[1] = "Monday";
        arr[2] = "Tuesday";
        arr[3] = "Wednesday";
        arr[4] = "Thursday";
        arr[5] = "Friday";
        arr[6] = "Saturday";
        int actual = sringFunction.arrlength(arr);
        assertEquals(7, actual);
    }
    
}
