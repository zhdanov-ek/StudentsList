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
import android.widget.TextView;

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



public class PersonActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvUrlProfile;
    TextView tvUrlImage;
    TextView tvAll;
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

        tvName = (TextView) findViewById(R.id.tvName);
        tvUrlProfile = (TextView) findViewById(R.id.tvUrlProfile);
        tvUrlImage = (TextView) findViewById(R.id.tvUrlImage);
        tvAll = (TextView) findViewById(R.id.tvAll);
        ivAva = (ImageView) findViewById(R.id.ivAva);
        llPersonBack = (LinearLayout) findViewById(R.id.llPersonBack);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        btnMore = (Button) findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(tvUrlProfile.getText().toString())));
            }
        });

        // Смотрим входящий интент и запрашиваем информацию с нужного источника
        Intent intent = getIntent();
        if (intent.getIntExtra(Consts.TYPE_CARD, 0) == Consts.TYPE_CARD_GOOGLE){

            // Формируем карточку с гугл
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_google));
            new LoadInfoFromServer()
                    .execute(intent.getStringExtra(Consts.ID_GOOGLE),
                            Integer.toString(Consts.TYPE_CARD_GOOGLE));
        } else {

            // Формируем карточку с ГИТ
            llPersonBack.setBackgroundColor(getResources().getColor(R.color.colorGit));
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_git));
            new LoadInfoFromServer()
                    .execute(intent.getStringExtra(Consts.ID_GIT),
                            Integer.toString(Consts.TYPE_CARD_GIT));
        }

    }


    /** Загружает с инфу с Google Plus или GIT
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

                    // Загружаем инфу с сервера, а результат сразу парсим формируя ContentValues
                    result = parseJsonGoogle(loadJsonFromServer(uriGoogle));
                    break;

                case Consts.TYPE_CARD_GIT:
                    Uri uriGit = Uri.parse(Consts.URL_GIT_BASE + idUser).buildUpon()
                            .build();
                    // Загружаем инфу с сервера, а результат сразу парсим формируя ContentValues
                    result = parseJsonGit(loadJsonFromServer(uriGit));
                    break;

                default:
                    break;
                }
            return  result;
            }


        @Override
        protected void onPostExecute(ContentValues cv) {
            ImageLoadTask  loadImageTask =
                    new ImageLoadTask(cv.getAsString(Consts.FIELD_URL_IMAGE), ivAva);
            loadImageTask.execute();

            tvName.setText(cv.getAsString(Consts.FIELD_NAME));
            tvUrlProfile.setText(cv.getAsString(Consts.FIELD_URL_PROFILE));
            tvUrlImage.setText(cv.getAsString(Consts.FIELD_URL_IMAGE));
            btnMore.setEnabled(true);

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
                cv.put(Consts.FIELD_NAME, jsonMain.getString("name"));
                cv.put(Consts.FIELD_GIT_REPOS, jsonMain.getString("public_repos"));
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
