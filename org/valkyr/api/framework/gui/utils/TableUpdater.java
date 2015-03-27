package org.valkyr.api.framework.gui.utils;

import org.valkyr.api.framework.gui.GuiTab;

import java.awt.*;

/**
 * @Author Josef
 */
public class TableUpdater implements Runnable {
    private final int row;
    private final int column;
    private final long interval;
    private final GuiTab tab;

    /**
     * @param row
     * @param column
     * @param interval The interval between updates in milliseconds.
     */
    public TableUpdater(int row, int column, long interval, GuiTab tab) {
        this.row = row;
        this.column = column;
        this.interval = interval;
        this.tab = tab;
    }

    @Override
    public void run() {
        while (tab.isVisible()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    tab.getTableModel().fireTableCellUpdated(row, column);
                }
            });
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}