package com.example.dell.nowyoucme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    LinearLayout layout;
    SensorManager manager;
    boolean color=false;
    boolean tag=false;
    long lastUpdate;
    static boolean connected=false;
    private BluetoothAdapter BA;
    static Button connect_b,cs,sw;
    private Set<BluetoothDevice> pairedDevices;
    public static Handler myHandler=new Handler();
    public static BluetoothSocket socket;
    static BluetoothDevice btdevice;
    String op="";


    public static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout) findViewById(R.id.activity_main);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        connect_b = (Button) findViewById(R.id.connect);
        cs = (Button) findViewById(R.id.cs);
        connect_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BA = BluetoothAdapter.getDefaultAdapter();


                if (!connected) {
                    if (!BA.isEnabled()) turnOnBt();
                    connectToDevice();
                    Log.d("Nazim", "nitro");
                } else turnOffBt();
            }
        });

        cs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                startActivity(new Intent(MainActivity.this, CarController.class));
                if (connected && tag) {
                    //   bluetoothOperation("x",btdevice);
                    Log.d("Nazim", "nitro");
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) tag = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) tag = false;

                return false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            lastUpdate = System.currentTimeMillis();
            getAccelerometer(event);

        }
    }
    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;

        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
       // Log.d("Nazim","y"+y);

        long actualTime = event.timestamp;

        if(actualTime-lastUpdate>700){
            if(y>3 && connected ) {

                bluetoothOperation("r",btdevice);
                Log.d("Nazim","right");
            }
            else if (y<-3 && connected ) {

                bluetoothOperation("l",btdevice);
                Log.d("Nazim","left");
            }

            else  {

              if(!tag && connected) {

                      bluetoothOperation("0",btdevice);
                     Log.d("Nazim","origin");
              }
            }
            lastUpdate = actualTime;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume() {
        super.onResume();

        manager.registerListener(this,
                manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        manager.unregisterListener(this);
    }

    void shuffle(SensorEvent event,float x,float y,float z){

        float accelationSquareRoot = (float) ((x * x + y * y + z * z)
                / (9.81 * 9.81));
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2 &&(actualTime-lastUpdate>200)) //
        {

            //
            lastUpdate = actualTime;
            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
                    .show();

            if (color) {
                layout.setBackgroundColor(Color.GREEN);
            } else {
                layout.setBackgroundColor(Color.RED);
            }
            color = !color;

        }

    }
    static void myToast(Context context,String msg){

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static void bluetoothOperation(String op,BluetoothDevice bt){

        Log.d("Nazim",""+connected);
        if(connected){

            ThreadCommunication comm=new ThreadCommunication(socket,op);
            comm.write(op);
        }

        else{
            ThreadConnection connection=new ThreadConnection(bt,op);
            connection.start();
        }

    }
    void connectToDevice(){
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            if (bt.getName().equals("HC-05")) {
                btdevice = bt;
                Log.d("Nazim", "detected");
            }

        }
        if (btdevice != null) {
            ThreadConnection connection = new ThreadConnection(btdevice,op);
            connection.start();

        }
    }
    public void turnOnBt(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turning On Bluetooth",Toast.LENGTH_LONG).show();
        }
    }
    void turnOffBt(){

        if(BA.isEnabled() && connected){
            connected=false;
            try{
                socket.close();
            }catch (Exception e){
            }
        }

    }
}
