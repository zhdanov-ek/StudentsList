package com.example.gek.studentslist.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.gek.studentslist.R;

public class ReceiversActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLoadImage, btnMakePhoto;
    ImageView ivLoadImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivers);

        btnLoadImage = (Button) findViewById(R.id.btnLoadImage);
        btnLoadImage.setOnClickListener(this);

        btnMakePhoto = (Button) findViewById(R.id.btnMakePhoto);
        btnMakePhoto.setOnClickListener(this);

        ivLoadImage = (ImageView) findViewById(R.id.ivLoadImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoadImage:

                break;
            case R.id.btnMakePhoto:

                break;
            default:
                break;
        }
    }
}
