package com.example.gek.studentslist.data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Лоадер делает запрос имен в Contacts.CONTENT_URI, что бы получить имена, ИД и URI иконки.
 * Для получения телефона делается отдельный запрос с указанием ID контакта. Каждый сформированный
 * контакт добавляем в список и в конце возвращаем в активити
 */

public class ContactsDataLoader extends AsyncTaskLoader<List<User>> {

    public ContactsDataLoader(Context context) {
        super(context);
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
        Context context = getContext();
        List<User> users = new ArrayList<>();
        final ContentResolver cr = context.getContentResolver();

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
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));
                String photoIcon = cursor.getString(cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));
                long contactId = cursor.getLong(cursor.getColumnIndex(Contacts._ID));

                final Cursor cursorPhone = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PHONES_PROJECTION,
                        PHONE_SELECTION,
                        new String[]{String.valueOf(contactId)},
                        null);
                String phone = null;
                if (cursorPhone != null) {
                    if (cursorPhone.moveToFirst()) {
                        phone = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    cursorPhone.close();
                }
                User user = new User();
                user.setName(name);
                if (photoIcon != null) {
                    final Uri photo = Uri.parse(photoIcon);
                    user.setIcon(photo);
                }
                user.setPhone(phone);
                users.add(user);
            }
            cursor.close();
        }
        return users;
    }
}
