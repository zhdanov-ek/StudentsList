package com.example.gek.studentslist.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gek.studentslist.R;

import java.util.ArrayList;

/**
 *
 */

public class ContactsActivity extends AppCompatActivity {
    ListView lvContacts;
    ArrayList<String> contactList;
    Cursor cursor;
    int counter;
    private ProgressDialog pDialog;
    private Handler updateBarHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Reading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();
        lvContacts = (ListView) findViewById(R.id.lvContacts);
        updateBarHandler =new Handler();

        // Выборку запускаем в отдельном потоке
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContacts();
            }
        }).start();

        // Клик по айтему
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TODO Do whatever you want with the list data
                Toast.makeText(getApplicationContext(),
                        "item clicked : \n"+contactList.get(position),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    public void getContacts() {
        contactList = new ArrayList<String>();
        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output;
        ContentResolver contentResolver = getContentResolver();

        // Получаем список контактов
        cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        final int totalCount = cursor.getCount();
        // Для каждого контакта делаем свою итерацию где ищем телефоны и емейл
        if (cursor.getCount() > 0) {
            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();
                // Update the progress message
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        pDialog.setMessage("Reading contacts : "+ counter++ +"/"+ totalCount);
                    }
                });
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                // Проверяем есть ли номера у контакта и если есть то делаем запрос в другую таблицу
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    output.append(" First Name: " + name);
                    // Ищем телефоны, который принадлежат текущему контакту
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI,
                            null,
                            Phone_CONTACT_ID + " = ?",
                            new String[] { contact_id },
                            null);

                    // Добавляем все найденные телефоны
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number: " + phoneNumber);
                    }
                    phoneCursor.close();

                    // Ищем емейлы текущего контакта и добавляем их если нашли
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,
                            null,
                            EmailCONTACT_ID+ " = ?",
                            new String[] { contact_id },
                            null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        output.append("\n Email: " + email);
                    }
                    emailCursor.close();
                }
                // Добавляем очередную запись контакта в список
                contactList.add(output.toString());
            }
            // Запускаем в основном потоке обновление данных в списке
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.item_contact, R.id.tvName, contactList);
                    lvContacts.setAdapter(adapter);
                }
            });
            // Отключаем прогресс бар
            updateBarHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.cancel();
                }
            }, 500);
        }
    }
}