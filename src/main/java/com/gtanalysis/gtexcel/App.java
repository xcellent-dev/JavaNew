/*
 */
package com.gtanalysis.gtexcel;

import com.gtanalysis.gtexcel.ui.MainFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.UIManager;

/**
 *
 * @author nkabiliravi
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            mainFrame.setLocation(10, 10);
            mainFrame.setSize(new Dimension(
                    (int) dimension.getWidth() - 20,
                    (int) dimension.getHeight() - 100));
            
        });
    }
}
