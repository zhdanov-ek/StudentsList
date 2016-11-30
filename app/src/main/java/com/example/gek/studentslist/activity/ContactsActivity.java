package com.example.gek.studentslist.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.adapters.ContactsAdapter;
import com.example.gek.studentslist.data.ContactsDataLoader;
import com.example.gek.studentslist.data.User;
import com.example.gek.studentslist.dialogs.AddContactDialog;

import java.util.List;

/**
 * Created by gek on 29.11.2016.
 */

public class ContactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>>{
    private static final int ID_LOADER_READ_PB = 55;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddContactDialog().show(getSupportFragmentManager(), "add_dialog");
            }
        });


        // запускаем лоадер
        getSupportLoaderManager().initLoader(ID_LOADER_READ_PB, null, this).forceLoad();
        recyclerView = (RecyclerView)findViewById(R.id.rvContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // При инициализации лоадера через менеджер отрабатывает этот метод где и запускается сам лоадер
    // если он не выполняется в текущий момент
    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new ContactsDataLoader(this);
    }

    // Лоадер завершил свою работу и в data возвратился результат его работы
    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        adapter = new ContactsAdapter(data);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        progressBar.setVisibility(View.GONE);

    }

}
