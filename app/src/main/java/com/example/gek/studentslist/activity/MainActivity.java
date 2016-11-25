package com.example.gek.studentslist.activity;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.receivers.AudioReceiver;
import com.example.gek.studentslist.receivers.BluetoothReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnListView, btnRecycleView, btnReceivers, btnContacts;
    AudioReceiver mAudioReceiver;
    BluetoothReceiver mBluetoothReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListView = (Button)findViewById(R.id.btnListView);
        btnListView.setOnClickListener(this);

        btnRecycleView = (Button)findViewById(R.id.btnRecycleView);
        btnRecycleView.setOnClickListener(this);

        btnReceivers = (Button)findViewById(R.id.btnReceivers);
        btnReceivers.setOnClickListener(this);

        btnContacts = (Button)findViewById(R.id.btnContacts);
        btnContacts.setOnClickListener(this);

        mAudioReceiver = new AudioReceiver();
        mBluetoothReceiver = new BluetoothReceiver();

        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mAudioReceiver, filter);

        IntentFilter filterB = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        registerReceiver(mBluetoothReceiver, filterB);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnListView:
                if (!isOnline()) {
                    Toast.makeText(this, getResources().getString(R.string.no_internet),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intentLV = new Intent(this, ListViewActivity.class);
                startActivity(intentLV);
                break;
            case R.id.btnRecycleView:
                if (!isOnline()) {
                    Toast.makeText(this, getResources().getString(R.string.no_internet),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intentRV = new Intent(this, RecyclerViewActivity.class);
                startActivity(intentRV);
                break;
            case R.id.btnReceivers:
                Intent intentRsv = new Intent(this, ReceiversActivity.class);
                startActivity(intentRsv);
                break;
            case R.id.btnContactsLoaders:
                Intent intentContLoaders = new Intent(this, ContactsLoadersActivity.class);
                startActivity(intentContLoaders);
                break;
            case R.id.btnContacts:
                Intent intentCont = new Intent(this, ContactsActivity.class);
                startActivity(intentCont);
                break;
        }
    }

    /** Проверяем есть ли интернет */
    public boolean isOnline() {
        // Обращаемся к сервису, который отвечает за соединение с интернетом
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }
}
