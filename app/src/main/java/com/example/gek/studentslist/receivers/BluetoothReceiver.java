package com.example.gek.studentslist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Bluetooth receiver
 * Этот ресивер регистрируется в самом коде программы во время ее запуска
 * Тут куча интент фильтров по нему
 * https://github.com/phonedeveloper/BluetoothIntentLogger/blob/master/app/src/main/AndroidManifest.xml*
 */

public class BluetoothReceiver extends BroadcastReceiver {
    public static Context mContext;

    final static int STATE_OFF = 10;
    final static int STATE_ON = 12;
    final static int STATE_TURNING_OFF = 13;
    final static int STATE_TURNING_ON = 11;
    final static String STATE = "android.bluetooth.adapter.extra.STATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;


        int state = intent.getIntExtra(STATE, 0);
        String mes = "";
        switch (state) {
            case STATE_ON:
                mes = "on";
                break;
            case STATE_OFF:
                mes = "off";
                break;
            case STATE_TURNING_ON:
                mes = "turning on";
                break;
            case STATE_TURNING_OFF:
                mes = "turning off";
                break;
        }
        if (mes.length() > 1) {
            Toast.makeText(mContext, "State bluetooth is " + mes, Toast.LENGTH_LONG).show();
        }


    }
}
