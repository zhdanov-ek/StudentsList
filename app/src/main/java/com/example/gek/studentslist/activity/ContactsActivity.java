package com.example.gek.studentslist.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.adapters.ContactsAdapter;
import com.example.gek.studentslist.data.ContactsDataLoader;
import com.example.gek.studentslist.data.User;

import java.util.List;

/**
 * Created by gek on 29.11.2016.
 */

public class ContactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>>{
    private static final int ID_LOADER_READ_PB = 55;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(ID_LOADER_READ_PB, null, this).forceLoad();
        recyclerView = (RecyclerView)findViewById(R.id.rvContacts);

    }


    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new ContactsDataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        recyclerView.setAdapter(new ContactsAdapter(data));
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }

}
