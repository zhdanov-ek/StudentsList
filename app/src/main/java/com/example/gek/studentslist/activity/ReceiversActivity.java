package com.example.gek.studentslist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gek.studentslist.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * Интерфейс работы с камерой (через интент), загрузки картинки с галереи и описания
 * встроенных в программу ресиверов, которые описаны в манифесте или загружаются в главном
 * активити
 * */

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
                // Проверяем есть ли вообще камера на устройстве
                // Через PackageManager можно получить информацию о многих возможностях
                PackageManager pm = getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    // Проверяем есть ли разрешения на камеру
                    // прежде чем ее использовать (для АПИ 23)
                    if (ActivityCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED) {
                        makePhoto();
                    } else {
                        // Если разрешения нет то просим юзера его добавить или же информируем как
                        // включить его в настройках.  Проверяем нужно ли запрашивать разрешение с
                        // пояснениями на получения разрешений
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)) {
                            // Показываем диалоговое окно с пояснениями о добавлении разрешения
                            // А также двумя способами включения разрешений
                            showExplanationDialog();
                        } else {
                            // Просто вызываем окно на получение разрешения
                            ActivityCompat.requestPermissions(
                                    this,
                                    new String[]{CAMERA},
                                    REQUEST_MAKE_PHOTO);
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.no_camera, Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }


    // Пытаемся сделать фото
    private void makePhoto() {
        Intent makePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (makePhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(makePhotoIntent, REQUEST_MAKE_PHOTO);
        } else   Toast.makeText(this, R.string.no_soft_for_use_camera, Toast.LENGTH_LONG).show();
    }


    // Обработка вызванных ранее интентов:
    //   - запрос картинки из галереи возвращает URI
    //   - запрос фото с камеры возвращает Bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null ) {
            if (requestCode == REQUEST_LOAD_IMG ) {
                Uri uriImage = data.getData();
                ivLoadImage.setImageURI(uriImage);
            } else if (requestCode == REQUEST_MAKE_PHOTO ) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivLoadImage.setImageBitmap(bitmap);
            }
        }

    }


    /** Пермишены на камеру */
    // Диалог запроса разрешения, где юзер выбирает заправшивать разрешения или запускать настройки
    private void showExplanationDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.use_camera_err)
                // Запрос разрешиния
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                ReceiversActivity.this,
                                new String[] {CAMERA},
                                REQUEST_MAKE_PHOTO);
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

    // Метод принимает результат выбора разрешений юзером после вызова ActivityCompat.requestPermissions
    // Проверяем было ли полученно разрешение при соответствующем запросе и выполняем процедуру
    // в случае с положительным ответом и вновь вызываем диалог в случае если разрешения нет.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_MAKE_PHOTO:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    makePhoto();
                } else {
                    // Если возвращается тут false то уже поставили галочку "не спрашивать"
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(ReceiversActivity.this, CAMERA )) {
                        showSnackToSettingsOpen();
                    }
                }
                break;
        }
    }

    // Когда разрешения можно будет включит только через настройки бросаем этот тост
    private void showSnackToSettingsOpen(){
        Snackbar.make(ivLoadImage, R.string.permissions_for_camera_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPermissionSettings();
                    }
                })
                .show();
    }




}
