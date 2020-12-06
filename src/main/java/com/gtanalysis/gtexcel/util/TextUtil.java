/*
 */
package com.gtanalysis.gtexcel.util;

import com.gtanalysis.gtexcel.ui.ChartRadioButton;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nkabiliravi
 */
public class TextUtil {
    
    public static String loadFromClassPath(String resourceName) {
        InputStream is = TextUtil.class.getResourceAsStream(resourceName);
        try {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException ex) {
            Logger.getLogger(ChartRadioButton.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
