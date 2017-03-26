/*
Created by Gary
Activity to select and return a color
 */

package com.antonioramos.canvas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorActivity extends AppCompatActivity {
    // sets default for selectedColor
    int selectedColor = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        created by Gary
        sets up on touch listener to get the color the user clicks/touchs
         */
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        iv.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int x = (int)event.getX();
                int y = (int)event.getY();
                int pixel = bitmap.getPixel(x,y);

                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setBackgroundColor(pixel);
                // set instance variable with the selected color
                selectedColor = pixel;

                return false;
            }
        });
        Button b = (Button) findViewById(R.id.lock_button);
        /*
        Created by Gary
        set up button listener to return to calling activity
         */
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
    Created by Gary
    Closes and returns color chosen by user
     */
    @Override
    public void finish() {
        Intent intent = new Intent();

        intent.putExtra(MainActivity.COLOR_CHOICE_KEY, selectedColor);
        setResult(RESULT_OK, intent);
        Log.i("SELECTEDCOLOR", Integer.toString(selectedColor));
        super.finish();
    }

}
