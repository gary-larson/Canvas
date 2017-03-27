/*
* Developed by Antonio Ramos and Gary Larson
* These are the 4 additional features
* 1) using a spinner to set the line width
* 2) saving what was drawn
* 3) colorpicker
* 4) lock/unlock starting point in place
 */
package com.antonioramos.canvas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener
{
    private static final String DATA_FILE = "canvas.txt";
    private ArrayList<MyShape> shapes = new ArrayList<>();
    private DrawShape drawShape;
    public static String shapeType = "line";

    private static final int COLOR_RESULT = 110;
    public static final String COLOR_CHOICE_KEY = "currentColor";
    int [] buttonId = {R.id.unLock_button,R.id.lock_button,R.id.ball_button,R.id.stop_button};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setSpinner(R.id.line_spinner,R.array.lineWeight_spinner);

        for(int id : buttonId){
            Button operation = (Button) findViewById(id);
            operation.setOnClickListener(this);

        }
    }
    /*
   method to setup spinner -by Antonio
   */
    private void setSpinner(int id, int spinnerArray){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, spinnerArray, android.R.layout.simple_spinner_item);
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
            /*
            created by Gary
            calls intent to select a color
             */
        } else if (id == R.id.action_color) {
            Intent intent = new Intent(getApplicationContext(), ColorActivity.class);
            startActivityForResult(intent, COLOR_RESULT);
            /*
            Created by Gary
            sets ShapeType to rectangle
             */
        } else if (id == R.id.action_rectangle) {
            shapeType = "rectangle";
            /*
            Created by Gary
            sets ShapeType to rectangle
             */
        } else if (id == R.id.action_line) {
            shapeType = "line";
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_clear) {
            DrawShape drawShape = (DrawShape) findViewById(R.id.canvas);
            drawShape.ClearList();
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    Spinner method changes line weight -by Antonio
    */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        DrawShape drawShape =(DrawShape)findViewById(R.id.canvas);
        drawShape.setLineWeight(i+1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    /*
    Code created by Gary
    retrieves the result from calling the intent ColorActivity to select a color
    then sets the drawShape color
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == COLOR_RESULT) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(COLOR_CHOICE_KEY)) {
                    int colorChoice = data.getIntExtra(COLOR_CHOICE_KEY, Color.BLACK);
                    DrawShape drawShape =(DrawShape)findViewById(R.id.canvas);
                    drawShape.setColor(colorChoice);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Request Canceled", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /*
    method will call readData and send collected data to DrawShape class -by Antonio
    */
    @Override
    protected void onResume() {
        super.onResume();
        readData();
        DrawShape drawShape = (DrawShape) findViewById(R.id.canvas);
        drawShape.setList(shapes);
    }
    /*
    method will retrieve ArrayList data from a file and load ArrayList<MyShape>  -by Antonio
    */
    public void readData(){
        try {
            Log.i("READ", "entered read");
            FileInputStream fis = openFileInput(DATA_FILE);
            ObjectInputStream objectInStream = new ObjectInputStream(fis);
            shapes = (ArrayList<MyShape>) objectInStream.readObject();

            fis.close();
        }catch (FileNotFoundException e) {
            // Log.i("INFO", "---------- Read Exception");
            // ok if file does not exist
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("READ", "Can not find Data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
    /*
   method will retrieve ArrayList<MyShape> from DrawShape class and save data to a file -by Antonio
   */
    public void saveData() {
        drawShape = (DrawShape) findViewById(R.id.canvas);
        shapes = drawShape.getMyList();

        try {

            FileOutputStream fis = openFileOutput(DATA_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fis);
            objectOutputStream.writeObject(shapes);
            fis.close();
            Log.e("writeData", "*************************************************** ");

        } catch (FileNotFoundException e) {
            Log.e("WRITE", "Cannot save data: " + e.getMessage());
            e.printStackTrace();
            //Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("WRITE", "Cannot save data: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        DrawShape drawShape =(DrawShape)findViewById(R.id.canvas);


        if(id == R.id.lock_button){
            drawShape.setLock(true);
        }
        else if(id == R.id.unLock_button){
            drawShape.unLock(false);
        }
        else if (id == R.id.ball_button){
            drawShape.addBall();

        }
        else {
            drawShape.stop();
        }
    }
}
