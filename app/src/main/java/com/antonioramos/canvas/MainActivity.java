package com.antonioramos.canvas;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    private static final int COLOR_RESULT = 110;
    public static final String COLOR_CHOICE_KEY = "currentColor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setSpinner(R.id.line_spinner,R.array.lineWeight_spinner);

    }
   //*****************************************************************************************
   // method to setup spinner
    //******************************************************************************************
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
        } else if (id == R.id.action_color) {
            Intent intent = new Intent(getApplicationContext(), ColorActivity.class);
            startActivityForResult(intent, COLOR_RESULT);
        }
        return super.onOptionsItemSelected(item);
    }


    //*****************************************************************************************
    // Spinner method changes line weight
   //******************************************************************************************
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        DrawShape drawShape =(DrawShape)findViewById(R.id.canvas);
        drawShape.setLineWeight(i+1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

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
}
