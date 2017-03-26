package com.antonioramos.canvas;

/**
 * Created by Gary on 3/26/2017.
 * myLine class is used to draw lines and inherits from MyShape
 */

public class MyLine extends MyShape {
    // Sets serialable version number
    private static final long serialVersionUid = 1l;

    /*
    Created by Gary
    constructor for MyLine Class
     */
    public MyLine (float x1, float y1, float x2, float y2, float stroke, int color) {
        super (x1, y1, x2, y2, stroke, color);
    }
}
