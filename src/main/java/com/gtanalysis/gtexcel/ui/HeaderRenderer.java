package com.gtanalysis.gtexcel.ui;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class HeaderRenderer extends DefaultTableCellRenderer {

    public static enum Axis {
        HORIZONTAL, VERTICAL
    } 
        
    private final Axis axis;

    public HeaderRenderer(Axis axis) {        
        this.axis = axis;

        // Uses table header's appearance
        LookAndFeel.installColorsAndFont(this, "TableHeader.background",
                "TableHeader.foreground", "TableHeader.font");
        LookAndFeel.installProperty(this, "opaque", Boolean.TRUE);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean selected, boolean hasFocus, int row, int column) {
        if (axis == Axis.HORIZONTAL) {
            selected = Arrays.binarySearch(table.getSelectedColumns(), column)
                    >= 0;
        }

        setBackground(UIManager.getColor("TableHeader."
                + (selected ? "selectionBackground" : "background")));
        setForeground(UIManager.getColor("TableHeader."
                + (selected ? "selectionForeground" : "foreground")));

        if (axis == Axis.VERTICAL) {
            setText(Integer.toString(row + 1));
        } else {
            setText(value != null ? value.toString() : null);
        }
        return this;
    }
    
}
