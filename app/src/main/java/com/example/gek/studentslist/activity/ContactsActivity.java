package com.example.gek.studentslist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gek.studentslist.R;
import com.example.gek.studentslist.adapters.ContactsAdapter;
import com.example.gek.studentslist.data.ContactsDataLoader;
import com.example.gek.studentslist.data.User;
import com.example.gek.studentslist.dialogs.AddContactDialog;

import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Чтение контактов из телефона с помощью лоадера AsyncTaskLoader.
 * Проверка разрешений для API 23.
 * Добавление контакта в телефон через DialogFragment
 */

//todo После добавления контакта на апи 23 прога упала, проверить все ли телефоны отображаются

public class ContactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>>{
    private static final int ID_LOADER_READ_PB = 55;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    private static final int READ_CONTACTS_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);

        // включит только после того как получим доступ к контактам на чтение
        fabAdd.setEnabled(false);

        recyclerView = (RecyclerView)findViewById(R.id.rvContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        // Проверяем если разрешения на чтения книги
        if (ActivityCompat.checkSelfPermission(this, READ_CONTACTS) == PERMISSION_GRANTED) {
            // если есть то запускаем лоадер, читающий контакты
            loadContacts();
            fabAdd.setEnabled(true);

        // Если разрешения нет то просим юзера его добавить или же информируем как включить его в настройках
        } else {
            // Проверяем нужно ли запрашивать разрешение с пояснениями на получения разрешений
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)) {
                // Показываем диалоговое окно с пояснениями о добавлении разрешений
                // А также двумя способами включения разрешений
                showExplanationDialog();
            } else {
                // Просто вызываем окно на получение разрешения
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{READ_CONTACTS},
                        READ_CONTACTS_REQUEST);
            }
        }

        // Перед запуском окна на добавление нового контакта проверяем разрешения
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ContactsActivity.this, WRITE_CONTACTS) ==
                        PERMISSION_GRANTED){
                    new AddContactDialog().show(getSupportFragmentManager(), "add_dialog");
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this, WRITE_CONTACTS )){
                        showExplanationDialog();
                    } else {
                        showSnackToSettingsOpen();
                    }
                }
            }
        });
    }

    // Запуск лоадера на загрузку контактов
    public void loadContacts(){
        getSupportLoaderManager().initLoader(ID_LOADER_READ_PB, null, this).forceLoad();
    }

    // Обновление списка
    public void updateUI(List<User> users) {
        progressBar.setVisibility(View.GONE);
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new ContactsAdapter(users));
        } else {
            ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
            adapter.updateAdapter(users);
        }
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
        updateUI(data);
    }


    // Лоадер прервал свою работу. Удаляем адаптер со списка
    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        progressBar.setVisibility(View.GONE);
        updateUI(null);
    }


    // Метод принимает результат выбора разрешений юзером после вызова ActivityCompat.requestPermissions
    // Проверяем было ли полученно разрешение при соответствующем запросе и выполняем процедуру
    // в случае с положительным ответом и вновь вызываем диалог в случае если разрешения нет.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case READ_CONTACTS_REQUEST:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    loadContacts();
                    fabAdd.setEnabled(true);
                } else {
                    // Если возвращается тут false то уже поставили галочку "не спрашивать"
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this, READ_CONTACTS )) {
                        showSnackToSettingsOpen();
                    }
                }
                break;
        }
    }


    // Диалог запроса разрешения, где юзер выбирает заправшивать разрешения или запускать настройки
    private void showExplanationDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.read_contacts_err)
                // Запрос разрешиния
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                ContactsActivity.this,
                                new String[] {READ_CONTACTS},
                                READ_CONTACTS_REQUEST);
                    }
                })
                // запуск окна с настройками программы через интент
                .setNegativeButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openPermissionSettings();
                    }
                })
                .create()
                .show();
    }


    // Когда разрешения можно будет включит только через настройки бросаем этот тост
    private void showSnackToSettingsOpen(){
        Snackbar.make(recyclerView, R.string.permissions_for_contacts_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPermissionSettings();
                    }
                })
                .show();
    }


    // Запуск окна с настройками разрешения программы
    private void openPermissionSettings() {
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

}
