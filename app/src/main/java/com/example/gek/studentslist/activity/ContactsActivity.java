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
 * Load contacts from phone and add new contact in phone
 */

public class ContactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ContactsAdapter mContactsAdapter;
    ListView lvContacts;
    private static final int MY_LOADER_ID = 87;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mContactsAdapter = new ContactsAdapter(this, null, 0);
        lvContacts = (ListView)findViewById(R.id.lvContacts);
        lvContacts.setAdapter(mContactsAdapter);
        getSupportLoaderManager().initLoader(MY_LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this);
    }

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
        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            // Get the ContentResolver which will send a message to the ContentProvider.
            ContentResolver resolver = getContext().getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            return cursor;
        }

    }



}
