package com.example.gek.studentslist.activity;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.provider.ContactsContract.CommonDataKinds.Phone;



import com.example.gek.studentslist.R;

import java.util.ArrayList;

/**
 * Демонстрация полученя контактов с телефонной книги и добавление новой записи в нее
 */

public class ContactsActivity extends AppCompatActivity {
    EditText etName, etPhone;
    ListView lvContacts;
    Button btnAddContact;
    ArrayList<String> contactList;
    Cursor cursor;
    int counter;
    private ProgressDialog pDialog;
    private Handler updateBarHandler;
    Context mCtx;
    public static final String TAG = "GEK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mCtx = getBaseContext();

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);

        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etName.getText().toString().length() > 0) && (etPhone.getText().length() > 0)){
                    if (insertContact(mCtx.getContentResolver(),
                            etName.getText().toString(),
                            etPhone.getText().toString())) {
                        Toast.makeText(mCtx, "Contact has been added", Toast.LENGTH_SHORT).show();
                        etName.setText("");
                        etPhone.setText("");
                    } else {
                        Toast.makeText(mCtx, "Error! Contact not added", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mCtx, "Please full all fields end retry", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Выводим диалог на время загрузки контактов
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Reading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();
        updateBarHandler =new Handler();

        // Выборку запускаем в отдельном потоке
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContacts();
            }
        }).start();


        lvContacts = (ListView) findViewById(R.id.lvContacts);
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

    /** Выборка всех контактов */
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

        String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME + " ASC ";

        // Получаем список контактов
        cursor = contentResolver.query(CONTENT_URI, null,null, null, SORT_ORDER);
        final int totalCount = cursor.getCount();
        // Для каждого контакта делаем свою итерацию где ищем телефоны и емейл
        if (cursor.getCount() > 0) {
            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();
                // Обновляем прогресбар
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        pDialog.setMessage("Reading contacts : "+ counter++ +"/"+ totalCount);
                    }
                });
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                if ( !(name == null)) {
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

        else {
            // Отключаем прогресс бар если контактов нет вообще
            updateBarHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.cancel();
                }
            }, 500);
        }
    }

    /** Добавление контакта в телефон */
    public static boolean insertContact(ContentResolver contentResolver, String firstName, String mobileNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Сначала добавляем пустой контакт
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //Добавляем имя контакта
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,firstName)
                .build());

        //Добавляем телефон
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,Phone.TYPE_MOBILE)
                .build());
        try {
            // Пробуем через резолвер все записать
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}