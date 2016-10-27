package com.example.gek.studentslist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //todo Переделать перед сдачей на апи 17

    Button btnListView, btnRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListView = (Button)findViewById(R.id.btnListView);
        btnListView.setOnClickListener(this);

        btnRecycleView = (Button)findViewById(R.id.btnRecycleView);
        btnRecycleView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnListView:
                Intent intentLV = new Intent(this, ListViewActivity.class);
                startActivity(intentLV);
                break;
        }
    }
}
