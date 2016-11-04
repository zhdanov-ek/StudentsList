/**
 * Активити демонстрирующая работу ListView
 */

package com.example.gek.studentslist.activity;

import com.example.gek.studentslist.data.*;
import com.example.gek.studentslist.adapters.*;
import com.example.gek.studentslist.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


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
