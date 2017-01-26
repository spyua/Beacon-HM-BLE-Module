/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.polkapolka.team.micro;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    //Layout Variable
    private Button get_and_sent, send_btn1, send_btn2, send_btn3, send_btn4;
    private EditText input_edit1, input_edit2, input_edit3, input_edit4;
    private TextView isSerial;
    private TextView mConnectionState;
    private TextView mDataField;
    private TextView f_dist_txt,c_rssi_txt;
    private TextView get_txt;
    private Context context=this;
    private Switch auto_switch_btn;
    //Element
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private Handler mHandler;

    //BlueTooth
    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;

    //Variable
    int connect_switch=1;
    int rssi;
    int user_rssi_set;
    private double f_dist;
    private String mDeviceName;
    private String mDeviceAddress;
    private String ReceiveData;
    private String Data_Array[];
    private boolean mConnected = false;

    //Timer
    private Timer timer = new Timer(true);
    private Timer auto_timer = new Timer(true);

    //Global Function
    public static int counter=0;
    public static int[] rssiArray = new int[11];
    public final static UUID HM_RX_TX = UUID.fromString(SampleGattAttributes.HM_RX_TX);
    auto_TimerTask auto_timerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        layout_init();

        //Button Listener
        get_and_sent.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(connect_switch==1) {
                    // TODO Auto-generated method stub
                    //RSSI->Distance
                    c_rssi_txt.setText(Integer.toString(rssi));
                    f_dist = getDist(69, rssi) * 100;
                    f_dist_txt.setText(Double.toString(f_dist));
                    write_hm10_chara("R:" + Integer.toString(rssi) + "D:" + Integer.toString((int) f_dist));
                }
            }

        });


        send_btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 500);
                        // Do something
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        // No longer down
                        return true;
                }
                return false;
            }


            Runnable mAction = new Runnable() {
                @Override public void run() {
                    write_hm10_chara(input_edit1.getText().toString());
                    mHandler.postDelayed(this, 500);
                }
            };
        });

        send_btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 500);

                        // Do something
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        // No longer down
                        return true;
                }
                return false;
            }


            Runnable mAction = new Runnable() {
                @Override public void run() {

                    write_hm10_chara(input_edit2.getText().toString());
                    mHandler.postDelayed(this, 500);
                }
            };
        });

        send_btn3.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 500);

                        // Do something
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        // No longer down
                        return true;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    write_hm10_chara(input_edit3.getText().toString());
                    mHandler.postDelayed(this, 500);
                }
            };
        });

        send_btn4.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 500);

                        // Do something
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        // No longer down
                        return true;
                }
                return false;
            }


            Runnable mAction = new Runnable() {
                @Override public void run() {
                    write_hm10_chara(input_edit4.getText().toString());
                    mHandler.postDelayed(this, 500);
                }
            };

        });



        auto_switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //enabled

                    get_and_sent.setVisibility(View.INVISIBLE);
                    send_btn1.setVisibility(View.INVISIBLE);
                    send_btn2.setVisibility(View.INVISIBLE);
                    send_btn3.setVisibility(View.INVISIBLE);
                    send_btn4.setVisibility(View.INVISIBLE);
                    input_edit1.setVisibility(View.INVISIBLE);
                    input_edit2.setVisibility(View.INVISIBLE);
                    input_edit3.setVisibility(View.INVISIBLE);
                    input_edit4.setVisibility(View.INVISIBLE);
                    //auto_timer.schedule(new auto_TimerTask(), 3000, 3000);
                    display_alert();


                } else {
                    //disabled
                    get_and_sent.setVisibility(View.VISIBLE);
                    send_btn1.setVisibility(View.VISIBLE);
                    send_btn2.setVisibility(View.VISIBLE);
                    send_btn3.setVisibility(View.VISIBLE);
                    send_btn4.setVisibility(View.VISIBLE);
                    input_edit1.setVisibility(View.VISIBLE);
                    input_edit2.setVisibility(View.VISIBLE);
                    input_edit3.setVisibility(View.VISIBLE);
                    input_edit4.setVisibility(View.VISIBLE);
                    //auto_timer.purge();
                    auto_timerTask.cancel();
                }
            }
        });




    }

    private Handler Handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(connect_switch==1){
                c_rssi_txt.setText(Integer.toString(rssi));
                f_dist = getDist(69, rssi) * 100;
                f_dist_txt.setText(Double.toString(f_dist));
                write_hm10_chara("R:" + Integer.toString(rssi) + "D:" + Integer.toString((int) f_dist));
            }
        }
    };



    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;

                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                    //Get Data Response
                    ReceiveData=intent.getStringExtra(mBluetoothLeService.EXTRA_DATA);
                    get_txt.setText(ReceiveData);


            }
        }
    };

    private void clearUI() {
        ((TextView) findViewById(R.id.device_address)).setText("none");
        mConnectionState.setText("Disconnect");
        f_dist_txt.setText("none");
        c_rssi_txt.setText("none");
        get_txt.setText("none");
        isSerial.setText("NOT SERIAL");

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            DeviceControlActivity.this.finish();
            timer.cancel();
            auto_timer.cancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void layout_init(){
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        // is serial present?
        isSerial = (TextView) findViewById(R.id.isSerial);
        f_dist_txt=(TextView)findViewById(R.id.f1_dist_txt);
        c_rssi_txt=(TextView)findViewById(R.id.c_rssi_txt);
        get_txt = (TextView)findViewById(R.id.get_data_txt);

        //Button
        get_and_sent = (Button)findViewById(R.id.send_btn0);
        send_btn1 = (Button)findViewById(R.id.send_btn1);
        send_btn2 = (Button)findViewById(R.id.send_btn2);
        send_btn3 = (Button)findViewById(R.id.send_btn3);
        send_btn4 = (Button)findViewById(R.id.send_btn4);
        input_edit1 = (EditText)findViewById(R.id.input_edi1);
        input_edit2 = (EditText)findViewById(R.id.input_edi2);
        input_edit3 = (EditText)findViewById(R.id.input_edi3);
        input_edit4 = (EditText)findViewById(R.id.input_edi4);
        auto_switch_btn = (Switch)findViewById(R.id.auto_switch);
        auto_switch_btn.setChecked(false);
        Data_Array=new String[50];
        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //Timer
        timer.schedule(new MyTimerTask(), 2000, 200);

    }

    private void write_hm10_chara(String str) {
        if (characteristicTX !=null) {
            characteristicTX.setValue(str);
        } else {
            Log.d(TAG, "mBLEGattChara == null");
            return;
        }
        if (mBluetoothLeService != null) {
            mBluetoothLeService.writeCharacteristic(characteristicTX);
        } else {
            Log.d(TAG, "mBluetoothLeService == null");
            return;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
            connect_switch=1;


        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
            connect_switch=0;

        }
        return true;
    }




    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            if(counter==11){
                rssi=calculate_average_rssi();
                counter=0;
            }
            rssiArray[counter]=mBluetoothLeService.getbluetoothrssi();
            counter++;
        }
    };

    public class auto_TimerTask extends TimerTask
    {
        public void run()
        {
            Message msg = new Message();
            msg.what = 0;
            Handler.sendMessage(msg);
        }
    };
    public void display_alert() {
        int delay = 2000;
        auto_timerTask= new auto_TimerTask();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(auto_timerTask, delay, 3000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
                ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
            }
        });
    }


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

            // Check service exists for HM 10 Serial
            if(SampleGattAttributes.lookup(uuid, unknownServiceString) == "HM 10 Serial") {
                isSerial.setText("SERIAL");
            }
            else{
                isSerial.setText("NOT SERIAL");
            }
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

     		// get characteristic when UUID matches RX/TX UUID
    		 characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
    		 characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
        }
        //Open RX
        mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //DIST Formula2
    public double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0)
        {
            return -1.0;
        }
        double ratio = rssi * 1.0 / txPower;

        if (ratio < 1.0)
        {
            return Math.pow(ratio, 10);
        }
        else
        {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    //DIST Formula1
    public  float getDist(int txpower,int rssi){
        int iRssi = Math.abs(rssi);
        float power = (float) ((iRssi-txpower)/(10*2.0));
        return (float) Math.pow(10, power);
    }


    //Calculate Average Rssi
    private int calculate_average_rssi(){
        int avr_rssi = 0;
        Arrays.sort(rssiArray);
        avr_rssi=(rssiArray[4]+rssiArray[5]+rssiArray[6])/3;
        return avr_rssi;
    }

}