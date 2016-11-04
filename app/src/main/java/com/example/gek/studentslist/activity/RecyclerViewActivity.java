/**
 * Активити демонстрирующее работу с RecyclerView
 */

package com.example.gek.studentslist.activity;

import com.example.gek.studentslist.data.*;
import com.example.gek.studentslist.adapters.*;
import com.example.gek.studentslist.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        DataStudents dataStudents = new DataStudents();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        // Задаем стандартный менеджер макетов
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаем адаптер
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dataStudents.getListStudents());
        recyclerView.setAdapter(adapter);
    }
}
