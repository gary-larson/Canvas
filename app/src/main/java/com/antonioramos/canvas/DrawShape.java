package com.antonioramos.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by antonio on 3/21/2017.
 */

public class DrawShape extends View {
    private Paint drawShape;
    private int lineColor;
    private float x_begin,y_begin;
    private float x_end,y_end;
    private boolean checkLine = false;

    ArrayList<Lines> lines;

    public DrawShape(Context context) {
        super(context);
        setup(null);
    }

    public DrawShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public DrawShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attributeSet){
        lines = new ArrayList<>();
        lineColor = Color.BLACK;
        drawShape = new Paint();
        drawShape.setColor(lineColor);
        drawShape.setStyle(Paint.Style.STROKE);
        drawShape.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Lines l : lines) {
            canvas.drawLine(l.x_start, l.y_start, l.x_stop, l.y_stop, drawShape);
        }
            canvas.drawLine(x_begin, y_begin, x_end, y_end, drawShape);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x_begin =event.getX();
                y_begin =event.getY();
                x_end = event.getX();
                y_end= event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x_end = event.getX();
                y_end= event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                x_end = event.getX();
                y_end= event.getY();
                Lines l = new Lines(x_begin,y_begin,x_end,y_end);
                lines.add(l);
                invalidate();
                break;

        }

        return true;
    }
    public void setLineWeight(int weight){
        weight = 2*weight;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,weight,dm);
        drawShape.setStrokeWidth(strokeWidth);
        invalidate();

    }

    private class Lines{
        float x_start,x_stop ;
        float y_start, y_stop;


        Lines(float x, float y, float xx, float yy){
            x_start =x;
            y_start =y;
            x_stop =xx;
            y_stop=yy;

        }

    }

}
