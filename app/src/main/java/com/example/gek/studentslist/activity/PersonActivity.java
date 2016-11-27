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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gek.studentslist.BuildConfig;
import com.example.gek.studentslist.R;
import com.example.gek.studentslist.data.Consts;
import com.example.gek.studentslist.retrofits.ResponseGit;
import com.example.gek.studentslist.retrofits.ResponseGoogle;
import com.example.gek.studentslist.retrofits.ServiceGit;
import com.example.gek.studentslist.retrofits.ServiceGoogle;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    ImageView ivAva, ivGender;
    LinearLayout llPersonBack;
    ImageView ivLogo;
    Button btnMore;
    private static final String TAG = "11111";

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
        ivGender = (ImageView) findViewById(R.id.ivGender);

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
        if (typeCard == Consts.TYPE_CARD_GOOGLE){
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_google));

            // грузим через ретрофит
            loadGoogleInfo(intent.getStringExtra(Consts.ID_GOOGLE),
                    Consts.FIELDS_GOOGLE_FOR_REQUEST,
                    BuildConfig.GOOGLE_API_KEY);
        }

        // Явный интент с нашего активити с указанием карточки в GitHub
        if (typeCard == Consts.TYPE_CARD_GIT){
            llPersonBack.setBackgroundColor(getResources().getColor(R.color.colorGit));
            btnMore.setBackground(getResources().getDrawable(R.drawable.bg_button_git));
            btnMore.setTextColor(getResources().getColor(R.color.colorGit));
            ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_git));

            // грузим через ретрофит
            loadGitInfo(intent.getStringExtra(Consts.ID_GIT));
        }

        // Интент с другой программы. Изымаем URL, парсим его и пытаемся получить инфу
        if (typeCard == 0) {
            // Изымаем переменную, которая хранит в себе данные по интенту
            Uri data = getIntent().getData();

            // В нашем случае это полный URL путь типа: https://github.com/zhdanov-ek
            String fullPath = data.toString();

            // Возвращает имя хоста: plus.google.com или github.com
            String host = data.getHost();

            // По URL определяем с какого сервера запрашивать инфу
            if (host.contentEquals(Consts.URL_GOOGLE_HOST)) {
                String idUserGoogle = data.getLastPathSegment();
                loadGoogleInfo(idUserGoogle,
                        Consts.FIELDS_GOOGLE_FOR_REQUEST,
                        BuildConfig.GOOGLE_API_KEY);
            }

            if (host.contentEquals(Consts.URL_GIT_HOST)) {
                llPersonBack.setBackgroundColor(getResources().getColor(R.color.colorGit));
                btnMore.setBackground(getResources().getDrawable(R.drawable.bg_button_git));
                btnMore.setTextColor(getResources().getColor(R.color.colorGit));
                ivLogo.setBackground(getResources().getDrawable(R.drawable.logo_git));

                // Изымаем из URL последнюю часть где указан юзер и подаем это значение дальше
                String idUserGit = data.getLastPathSegment();

                // грузим через ретрофит
                loadGitInfo(idUserGit);
            }
        }

    }

    /** Загрузка инфы и ее парсинг с GITHUB средствами ретрофита */
    private void loadGitInfo(String idUser){
        Retrofit retrofitGit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Consts.URL_GIT_BASE)
                .build();
        ServiceGit serviceGit = retrofitGit.create(ServiceGit.class);

        // Создаем интерфейс call на основе нашего интерфейса. Подаем ему на вход ID,
        // а получаем класс ResponseGit, который содержит все поля с json ответа
        Call<ResponseGit> call = serviceGit.loadUserInfo(idUser);
        call.enqueue(new Callback<ResponseGit>() {
            @Override
            public void onResponse(Call<ResponseGit> call, Response<ResponseGit> response) {
                if (response.isSuccessful()){
                    String jsonResult = response.body().toString();
                    Log.i(TAG, "jsonResult = " + jsonResult);

                    // Сразу начинаем грузить в таске нашу картинку и заполняем поля значениями
                    ImageLoadTask  loadImageTask =
                            new ImageLoadTask(response.body().getAvatarUrl(), ivAva);
                    loadImageTask.execute();
                    tvName.setText(response.body().getName());
                    tvUrlProfile.setText(response.body().getHtmlUrl());
                    tvUrlProfile.setVisibility(View.GONE);

                    if (response.body().getPublicRepos() > 0) {
                        tvOther.setText(getString(R.string.have_repositories_on_git) +
                                " " + response.body().getPublicRepos());
                    }
                    btnMore.setEnabled(true);

                } else {
                    btnMore.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    ivAva.setVisibility(View.GONE);
                    tvOther.setText(R.string.error_in_link);
                }
            }
            @Override
            public void onFailure(Call<ResponseGit> call, Throwable t) {
            }
        });
    }


    /** Загрузка инфы и ее парсинг с GOOGLE средствами ретрофита */
    private void loadGoogleInfo(String idUser, String fields, String apiKey){

        Retrofit retrofitGoogle = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Consts.URL_GOOGLE_BASE)
                .build();
        ServiceGoogle serviceGoogle = retrofitGoogle.create(ServiceGoogle.class);

        // Создаем интерфейс call на основе нашего интерфейса. Подаем ему на вход ID, fields, key
        // а получаем класс ResponseGoogle, который содержит все поля с json ответа
        Call<ResponseGoogle> call = serviceGoogle.loadUserInfo(idUser, fields, apiKey);
        Log.d(TAG, "http request: " + call.request().toString());
        call.enqueue(new Callback<ResponseGoogle>() {
            @Override
            public void onResponse(Call<ResponseGoogle> call, Response<ResponseGoogle> response) {
                if (response.isSuccessful()) {
                    // Сразу начинаем грузить в таске нашу картинку и заполняем поля значениями
                    // В линке на картинку меняем параметр отвечающий за размер
                    String urlImage = response.body().getImage().getUrlImage();
                    urlImage = urlImage.replace("sz=50", "sz=500");
                    ImageLoadTask  loadImageTask = new ImageLoadTask(urlImage, ivAva);
                    loadImageTask.execute();

                    // если указан пол то отображаем иконку
                    if ( !(response.body().getGender() == null) ){
                        if (response.body().getGender().contentEquals("female")){
                            ivGender.setBackground(getResources().getDrawable(R.drawable.female));
                        } else {
                            ivGender.setBackground(getResources().getDrawable(R.drawable.male));
                        }
                        ivGender.setVisibility(View.VISIBLE);
                    }

                    // Если имя не указанно то выводим его с другого поля
                    if (response.body().getName().getFamilyName().isEmpty()){
                        tvName.setText(response.body().getDisplayName());
                    } else {
                        tvName.setText(response.body().getName().getFamilyName() + " " +
                                response.body().getName().getGivenName());
                    }
                    tvUrlProfile.setText(response.body().getUrlUser());
                    tvUrlProfile.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ResponseGoogle> call, Throwable t) {
            }
        });
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
