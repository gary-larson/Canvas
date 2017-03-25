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
    private float x_begin,y_begin;
    private float x_end,y_end;
    private int currentColor = Color.BLACK;
    float strokeWidth =1;
    ArrayList<Lines> lines;
    Lines newLine;
    boolean lockPoint = false;
    boolean lock= false;

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
        drawShape = new Paint();
        drawShape.setColor(currentColor);
        drawShape.setStyle(Paint.Style.STROKE);
        drawShape.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Lines newLine : lines) {
            drawShape.setColor(newLine.currentColor);
            drawShape.setStrokeWidth(newLine.strokeWidth); //is used to update line weight
            canvas.drawLine(newLine.x_start, newLine.y_start, newLine.x_stop, newLine.y_stop, drawShape); //used to redraw old lines
        }
            canvas.drawLine(x_begin, y_begin, x_end, y_end, drawShape); // draw current line
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
           //save start points to variables and invalidates
            case MotionEvent.ACTION_DOWN:
                //this if statement helps control locking and unlocking start point
                if(!lockPoint) {
                    x_begin = event.getX();
                    y_begin = event.getY();
                    x_end = event.getX();
                    y_end = event.getY();
                    invalidate();
                    lockPoint =lock;
                }
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
                newLine = new Lines(x_begin,y_begin,x_end,y_end,strokeWidth, currentColor);
                lines.add(newLine);
                invalidate();
                break;
        }

        return true;
    }
    //set color to shapes
    public void setColor(int color){
        currentColor = color;
    }
    //sets line weight, create new object and pass line data and adds object to ArrayList
    //this method ensures only future lines are effected by line weight setting
    public void setLineWeight(int weight){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,weight,dm);
        newLine = new Lines(0,0,0,0, strokeWidth, currentColor);
        lines.add(newLine);
    }
    //redraws canvas
    public void setList(ArrayList<Lines> line){
        lines = line;
        invalidate();
    }
    //passes line arraylist to main for saving
    public ArrayList<Lines> getList(){

        return lines;

    }
    //locks start point
    public void setLock(boolean lockStartPoint){
       lock = lockStartPoint;
    }
    //unlocks start point
    public void unLock(boolean unlockStartPoint){
        lockPoint =lock = unlockStartPoint;
    }


}
