package org.culpan.fcm;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by harryculpan on 5/27/17.
 */
public class DayBlock extends Rectangle {
    protected int dayOfMonth;

    public DayBlock(int dayOfMonth) {
        super(100, 125, Color.GRAY);
        setStroke(Color.BLACK);
        setStrokeWidth(4);
        setFill(Color.TRANSPARENT);

        this.dayOfMonth = dayOfMonth;
    }
}
