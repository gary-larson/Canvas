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
import java.util.Random;

import static com.antonioramos.canvas.R.id.canvas;

/**
 * Created by antonio on 3/21/2017.
 */

public class DrawShape extends View {
    private Paint drawShape;
    private float x_begin,y_begin;
    private float x_end,y_end;
    private int currentColor = Color.BLACK;
    float strokeWidth =1;
    ArrayList<MyShape> shapes;
    boolean lockPoint = false;
    boolean lock= false;
    boolean ballDraw = false;

    ArrayList<Integer> old_x =new ArrayList<>();
    ArrayList<Integer> new_y = new ArrayList<>();
    ArrayList<Integer> old_y = new ArrayList<>();
    ArrayList<Integer> new_x = new ArrayList<>();
    ArrayList<Integer> ballColor = new ArrayList<>();

    int numberOfballs =0;

    public  static final int BALL_SIZE_DPI=50;
    public static final int BALL_STEP_DPI=5;
    Paint ballPaint;
    int currentWidth;
    int currentHeight;
    AnimateThread animateThread;
    ArrayList<Ball>balls;
    private Random random;


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
        shapes = new ArrayList<>();
        drawShape = new Paint();
        drawShape.setColor(currentColor);
        drawShape.setStyle(Paint.Style.STROKE);
        drawShape.setAntiAlias(true);

        random = new Random();
        balls = new ArrayList<>();
        ballPaint = new Paint();
        ballPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for(MyShape shape : shapes) {
            drawShape.setColor(shape.currentColor);
            drawShape.setStrokeWidth(shape.strokeWidth);
            if (shape instanceof MyLine) {
                canvas.drawLine(shape.x_start, shape.y_start, shape.x_stop, shape.y_stop, drawShape);
            } else if (shape instanceof MyRectangle) {
                canvas.drawRect(shape.x_start, shape.y_start, shape.x_stop, shape.y_stop, drawShape);
            }
        }
        if (MainActivity.shapeType.equals("line")) {
            canvas.drawLine(x_begin, y_begin, x_end, y_end, drawShape);
        } else if (MainActivity.shapeType.equals("rectangle")) {
            canvas.drawRect(x_begin, y_begin, x_end, y_end, drawShape);
        }
        if(ballDraw) {
            for (Ball ball : balls) {
                ballPaint.setColor(ball.color);

                canvas.drawOval(ball.x, ball.y,
                        ball.x + ball.sizePixels, ball.y + ball.sizePixels,
                        ballPaint);
            }
        }

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

                /*
                Created by Gary to accommodate multiple shapes
                 */
                if (MainActivity.shapeType.equals("line")) {
                    MyLine newMyLine = new MyLine(x_begin,y_begin,x_end,y_end,strokeWidth, currentColor);
                    shapes.add(newMyLine);
                } else if (MainActivity.shapeType.equals("rectangle")) {
                    MyRectangle newMyRectangle = new MyRectangle(x_begin,y_begin,x_end,y_end,strokeWidth, currentColor);
                    shapes.add(newMyRectangle);
                }
                /*
                x_begin = 0f;
                y_begin = 0f;
                x_end = 0f;
                y_end = 0f;
                */
                invalidate();
                break;
        }

        return true;
    }
    /*
       sets line shape color -by Antonio
    */
    public void setColor(int color){
        currentColor = color;
    }
    /*
      sets line weight, create new object and pass line data and adds object to ArrayList
      this method ensures only future lines are effected by line weight setting -by Antonio
   */
    public void setLineWeight(int weight){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,weight,dm);
    }

    /*
    Created by Gary to work with shapes
     */
    public void setList(ArrayList<MyShape> shapes){
        this.shapes = shapes;
        invalidate();
    }

    /*
    created by Gary
    clears the ArrayList<MyShape>
     */
    public void ClearList () {
        this.shapes.clear();
        this.ballColor.clear();
        this.new_x.clear();
        this.new_y.clear();
        this.old_x.clear();
        this.old_y.clear();

        invalidate();
    }

    /*
       returns ArrayList<Myshape> to main for saving - by Gary & Antonio
    */
    public ArrayList<MyShape> getMyList() {
        return shapes;
    }
    /*
      locks start point in place -by Antonio
   */
    public void setLock(boolean lockStartPoint){
        lock = lockStartPoint;
    }
    /*
     unlocks start point -by Antonio
   */
    public void unLock(boolean unlockStartPoint){
        lockPoint =lock = unlockStartPoint;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentHeight = h;
        currentWidth = w;
    }

    public void addBall() {

       if(numberOfballs < 1) {
           ballDraw = true;
           Ball ball = new Ball(BALL_SIZE_DPI, BALL_STEP_DPI);
           balls.add(ball);
           invalidate();
           start();
       }
        numberOfballs++;
    }

    public void start() {
        if (animateThread == null) {
            animateThread = new AnimateThread();
            animateThread.start();
        }
    }

    public void stop() {
        if (animateThread != null) {
            animateThread.running = false;
            animateThread = null;
            ballDraw = false;
            numberOfballs = 0;
        }
        MyLine newMyLine;
        this.balls.clear();
        for(int i = 0; i <new_x.size();i++){

            newMyLine = new MyLine(old_x.get(i),old_y.get(i),new_x.get(i),new_y.get(i),strokeWidth,
                    ballColor.get(i));
            shapes.add(newMyLine);

        }
        invalidate();
    }

    private class AnimateThread extends Thread {


        boolean running = true;

        @Override
        public void run() {
            while (running) {
                int size = balls.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        Ball ball = balls.get(i);
                        old_x.add(ball.x);
                        old_y.add(ball.y);
                        ballColor.add(ball.color);

                        ball.x += ball.xStep;
                        ball.y += ball.yStep;

                        new_x.add(ball.x);
                        new_y.add(ball.y);

                        if (ball.x < 0) {
                            ball.xStep = -ball.xStep;

                        } else if (ball.x + ball.sizePixels > currentWidth) {
                            ball.xStep = -ball.xStep;
                        }

                        if (ball.y < 0) {
                            ball.yStep = -ball.yStep;
                        } else if (ball.y + ball.sizePixels > currentHeight) {
                            ball.yStep = -ball.yStep;
                        }
                    }
                    postInvalidate();
                }
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }
    }

    private class Ball {
        int x;
        int y;
        float sizePixels;
        float xStep;
        float yStep;
        int color;

        Ball(int ballSizeDpi, int stepDpi) {
            color = random.nextInt(0x1000000) + 0xff000000;

            DisplayMetrics dm = getResources().getDisplayMetrics();
            sizePixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ballSizeDpi, dm);

            xStep = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, stepDpi, dm);
            yStep = xStep;

            x = random.nextInt((int) (currentWidth - sizePixels));
            y = random.nextInt((int) (currentHeight - sizePixels));
        }
    }



}