package com.example.gek.studentslist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

/**
 * Отлавливаем уведомление о подключении/отключении наушников.
 * Этот ресивер регистрируется в самом коде программы во время ее запуска
 */

public class AudioReceiver extends BroadcastReceiver {
    public static Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mContext == null) mContext = context;

        int state = intent.getIntExtra("state", -1);
        switch (state) {
            case 0:
                Toast.makeText(mContext, "Headset is unplugged", Toast.LENGTH_LONG).show();

                break;
            case 1:
                Toast.makeText(mContext, "Headset is plugged", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

    }
}
