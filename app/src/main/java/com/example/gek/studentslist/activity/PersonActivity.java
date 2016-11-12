package com.example.gek.studentslist.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gek.studentslist.BuildConfig;
import com.example.gek.studentslist.R;
import com.example.gek.studentslist.data.Consts;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Класс принимающий на вход URL профиля пользователя на github.com или plus.google.com
 * Класс делает запрос на указанные сервера и изымает данные о пользователях с серверов
 * после чего выводит их в нужной форме в своем активити.
 *
 * Работает в двух режимах:
 * 1) Явным интеном с других активити этой программы
 * 2) Не явными интентами с других программ (интент фильтр в манифесте)
 *
 * */

public class PersonActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView tvName;
    TextView tvUrlProfile;
    TextView tvOther;
    ImageView ivAva;
    LinearLayout llPersonBack;
    ImageView ivLogo;
    Button btnMore;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ctx = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUrlProfile = (TextView) findViewById(R.id.tvUrlProfile);
        tvOther = (TextView) findViewById(R.id.tvOther);
        ivAva = (ImageView) findViewById(R.id.ivAva);
        llPersonBack = (LinearLayout) findViewById(R.id.llPersonBack);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        btnMore = (Button) findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Для отображения инфы в браузере после клика на кнопку нужен шузер ибо
                // может снова запускаться наша программа если ее выберут как основную до этого
                Intent intentMore = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(tvUrlProfile.getText().toString()));
                Intent chooser = Intent.createChooser(intentMore,
                        getResources().getString(R.string.chooser_title));
                if (intentMore.resolveActivity(getPackageManager()) != null) {
                    ctx.startActivity(chooser);
                } else {
                    Toast.makeText(ctx, "Program for open link did't found in system.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        Intent intent = getIntent();

        // Пытаемся получить параметр по которому определим как сформирован интент:
        // если он есть то значение будет 1 или 2 и это явные интенты с нашей же программы
        // если параметра нет то значение присвоится 0 и это будет неявный интент из вне
        int typeCard = intent.getIntExtra(Consts.TYPE_CARD, 0);

        // Явный интент с нашего активити с указанием карточки в Google Plus
        if ( typeCard == Consts.TYPE_CARD_GOOGLE){
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_google));
            new LoadInfoFromServer()
                    .execute(intent.getStringExtra(Consts.ID_GOOGLE),
                            Integer.toString(Consts.TYPE_CARD_GOOGLE));
        }

        // Явный интент с нашего активити с указанием карточки в GitHub
        if (typeCard == Consts.TYPE_CARD_GIT){
            llPersonBack.setBackgroundColor(getResources().getColor(R.color.colorGit));
            btnMore.setBackground(getResources().getDrawable(R.drawable.bg_button_git));
            btnMore.setTextColor(getResources().getColor(R.color.colorGit));
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_git));
            new LoadInfoFromServer()
                    .execute(intent.getStringExtra(Consts.ID_GIT),
                            Integer.toString(Consts.TYPE_CARD_GIT));
        }

        // Интент с другой программы. Изымаем URL, парсим его и пытаемся получить инфу
        if (typeCard == 0) {
            // Изымаем переменную, которая хранит в себе данные по интенту
            Uri data = getIntent().getData();

            // В нашем случае это полный URL путь типа: https://github.com/zhdanov-ek
            String fullPath = data.toString();

            // Возвращает имя хоста: plus.google.com или github.com
            String host = data.getHost();

            // Смотрим на хост и обрабатывем ссылку соответствующим образом
            if (host.contentEquals(Consts.URL_GOOGLE_HOST)) {
                String idUserGoogle = data.getLastPathSegment();
                new LoadInfoFromServer()
                        .execute(idUserGoogle,
                                Integer.toString(Consts.TYPE_CARD_GOOGLE));
            }

            if (host.contentEquals(Consts.URL_GIT_HOST)) {
                llPersonBack.setBackgroundColor(getResources().getColor(R.color.colorGit));
                btnMore.setBackground(getResources().getDrawable(R.drawable.bg_button_git));
                btnMore.setTextColor(getResources().getColor(R.color.colorGit));
                ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_git));

                // Изымаем из URL последнюю часть где указан юзер и подаем это значение дальше
                String idUserGit = data.getLastPathSegment();

                new LoadInfoFromServer()
                        .execute(idUserGit,
                                Integer.toString(Consts.TYPE_CARD_GIT));
            }
        }

    }


    /** Загружает инфу с Google Plus или GIT
     * Параметры на вход таску:
     * 1. ID account Google or GIT
     * 2. Сервер: TYPE_CARD_GOOGLE or TYPE_CARD_GIT
     * */
     private class LoadInfoFromServer extends AsyncTask<String, Integer, ContentValues>{
        private final String LOG_TAG = "MyLog LoadInfoGoogle: ";

        @Override
        protected ContentValues doInBackground(String... params) {
            String idUser = params[0];
            int typeCard = Integer.parseInt(params[1]);
            ContentValues result = null;

            switch (typeCard){
                case Consts.TYPE_CARD_GOOGLE:
                    // указываем поля, которые хотим получить. Без указания получим все
                    final String FIELDS_GOOGLE = "image,name,organizations,url,urls";
                    final String KEY_GOOGLE = BuildConfig.GOOGLE_API_KEY;
                    Uri uriGoogle = Uri.parse(Consts.URL_GOOGLE_BASE + idUser).buildUpon()
                            .appendQueryParameter("fields", FIELDS_GOOGLE)
                            .appendQueryParameter("key", KEY_GOOGLE)
                            .build();

                    // Загружаем инфу с сервера, а результат парсим формируя ContentValues
                    // Если данные есть то парсим их, иначе возвращаем выше ошибку
                    String jsonGoogle = loadJsonFromServer(uriGoogle);
                    if (jsonGoogle != null) {
                        result = parseJsonGoogle(jsonGoogle);
                    } else {
                        ContentValues cvError = new ContentValues();
                        cvError.put(Consts.FIELD_RESULT_STATUS,
                                getResources().getString(R.string.error_in_link));
                        result = cvError;
                    }
                    break;

                case Consts.TYPE_CARD_GIT:
                    Uri uriGit = Uri.parse(Consts.URL_GIT_BASE + idUser).buildUpon()
                            .build();

                    // Загружаем инфу с сервера, а результат парсим формируя ContentValues
                    // Если данные есть то парсим их, иначе возвращаем выше ошибку
                    String jsonGit = loadJsonFromServer(uriGit);
                    if (jsonGit != null) {
                        result = parseJsonGit(jsonGit);
                    } else {
                        ContentValues cvError = new ContentValues();
                        cvError.put(Consts.FIELD_RESULT_STATUS,
                                getResources().getString(R.string.error_in_link));
                        result = cvError;
                    }
                    break;

                default:
                    break;
                }
            return  result;
            }


        @Override
        protected void onPostExecute(ContentValues cv) {
            // Проверяем чем закончилась загрузка данных с сервера
            if (cv.containsKey(Consts.FIELD_RESULT_STATUS) &&
                    cv.getAsString(Consts.FIELD_RESULT_STATUS)
                            .contentEquals(Consts.RESULT_STATUS_PARSING_OK)) {
                // Сразу начинаем грузить в таске нашу картинку и заполняем поля значениями
                ImageLoadTask  loadImageTask =
                        new ImageLoadTask(cv.getAsString(Consts.FIELD_URL_IMAGE), ivAva);
                loadImageTask.execute();
                tvName.setText(cv.getAsString(Consts.FIELD_NAME));
                tvUrlProfile.setText(cv.getAsString(Consts.FIELD_URL_PROFILE));
                tvUrlProfile.setVisibility(View.GONE);

                if (cv.containsKey(Consts.FIELD_GIT_REPOS)) {
                    tvOther.setText(getString(R.string.have_repositories_on_git) +
                            " " + cv.getAsString(Consts.FIELD_GIT_REPOS));
                }
                btnMore.setEnabled(true);

            } else {
                btnMore.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                ivAva.setVisibility(View.GONE);
                tvOther.setText(R.string.error_in_link);
            }


        }

        // Делам запрос на какой либо сервер и возвращаем ответ в виде строки
        private String loadJsonFromServer(Uri uri) {

            try {
                URL url = new URL(uri.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder stringBuffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                String line;
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                }
                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Парсим ответ с гугла
        private ContentValues parseJsonGoogle(String jsonStr){
            try {
                ContentValues cv =  new ContentValues();
                JSONObject jsonMain = new JSONObject(jsonStr);

                // В гугле имя и фамилия в разных полях. Сначала их собераем в единое
                String givenName = jsonMain.getJSONObject("name").getString("givenName");
                String familyName = jsonMain.getJSONObject("name").getString("familyName");
                cv.put(Consts.FIELD_NAME, familyName + " " + givenName);
                cv.put(Consts.FIELD_URL_PROFILE, jsonMain.getString("url"));

                // В линке на картинку меняем параметр отвечающий за размер
                String urlImage = jsonMain.getJSONObject("image").getString("url");
                urlImage = urlImage.replace("sz=50", "sz=500");
                cv.put(Consts.FIELD_URL_IMAGE, urlImage);
                cv.put(Consts.FIELD_RESULT_STATUS, Consts.RESULT_STATUS_PARSING_OK);
                return cv;

            } catch (JSONException e){
                Log.e(LOG_TAG, "parseJsonGoogle()", e);
                return null;
            }

        }

        // Парсим ответ с гита
        private ContentValues parseJsonGit(String jsonStr){
            try {
                JSONObject jsonMain = new JSONObject(jsonStr);
                ContentValues cv = new ContentValues();
                cv.put(Consts.FIELD_URL_PROFILE, jsonMain.getString("html_url"));
                cv.put(Consts.FIELD_URL_IMAGE, jsonMain.getString("avatar_url"));
                String name = jsonMain.getString("name");
                if (name.equals("null")) {
                    name = jsonMain.getString("login");
                }
                cv.put(Consts.FIELD_NAME, name);
                cv.put(Consts.FIELD_GIT_REPOS, jsonMain.getString("public_repos"));
                cv.put(Consts.FIELD_RESULT_STATUS, Consts.RESULT_STATUS_PARSING_OK);
                return cv;
            } catch (JSONException e){
                Log.e(LOG_TAG, "parseJsonGit()", e);
                return null;
            }

        }
    }


    /** Подгружает в фоне картинку в ImageView
     * /todo Надо вынести в отдельный файл */
    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;
        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }

}
