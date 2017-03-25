package com.antonioramos.canvas;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by antonio on 3/24/2017.
 */

public class Lines extends AppCompatActivity implements Serializable{
    float x_start,x_stop ;
    float y_start, y_stop;
    float strokeWidth;
    int currentColor;

    public Lines(float x, float y, float xx, float yy, float weight, int color){
        x_start =x;
        y_start =y;
        x_stop =xx;
        y_stop=yy;
        currentColor = color;
        strokeWidth = weight;
    }



}




