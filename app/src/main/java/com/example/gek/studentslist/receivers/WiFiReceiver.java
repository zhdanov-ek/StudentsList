package com.example.gek.studentslist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Отлавливание события изменения сети по изменению состояний Wifi
 * android.net.wifi.supplicant.CONNECTION_CHANGE
 */

public class WiFiReceiver extends BroadcastReceiver {
    public static Context mContext;

    @Override

    public void onReceive(Context context, Intent intent) {

        if (mContext == null) mContext = context;

        // получаем доступ к системнному сервису WIFI_SERVICE, что бы иметь удобные константы для
        // изьятия данных с интента
        final WifiManager wm = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);


        // Смотрим значение ключей заданное полученной константой и отрабатываем
        if (intent.getBooleanExtra(wm.EXTRA_SUPPLICANT_CONNECTED, true)) {

            // Зная, что включился wifi проверяем еще и состояние его подключения через WifiManager
            WifiInfo info = wm.getConnectionInfo();
            Toast.makeText(context, "Wifi connected to network: \n" + info.toString(), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Wifi disconnected from network" , Toast.LENGTH_LONG).show();
        }

    }

}