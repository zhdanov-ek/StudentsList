package com.example.gek.studentslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by gek on 27.10.2016.
 */

public class ListViewActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView)findViewById(R.id.listView);

        DataStudents dataStudents = new DataStudents();


        ListViewAdapter adapter = new ListViewAdapter(this, dataStudents.getListStudents());
        listView.setAdapter(adapter);

    }
}
