package com.example.dell.nowyoucme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DELL on 31/03/2017.
 */

public class ThreadCommunication extends Thread {
    InputStream connectedinputStream;
    OutputStream connectedoutputStream;
    BluetoothSocket bluetoothSocket;
    String op;
    private byte[] mmBuffer;

    public ThreadCommunication(BluetoothSocket connectedbtsocket, String code) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        bluetoothSocket = connectedbtsocket;
        op = code;
        try {
            inputStream = connectedbtsocket.getInputStream();
            outputStream = connectedbtsocket.getOutputStream();

        } catch (Exception e) {
            Log.d("Nazim", e.toString());
        }

        connectedinputStream = inputStream;
        connectedoutputStream = outputStream;

    }

    @Override
    public void run() {

        mmBuffer = new byte[1024];
        int numBytes;
        while (true) {

            try {

                numBytes = connectedinputStream.read(mmBuffer);
                final String readMessage = new String(mmBuffer, 0, numBytes);
                //decoment later

                if (readMessage != "") {

                    Log.d("hello",readMessage);
                }

            } catch (IOException e) {
                Log.d("Nazim", "Input stream was disconnected", e);
                break;
            }
        }

    }

    public void write(String op) {
        // Root.tag=false;
        try {
            connectedoutputStream.write(op.getBytes());
            Log.d("Nazim", "OKKKK");
        } catch (IOException e) {
            Log.d("Nazim", e.toString());
        }

    }

}
