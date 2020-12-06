package com.gtanalysis.gtexcel.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;

public class RowHeader extends JComponent {

    private final JTable table;

    private final CellRendererPane rendererPane = new CellRendererPane();

    private final TableCellRenderer renderer
            = new HeaderRenderer(HeaderRenderer.Axis.VERTICAL);

    private final int width = 30;

    private final int minRowHeight = 5;

    private final int rowMargin = 1;

    private int resizingRow = -1;

    public RowHeader(JTable table) {
        this.table = table;
        add(rendererPane);
        setMinimumSize(new Dimension(0, 0));
        setPreferredSize(new Dimension(width, table.getRowCount() * table.getRowHeight()));
        MouseInputListener rowResizer = new RowResizer();
        addMouseListener(rowResizer);
        addMouseMotionListener(rowResizer);
    }

    @Override
    public void paint(Graphics g) {
        // Calculates visible area
        Rectangle bounds = g.getClipBounds();
        Point top = bounds.getLocation();
        Point bottom = new Point(bounds.x, bounds.y + bounds.height - 1);

        // Finds rows to paint
        int minRow = table.rowAtPoint(top);
        int maxRow = table.rowAtPoint(bottom);
        if (minRow == -1) {
            minRow = 0;
        }
        if (maxRow == -1) {
            maxRow = table.getRowCount() - 1;
        }

        // Paints rows
        int y = table.getCellRect(minRow, 0, true).y;
        int[] selectedRows = table.getSelectedRows();
        for (int row = minRow; row <= maxRow; row++) {
            // Fetches component from renderer
            boolean selected = Arrays.binarySearch(selectedRows, row) >= 0;
            Component c = renderer.getTableCellRendererComponent(
                    table, null, selected, false, row, -1);

            // Calculates coordinates and paints component
            int rowHeight = table.getRowHeight(row);
            rendererPane.paintComponent(g, c, this,
                    0, y, width, rowHeight, true);
            y += rowHeight;
        }
    }

    /**
     * Adjusts the height of the row at the given index to precisely fit all
     * data being rendered.
     *
     * @param row the index of the row to auto-resize
     */
    public void autoResize(int row) {
        int height = calcDefaultHeight(row);
        if (height > minRowHeight) {
            table.setRowHeight(resizingRow, height);
        } else {
            table.setRowHeight(minRowHeight);
        }
        repaint();
    }

    private int calcDefaultHeight(int row) {
        // Gets width of row header
        int height = renderer.getTableCellRendererComponent(table, null,
                false, false, row, 0).getPreferredSize().height;
        // Gets maximum width of column data
        for (int column = 0; column < table.getColumnCount(); column++) {
            Component c = table.getCellRenderer(row, column)
                    .getTableCellRendererComponent(table, table.getValueAt(row, column), false, false, row, column);
            height = Math.max(height, c.getPreferredSize().height);
        }
        // Adds margin
        height += 2 * rowMargin;
        return height;
    }

    public void collapseRow(int index) {
        table.setRowHeight(index, 1);
        table.updateUI();
    }

    private void expandSiblingRows(int index) {
        if (index + 1 < this.table.getRowCount()) {
            table.setRowHeight(index + 1, calcDefaultHeight(index));
        }

        if (index - 1 >= 0) {
            table.setRowHeight(index - 1, calcDefaultHeight(index));
        }
        updateUI();
        table.updateUI();
    }

    /**
     * A mouse input listener that enables resizing of rows
     */
    protected class RowResizer extends MouseInputAdapter {

        /**
         * The normal cursor
         */
        private final Cursor NORMAL_CURSOR = getCursor();

        /**
         * The cursor to display when resizing a row
         */
        private final Cursor RESIZE_CURSOR
                = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);

        /**
         * Selects the clicked row, unless resizing is intended.
         *
         * @param e the event that was fired
         */
        @Override
        public void mousePressed(MouseEvent e) {
            // Checks what was done
            resizingRow = getResizingRow(e.getPoint());
            if (resizingRow == -1) {
                int pressedRow = table.rowAtPoint(e.getPoint());
                int columns = table.getColumnCount();

                // Configures new selection
                if (e.isShiftDown()) {
                    table.changeSelection(pressedRow, 0, false, true);
                } else if (e.isControlDown()) {
                    table.changeSelection(pressedRow, 0, true, false);
                } else {
                    table.changeSelection(pressedRow, columns, false, false);
                    table.changeSelection(pressedRow, 0, false, true);
                }
                repaint();
            }
        }

        /**
         * Auto-resizes a column whose border was double-clicked.
         *
         * @param e the event that was fired
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int selectedRowIndex = table.rowAtPoint(e.getPoint());
                if (resizingRow != -1) {
                    autoResize(resizingRow);
                } else if (selectedRowIndex > -1) {
                    if (e.isShiftDown()) {
                        expandSiblingRows(selectedRowIndex);
                    } else {
                        collapseRow(selectedRowIndex);
                    }
                }
            }
        }

        /**
         * Sets the appropriate cursor depending on whether the mouse is on a
         * row that can be resized.
         *
         * @param e the event that was fired
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            setCursor(getResizingRow(e.getPoint()) == -1
                    ? NORMAL_CURSOR : RESIZE_CURSOR);
        }

        /**
         * Resizes the row that is dragged
         *
         * @param e the event that was fired
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (resizingRow != -1) {
                int rowHeight = e.getPoint().y
                        - table.getCellRect(resizingRow, 0, true).y;
                if (rowHeight >= minRowHeight) {
                    table.setRowHeight(resizingRow, rowHeight);
                }
                repaint();
            }
        }

        /**
         * Retrieves the index of the row at the given point, if it can be
         * resized.
         *
         * @param p the point to look at
         * @return the index of the row, or -1 if it can not be resized
         */
        private int getResizingRow(Point p) {
            // Fetches the row index, and stops if it is invalid
            int row = table.rowAtPoint(p);
            if (row == -1) {
                return row;
            }

            // Fetches the bounding rectangle of the header row
            Rectangle r = table.getCellRect(row, 0, true);
            r = new Rectangle(0, r.y, width, table.getRowHeight(row));
            r.grow(0, -2);

            // Stops if the point is inside the header row
            if (r.contains(p)) {
                return -1;
            }

            // If above the middle of the row, resize previous row
            if (p.y < (r.y + (r.height / 2))) {
                row--;
            }
            return row;
        }
    }
}
