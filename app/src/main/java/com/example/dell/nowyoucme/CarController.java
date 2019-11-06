package com.example.dell.nowyoucme;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class CarController extends AppCompatActivity  {


    Button right,left,up,down,brake;
    SeekBar seekBar;
    int speed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_controller);
        ini();

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(MainActivity.connected){

                    MainActivity.bluetoothOperation("R\n",MainActivity.btdevice);
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(MainActivity.connected){

                    MainActivity.bluetoothOperation("L\n",MainActivity.btdevice);

                }
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(MainActivity.connected){

                    MainActivity.bluetoothOperation("U\n",MainActivity.btdevice);
                    MainActivity.bluetoothOperation(speed+"\n",MainActivity.btdevice);

                }
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.connected){

                    MainActivity.bluetoothOperation("D\n",MainActivity.btdevice);
                    MainActivity.bluetoothOperation("B\n",MainActivity.btdevice);

                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                 speed= seekBar.getProgress();
            }
        });

        brake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.bluetoothOperation("B\n",MainActivity.btdevice);
            }
        });
    }

    void ini(){

        right=(Button)findViewById(R.id.b_right);
        left=(Button)findViewById(R.id.b_left);
        brake=(Button)findViewById(R.id.b_brake);
        up=(Button)findViewById(R.id.b_up);
        down=(Button)findViewById(R.id.b_down);
        seekBar=(SeekBar) findViewById(R.id.seek);
        seekBar.setMax(255);
        seekBar.setProgress(0);



    }

}
