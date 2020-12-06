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
public class BasicFunctionTest {

    private BasicFunctions basicFunction;

    @BeforeClass
    public static void setupClass() {
    }

    @Before
    public void setup() {
        basicFunction = new BasicFunctions();
    }

    @Test
    public void testSigma() {
        try {
            double[] arr = new double[5];
            arr[0] = 14.32;
            arr[1] = 26.1;
            arr[2] = 0.001;
            arr[3] = 100;
            arr[4] = 1.4567;
            double actual = basicFunction.sigma(arr);
            assertEquals(141.8777, actual, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Test
    public void testAverage() {
        try {
            double[] arr = new double[8];
            arr[0] = 8.12;
            arr[1] = 2.165;
            arr[2] = 24.71;
            arr[3] = 1.032;
            arr[4] = 14.96;
            arr[5] = 0.96;
            arr[6] = 32.6;
            arr[7] = 5.045;
            double actual = basicFunction.average(arr);
            assertEquals(123.98, actual, 0.05);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    @Test
    public void testVariance() {
        try {
            double[] arr = new double[8];
            arr[0] = 8.12;
            arr[1] = 2.165;
            arr[2] = 24.71;
            arr[3] = 1.032;
            arr[4] = 14.96;
            arr[5] = 0.96;
            arr[6] = 32.6;
            arr[7] = 5.045;
            double actual = basicFunction.variance(arr);
            assertEquals(89.592, actual, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
