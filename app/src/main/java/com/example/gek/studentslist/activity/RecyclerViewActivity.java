/**
 * Активити демонстрирующее работу с RecyclerView
 */

package com.example.gek.studentslist.activity;

import com.example.gek.studentslist.data.*;
import com.example.gek.studentslist.adapters.*;
import com.example.gek.studentslist.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class RecyclerViewActivity extends AppCompatActivity {
    private Realm realm;
    private final static String TAG = "1111";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        // Добавляем тулбар бар
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBar);
        myToolbar.setTitle(R.string.title_recylcer_view);
        setSupportActionBar(myToolbar);

        DataStudents dataStudents = new DataStudents();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);



        // Задаем стандартный менеджер макетов
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получаем доступ к нашему реалму. Этот риалм нужно закрыать после выполнения
        // необходимых операций и не открывать лишних
        realm = Realm.getDefaultInstance();

        // Получаем список всех объектов Student. По сути это ссылки на объекты в БД
        RealmResults<Student> students = realm.where(Student.class).findAll();

        if ((students != null) && (students.size() > 0)){
            Log.i(TAG, "Student list FINDED! ");
            for (Student s: students){
                Log.i(TAG, "Student: " + s.getName());
            }
        } else {
            if (loadStudentsToRealm()){
                Log.i(TAG, "Student list LOADED! ");
                for (Student s: students){
                    Log.i(TAG, "Student: " + s.getName());
                }
            }
        }

        // Создаем адаптер
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dataStudents.getListStudents());
        recyclerView.setAdapter(adapter);
    }

    private Boolean loadStudentsToRealm(){
        DataStudents dataStudents = new DataStudents();
        List<Student> students = dataStudents.getListStudents();
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(students);
            realm.commitTransaction();
            return true;
        } catch (Exception e){
            Log.d(TAG, "loadStudentsToRealm: " + e);
            return false;
        }
    }

    // Указываем как нам формировать меню и описываем виджет SearchView
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.ab_search);

        SearchView searchView =(SearchView) MenuItemCompat.getActionView(searchItem);

        // Отрабатываем смену текста в окне поиска
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Реакция на команду ввода (Enter)
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // Непосредственно событие смены содержимого. Делаем запрос к БД по каждому изменению
            // в окне поиска
            @Override
            public boolean onQueryTextChange(String newText) {
                // Делаем выборку из БД после чего проверяем есть ли результат. Если нет то
                // делаем выборку всех слов
                Log.i(TAG, "onQueryTextChange: " + newText);
//                Cursor cursor = db.getAllData(Consts.LIST_TYPE_SEARCH, Consts.ORDER_BY_ABC, newText);
//                if ((cursor == null) || (cursor.getCount() == 0)) {
//                    cursor = db.getAllData(Consts.LIST_TYPE_ALL, Consts.ORDER_BY_ABC, null);
//                }
//                mListWords = db.getFullListWords(cursor);
//                mAdapter = new RecyclerViewAdapter((Activity)mCtx, mListWords);
//                mRrecyclerView.setAdapter(mAdapter);
                return false;
            }
        });


        // Событие срабатывает по второму нажатию на крестик, т.е. закрытию окна поиска.
        // Первое нажатие очищает окно ввода и фактически срабатывает onQueryTextChange с пустой строкой
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.i(TAG, "onClose: Close search Widget");
                return false;
            }
        });
        return true;
    }


}
