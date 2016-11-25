package com.example.gek.studentslist.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.LoaderManager;
import android.widget.ListView;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.adapters.ContactsAdapter;

/**
 * Демонстрация использования LoaderManager. Делается выборка только с одной таблицы - имен
 * USED: CursorLoader, CursorAdapter, Contract
 *
 * https://developer.android.com/training/contacts-provider/retrieve-details.html
 */

public class ContactsLoadersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ContactsAdapter mContactsAdapter;
    ListView lvContacts;
    private static final int MY_LOADER_ID = 87;     // ID нашего лоадера


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_loaders);

        mContactsAdapter = new ContactsAdapter(this, null, 0);
        lvContacts = (ListView)findViewById(R.id.lvContacts);
        lvContacts.setAdapter(mContactsAdapter);

        // Черезе менеджер инициализируем запуск лоадера
        getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this);
    }


    // Проверяем какой лоадер был создан (на случай когда их несколько)
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        if (loaderId == MY_LOADER_ID){
            return new MyCursorLoader(this);
        } else {
            return null;
        }

    }

    // Данные на этом этапе возвращаются лоадером в виде курсора.
    // Подаем адаптеру полученные данные подменяя старый курсор
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactsAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    /**
     * Описываем наш кастомный лоадер для доступа к телефонной книке
     * */
    static class MyCursorLoader extends CursorLoader {
        private static final String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME + " ASC ";

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            // Получаем ContentResolver и делаем через него запрос к ContentProvider (ContactsContract)
            ContentResolver resolver = getContext().getContentResolver();

            // Столбцы которые хотим получить в результате запроса
            String[] projection = null;

            // Условие отбора данных: отбираем те у кого указан телефон
            String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";

            // Параметры, которые подаются в условия отбора
            String[] selectionArgs = null;


            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    SORT_ORDER);
            return cursor;
        }

    }



}
