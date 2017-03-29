/*
* Developed by Antonio Ramos and Gary Larson
* These are the 4 additional features
* 1) save/load drawing
*   feature will allow user to save current drawing when onPause is active and redraw canvas when
*   onResume is active if selected from menu user can save and load files they name
*       methods used- MainActivity(readData, saveData, onPause, onResume, onOptionsItemSelected,
*       saveFile, loadFile) DrawShape (getMyList, setList)
*
* 2) colorpicker
*   feature allows you to select a color from a color heel image
*       methods used MainActivity (onOptionsItemSelected, onActivityResult)
*           DrawShape (setColor) ColorActivity (all)
* 3) lock/unlock starting point in place
*    feature will lock starting point in place and allow user to draw different shape with the
*    same starting point
*       methods used-MainActivity (onClick) DrawShape (unLock, setLock, lockPoint(flag))
* 4) Draw with ball
*    feature will display animated ball and then draw ball's path
*       methods used-MainActivity (onClick) DrawShape (addBall, stop, clearPointArray, ArrayLists,
*       class AnimateThread,class Ball, onSizeChanged, Animated ball- created by Professor John Nicholson
 *       modified by-Antonio Ramos)
* 5)clear all and clear last
*   feature will allow user to delete all drawings from canvas when selecting clear from menu.
*   Allow user to delete last drawn item when clicking on Clear Last button.
*       Methods used MainActivity (onOptionsItemSelected, onClick) Draw(clearOne, clearList,ClearPointArray,
*           clearCurrentPoints
*
 */
package com.antonioramos.canvas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private static final String NAME_FILE = "filename.txt";
    private ArrayList<MyShape> shapes = new ArrayList<>();
    private ArrayList<String> fileList = new ArrayList<>();
    private DrawShape drawShape;
    public static String shapeType = "line";

    private static final int COLOR_RESULT = 110;
    public static final String COLOR_CHOICE_KEY = "currentColor";
    private int [] buttonId = {R.id.lock_button,R.id.ball_button,R.id.clearLast_button};
    private boolean checkLock;
    private boolean checkBall;


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
            return true;
            /*
            Created by Gary
            sets ShapeType to rectangle
             */
        } else if (id == R.id.action_rectangle) {
            shapeType = "rectangle";
            return true;
            /*
            Created by Gary
            sets ShapeType to line
             */
        } else if (id == R.id.action_line) {
            shapeType = "line";
            return true;
            /*
            Created by Gary
            loads About Activity
             */
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
            /*
            Created by Gary
            clears shapes
             */
        } else if (id == R.id.action_clear) {
            DrawShape drawShape = (DrawShape) findViewById(R.id.canvas);
            drawShape.ClearList();
            return true;
            /*
            Created by Gary
            saves shapes to file named by user
            */
        } else if (id == R.id.action_save) {
            saveFile();
            return true;
            /*
            Created by Gary
            loads a previously saved file
            */
        } else if (id == R.id.action_load) {
            loadFile();
            return true;
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
    modified by Gary
    */
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList al = readData(DATA_FILE);
        if (al != null) {
            shapes = (ArrayList<MyShape>) al;
            DrawShape drawShape = (DrawShape) findViewById(R.id.canvas);
            drawShape.setList(shapes);
        }
    }

    /*
    Created by Gary
    allows the user to save file to a specific name
     */
    private void saveFile (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_dialog_title);
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(et);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String fileName = et.getText().toString() + ".txt";
                fileList = (ArrayList<String>) readData (NAME_FILE);
                if (fileList == null) {
                    fileList = new ArrayList<>(0);
                }
                if (!fileList.contains(fileName)) {
                    drawShape = (DrawShape) findViewById(R.id.canvas);
                    shapes = drawShape.getMyList();
                    saveData(fileName, shapes);
                    fileList.add(fileName);
                    saveData(NAME_FILE, fileList);
                } else {
                    Toast.makeText(MainActivity.this, "File Name Used", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /*
    Created by Gary
    allows the user to load a file previously saved by filename
     */
    private void loadFile() {
        fileList = (ArrayList<String>) readData (NAME_FILE);
        if (fileList.isEmpty()) {
            Toast.makeText(MainActivity.this, "No Files have been saved", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.save_dialog_title);
            String sa[] = new String[fileList.size()];
            sa = fileList.toArray(sa);
            builder.setItems(sa, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    ArrayList al = readData(fileList.get(i));
                    if (al != null) {
                        shapes = (ArrayList<MyShape>) al;
                        DrawShape drawShape = (DrawShape) findViewById(R.id.canvas);
                        drawShape.setList(shapes);
                    }
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    /*
    method will retrieve ArrayList data from a file and load ArrayList<MyShape>  -by Antonio
    */
    public ArrayList readData(String fileName){
        ArrayList al = null;
        try {
            Log.i("READ", "entered read");
            FileInputStream fis = openFileInput(fileName);
            ObjectInputStream objectInStream = new ObjectInputStream(fis);
            al =  (ArrayList) objectInStream.readObject();

            fis.close();
        }catch (FileNotFoundException e) {
            Log.i("INFO", "---------- Read Exception");
            // ok if file does not exist
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("READ", "Can not find Data: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return al;
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawShape = (DrawShape) findViewById(R.id.canvas);
        shapes = drawShape.getMyList();
        saveData(DATA_FILE, shapes);
    }
    /*
   method will retrieve ArrayList<MyShape> from DrawShape class and save data to a file -by Antonio
   modified by Gary
   */
    public void saveData(String filename, ArrayList al) {
      //  drawShape = (DrawShape) findViewById(R.id.canvas);
      //  shapes = drawShape.getMyList();

        try {

            FileOutputStream fis = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fis);
            objectOutputStream.writeObject(al);
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
           if(!checkLock) {
               checkLock = true;
               drawShape.setLock(checkLock);
           }
            else{
               checkLock =false;
               drawShape.unLock(checkLock);
           }
        }
        else if (id == R.id.ball_button){
            if(!checkBall) {
                checkBall = true;
                drawShape.addBall();
            }
            else{
                checkBall = false;
                drawShape.stop();
            }

        }
        else {
            drawShape.clearOne();;
        }
    }
}
