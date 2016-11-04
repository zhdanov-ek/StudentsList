package com.example.gek.studentslist.activity;

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

            // Формируем карточку с гугл +
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_google));
            new LoadInfoGoogle().execute(intent.getStringExtra(Consts.ID_GOOGLE));
        } else {

            // Формируем карточку с ГИТ
            llPersonBack.setBackgroundColor(getResources().getColor(R.color.colorGit));
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_git));
        }

    }


    /** Загружает с инфу с учетки Google Plus */
     private class LoadInfoGoogle extends AsyncTask<String, Integer, String[]>{
        private final String LOG_TAG = "MyLog LoadInfoGoogle: ";

        @Override
        protected String[] doInBackground(String... params) {
            final String BASE_URL = "https://www.googleapis.com/plus/v1/people/";

            // указываем поля, которые хотим получить. Без указания получим все
            final String FIELDS_GOOGLE = "image,name,organizations,url,urls";
            final String KEY_GOOGLE = BuildConfig.GOOGLE_API_KEY;
            String idUser = params[0];

            Uri uri = Uri.parse(BASE_URL + idUser).buildUpon()
                    .appendQueryParameter("fields", FIELDS_GOOGLE)
                    .appendQueryParameter("key", KEY_GOOGLE)
                    .build();

            // Загружаем инфу с сервера, а результат сразу парсим формируя массив значений
            return parseJsonGoogle(loadJsonFromServer(uri));
        }


        @Override
        protected void onPostExecute(String[] result) {
            ImageLoadTask  loadImageTask = new ImageLoadTask(result[2], ivAva);
            loadImageTask.execute();

            tvName.setText(result[0]);
            tvUrlProfile.setText(result[1]);
            tvUrlImage.setText(result[2]);
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
        private String[] parseJsonGoogle(String jsonStr){
            try {
                JSONObject jsonMain = new JSONObject(jsonStr);
                String urlGoogleProfile = jsonMain.getString("url");
                String urlImage = jsonMain.getJSONObject("image").getString("url");
                // В линке на картинку меняем параметр отвечающий за размер
                urlImage = urlImage.replace("sz=50", "sz=350");
                String name = jsonMain.getJSONObject("name").getString("givenName");
                String familyName = jsonMain.getJSONObject("name").getString("familyName");
                return new String[]{familyName + " " + name, urlGoogleProfile, urlImage};
            } catch (JSONException e){
                Log.e(LOG_TAG, "parseJsonGoogle()", e);
                return new String[]{"Error" + e};
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
