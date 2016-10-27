package com.example.gek.studentslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by gek on 27.10.16.
 */

public class MyRecyclerViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView)

//http://www.fandroid.info/primer-ispolzovaniya-cardview-i-recyclerview-v-android/


    }
}
