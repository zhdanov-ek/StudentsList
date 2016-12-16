/**
 * Активити демонстрирующее работу с RecyclerView
 */

package com.example.gek.studentslist.activity;

import com.example.gek.studentslist.data.*;
import com.example.gek.studentslist.adapters.*;
import com.example.gek.studentslist.R;

import android.content.Context;
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
import io.realm.Sort;


public class RecyclerViewActivity extends AppCompatActivity {
    private Realm realm;
    private final static String TAG = "1111";
    private RecyclerView recyclerView;
    private RealmResults<Student> students;
    private Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        ctx = getBaseContext();

        // Добавляем тулбар бар
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBar);
        myToolbar.setTitle(R.string.title_recylcer_view);
        setSupportActionBar(myToolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        // Задаем стандартный менеджер макетов
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получаем доступ к нашему реалму. Этот риалм нужно закрыать после выполнения
        // необходимых операций и не открывать лишних
        realm = Realm.getDefaultInstance();

        // Получаем список всех объектов Student. По сути это ссылки на объекты в БД
        students = realm
                .where(Student.class)
                .findAllSorted("name", Sort.ASCENDING);

        if (students.size() > 0){
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
                students = realm
                        .where(Student.class)
                        .findAllSorted("name", Sort.ASCENDING);
            }
        }

        // Создаем адаптер
        if (students.size() > 0){
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, students);
            recyclerView.setAdapter(adapter);
        }

    }

    /** Загрузка записей в реалм. Выполняется один раз при первом запуске программы */
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

    /** Возвращает список студентов по маске поиска */
    private RealmResults<Student> searchUser(String name){
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Student> searchUsers = realm
                .where(Student.class)
                .contains("searchName", name.toLowerCase())
                .findAllSorted("searchName", Sort.ASCENDING);
        realm.close();
        return searchUsers;
    }

    // Указываем как нам формировать меню и описываем виджет SearchView
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.ab_search);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(searchItem);

        // Отрабатываем смену текста в окне поиска
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // Непосредственно событие смены содержимого. Делаем запрос к БД по каждому изменению
            // в окне поиска
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + newText);
                students = searchUser(newText);
                RecyclerViewAdapter rva = new RecyclerViewAdapter(ctx, students);
                recyclerView.setAdapter(rva);
                return false;
            }
        });

        return true;
    }


    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

}
