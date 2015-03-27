package alpharunescape.src.utils;

import org.valkyr.api.framework.gui.utils.Returnable;

import javax.swing.*;
import java.awt.*;

public class LabelUpdater implements Runnable {

    private final JLabel label;
    private final Returnable returnable;
    private final int interval;

    public LabelUpdater(JLabel label, Returnable returnable, int interval) {
        this.label = label;
        this.returnable = returnable;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (label.isVisible()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    label.setText(String.valueOf(returnable.get()));
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