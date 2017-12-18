package com.kth.dagdouglas.mobillaboration3a;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity  implements SensorEventListener{


    private Sensor accelerometer;


    private GraphView graphView1;
    private SensorManager sensorManager;

    LineGraphSeries<DataPoint> seriesX;
    LineGraphSeries<DataPoint> seriesY;

    LineGraphSeries<DataPoint> seriesZ;


    private DataPoint[] initX = new DataPoint[]{};


    private  int dataCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.sensor_menu);
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.preference:
                        Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        graphView1=findViewById(R.id.graph);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        seriesX= new LineGraphSeries<>(new  DataPoint[]{});
        seriesX.setColor(Color.CYAN);
        seriesY = new LineGraphSeries<>(new DataPoint[]{});
        seriesY.setColor(Color.RED);
        seriesZ = new LineGraphSeries<>(new DataPoint[]{});
        seriesZ.setColor(Color.GREEN);
        graphView1.addSeries(seriesX);
        graphView1.addSeries(seriesY);
        graphView1.addSeries(seriesZ);
        graphView1.getViewport().setXAxisBoundsManual(true);
        graphView1.getViewport().setMinX(0);
        graphView1.getViewport().setMaxX(20);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double sensorX = event.values[0];
        double sensory = event.values[1];
        double sensorz = event.values[2];
        if(dataCount>20) {
            seriesX.appendData(new DataPoint(dataCount, sensorX), true, 20);
            seriesY.appendData(new DataPoint(dataCount, sensory), true, 20);
            seriesZ.appendData(new DataPoint(dataCount, sensorz), true, 20);
        }
        dataCount++;
        Log.i("tag", "onSensorChanged: "+event.values[0]+" " +dataCount);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
