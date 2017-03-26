package com.antonioramos.canvas;

import java.io.Serializable;

/**
 * Created by Gary on 3/26/2017.
 * Super Class to draw shapes
 */

public class MyShape implements Serializable {
    // Sets serialable version number
    private static final long serialVersionUid = 1l;

    float x_start,x_stop ;
    float y_start, y_stop;
    float strokeWidth;
    int currentColor;

    public MyShape (float x1, float y1, float x2, float y2, float stroke, int color) {
        x_start = x1;
        y_start = y1;
        x_stop = x2;
        y_stop = y2;
        strokeWidth = stroke;
        currentColor = color;
    }
}
