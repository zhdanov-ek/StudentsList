package com.example.gek.studentslist.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gek.studentslist.R;

public class ReceiversActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLoadImage, btnMakePhoto;
    ImageView ivLoadImage;
    public static final int REQUEST_LOAD_IMG = 1;
    public static final int REQUEST_MAKE_PHOTO = 2;
    public static final String LOG_TAG = "MyLog";

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
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                if (photoPickerIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(photoPickerIntent, REQUEST_LOAD_IMG);
                }
                break;

            case R.id.btnMakePhoto:
                Intent makePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (makePhotoIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(makePhotoIntent, REQUEST_MAKE_PHOTO);
                }
                break;

            default:
                break;
        }
    }


    /** Intent с сылкой на фото в галерее возвращают оба наших интента, по этому
     * обработка одинаковая и сводиться к тому, что нужно извлечь путь к файлу и подать
     * его в метод загрузки картинки в ImageView
    / */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null ) {
            String operation = "";
            if (requestCode == REQUEST_LOAD_IMG ) {
                operation = "Картинка загружена с галереи";
            } else if (requestCode == REQUEST_MAKE_PHOTO ) {
                operation = "Картинка загружена с камеры";
            }
            try {
                // Получаем URI  файла с интента data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Получаем курсор
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                // Получаем абсолютный путь к файлу
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // Загрузка киртинки
                ivLoadImage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                Toast.makeText(this, operation, Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

}
