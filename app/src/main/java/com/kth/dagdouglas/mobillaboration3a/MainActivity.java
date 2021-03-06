package com.kth.dagdouglas.mobillaboration3a;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private final  int maxAcceleration= 12;
    private Sensor accelerometer;
    private SensorManager sm;
    private  TextView degree;

    private double xa, ya, za;

    private  double prevX,prevY,prevZ;

    private  int currentDegree,oldDegree;
    private  float currentAccel;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.graph_menu);
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gview:
                        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);

        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        degree = findViewById(R.id.d);
    }

    boolean cyan = false;
    @Override
    public void onSensorChanged(SensorEvent event) {
        prevX=xa;
        xa=filter(prevX,event.values[0]);
        prevY=ya;
        ya = filter(prevY,event.values[1]);

        prevZ = za;
        za = filter(prevZ,event.values[2]);

        currentAccel = (float) Math.sqrt(Math.pow(xa,2)+ Math.pow(ya,2)+Math.pow(za,2));

        if(currentAccel>maxAcceleration){
            if(startTime == 0) {
                startTime = System.currentTimeMillis();
            }else if(startTime+1000 >=System.currentTimeMillis() ) {
                if(cyan){
                    degree.setTextColor(Color.BLACK);
                    cyan=false;
                }else {
                    degree.setTextColor(Color.CYAN);
                    cyan=true;
                }
            }
        }
        else if((System.currentTimeMillis()-startTime)<1000){
            startTime=0;
        }

        currentDegree = (int) Math.toDegrees(Math.atan2(xa,ya));
        if(currentDegree<0){
            currentDegree +=360;
        }
        degree.setText(""+(int)filter(oldDegree,currentDegree));
        oldDegree = currentDegree;

    }
    private double filter(double preValue, double sensorValue){
        return (double) (0.8*preValue)+((1-0.8)*sensorValue);
    }

    private float getAverage(float[] list){
        float sum=0;
        for(int i=0;i<list.length;i++){
            sum +=list[i];
        }
        return sum/list.length;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
