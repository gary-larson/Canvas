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
    private int lineWeight =1;
    private int currentColor = Color.BLACK;
    ArrayList<Lines> lines;
    Lines l;

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
        lineColor = currentColor;   //******To Gary******* change when you add color method the only time this is called is in the constructor
        // so it does not change the color
        drawShape = new Paint();
        drawShape.setColor(lineColor);
        drawShape.setStyle(Paint.Style.STROKE);
        drawShape.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Lines l : lines) {
            drawShape.setStrokeWidth(l.strokeWidth); //is used to update line weight
            canvas.drawLine(l.x_start, l.y_start, l.x_stop, l.y_stop, drawShape); //used to redraw old lines
        }
            canvas.drawLine(x_begin, y_begin, x_end, y_end, drawShape); // draw current line
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
           //save start points to variables and invalidates
            case MotionEvent.ACTION_DOWN:
                x_begin =event.getX();
                y_begin =event.getY();
                x_end = event.getX();
                y_end= event.getY();
                invalidate();
                break;
            //save finger drag points and invalidates
            case MotionEvent.ACTION_MOVE:
                x_end = event.getX();
                y_end= event.getY();
                invalidate();
                break;
            //saves last point, create new object with start and end points, adds object to ArrayList
            case MotionEvent.ACTION_UP:
                x_end = event.getX();
                y_end= event.getY();
                l = new Lines(x_begin,y_begin,x_end,y_end,lineWeight);
                lines.add(l);
                invalidate();
                break;
        }

        return true;
    }
    //sets line weight, create new object and pass line data and adds object to ArrayList
    //this method ensures only future lines are effected by line weight setting
    public void setLineWeight(int weight){
        lineWeight = weight;
        l = new Lines(0,0,0,0, weight);
        lines.add(l);
    }
    //class saves line data
    private class Lines{
        float x_start,x_stop ;
        float y_start, y_stop;
        int setWeight;
        float strokeWidth;


        Lines(float x, float y, float xx, float yy, int weight){
            x_start =x;
            y_start =y;
            x_stop =xx;
            y_stop=yy;
            setWeight = weight;

            DisplayMetrics dm = getResources().getDisplayMetrics();
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,setWeight,dm);
        }

    }

    public void setColor (int color) {
        currentColor = color;
    }

}
