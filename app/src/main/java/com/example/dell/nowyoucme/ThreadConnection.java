package com.example.dell.nowyoucme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * Created by DELL on 31/03/2017.
 */

public class ThreadConnection extends Thread {
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothDevice bluetoothDev;
    private BluetoothAdapter BA;
    String op;

    public ThreadConnection(BluetoothDevice bD,String code) {
        this.bluetoothDev = bD;
        BluetoothSocket socket=null;
        op=code;

        try{
            BA=BluetoothAdapter.getDefaultAdapter();
            bluetoothDev=BA.getRemoteDevice(bluetoothDev.getAddress());
            Log.d("Nazim","bluetoothDev: "+bluetoothDev);
            socket=bluetoothDev.createInsecureRfcommSocketToServiceRecord(MainActivity.MY_UUID_SECURE);
            Log.d("Nazim","BluetoothSocket : "+socket);

        }catch (Exception e){
            e.printStackTrace();
        }
        bluetoothSocket=socket;
    }

    @Override
    public void run() {

        // BA.cancelDiscovery();

        try{

            bluetoothSocket.connect();
            MainActivity.socket=bluetoothSocket;
            MainActivity.connected=true;
            MainActivity.myHandler.post(new Runnable() {
                @Override
                public void run() {


                }
            });

        }catch (Exception e){
            MainActivity.connected=false;
            Log.d("Nazim",e.toString());

            MainActivity.myHandler.post(new Runnable() {
                @Override
                public void run() {


                }
            });
            try{
                bluetoothSocket.close();

                Log.d("Nazim","Closing thread");
            }catch (Exception ex){


                Log.d("Nazim",ex.toString());
            }
        }
    }
}
