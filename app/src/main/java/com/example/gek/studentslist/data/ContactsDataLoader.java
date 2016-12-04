package com.example.gek.studentslist.data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.AsyncTaskLoader;

import com.example.gek.studentslist.activity.ContactsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Лоадер делает запрос имен в Contacts.CONTENT_URI, что бы получить имена, ИД и URI иконки.
 * Для получения телефона и почты делается отдельный запрос с указанием ID контакта. Каждый сформированный
 * контакт добавляем в список и в конце возвращаем в активити
 */

public class ContactsDataLoader extends AsyncTaskLoader<List<User>> {
    private Context ctx;

    public ContactsDataLoader(Context context) {
        super(context);
        ctx = context;
    }

    private static final String[] CONTACTS_PROJECTION = {
            Contacts._ID,
            Contacts.PHOTO_THUMBNAIL_URI,
            Contacts.DISPLAY_NAME_PRIMARY,
    };

    private static final String CONTACTS_SORT = Contacts.DISPLAY_NAME_PRIMARY + " ASC";

    private static final String CONTACTS_SELECTION = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = ?";

    private static final String[] SELECTION_ARGS = {"1"};


    private static final String[] PHONES_PROJECTION = {
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private static final String PHONE_SELECTION = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";


    @Override
    public List<User> loadInBackground() {

        List<User> users = new ArrayList<>();
        final ContentResolver cr = ctx.getContentResolver();

        // Запрашиваем все имена контактов с ID и URI иконками
        final Cursor cursor = cr.query(
                Contacts.CONTENT_URI,
                CONTACTS_PROJECTION,
                CONTACTS_SELECTION,
                SELECTION_ARGS,
                CONTACTS_SORT,
                null);

        // Перебираем каждый контакт формируя запись в User для добавления в List
        // Дополнительно запрашиваем в другой таблице телефоны по ID контакта
        if ((cursor != null) && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String phoneList = "";
                String emailList = "";
                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));
                String photoIcon = cursor.getString(cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));
                long contactId = cursor.getLong(cursor.getColumnIndex(Contacts._ID));

                // Отбираем телефоны по ID контакта
                Cursor cursorPhone = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PHONES_PROJECTION,
                        PHONE_SELECTION,
                        new String[]{String.valueOf(contactId)},
                        null);

                if ((cursorPhone != null) && cursorPhone.moveToFirst()) {
                    Boolean first = true;
                    do {
                        String phone = cursorPhone.getString(cursorPhone.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (first) {
                            phoneList += phone;
                            first = false;
                        } else {
                            phoneList += ", " + phone;
                        }
                    } while (cursorPhone.moveToNext());
                    cursorPhone.close();
                }

                // Отбираем почту по ID контакта
                Cursor cursorEmail = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{String.valueOf(contactId)},
                        null);
                if ((cursorEmail != null) && (cursorEmail.moveToFirst())) {

                    Boolean first = true;
                    do {
                        String email = cursorEmail.getString(cursorEmail.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA));
                        if (first) {
                            emailList += email;
                            first = false;
                        } else {
                            emailList += ", " + email;
                        }
                    } while (cursorEmail.moveToNext());
                    cursorEmail.close();
                }

                // формируем данные по юзеру и добавляем в общий список
                User user = new User();
                    user.setName(name);
                    if (photoIcon != null) {
                        final Uri photo = Uri.parse(photoIcon);
                        user.setIcon(photo);
                    }
                    user.setPhone(phoneList);
                    user.setEmail(emailList);
                    users.add(user);
            }
            cursor.close();
        }
        return users;
    }
}
