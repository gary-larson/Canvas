package com.antonioramos.canvas;

/**
 * Created by Gary on 3/26/2017.
 * myRectangle class is used to draw rectangles and inherits from MyShape
 */

public class MyRectangle extends MyShape {
    // Sets serialable version number
    private static final long serialVersionUid = 1l;

    /*
    Created by Gary
    constructor for MyRectangle Class
     */
    public MyRectangle (float x1, float y1, float x2, float y2, float stroke, int color) {
        super (x1, y1, x2, y2, stroke, color);
        AdjustCoordinates();
    }

    /*
        Created by Gary
        Adjusts the rectangles endpoints so they can be drawn
     */
    public void AdjustCoordinates () {
        float temp;

        if (x_start > x_stop) {
            temp = x_start;
            x_start = x_stop;
            x_stop = temp;
        }
        if (y_start > y_stop) {
            temp = y_start;
            y_start = y_stop;
            y_stop = temp;
        }
    }
}
