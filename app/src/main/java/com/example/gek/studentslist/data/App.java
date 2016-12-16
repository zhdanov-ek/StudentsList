package com.example.gek.studentslist.data;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Инициализируем глобальные параметры для всего приложения
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Инициализируем риалм и создаем конфигурацию, которую подаем
        Realm.init(this);
        RealmConfiguration configuration =
                new RealmConfiguration.Builder()
                        .name("students_list.realm")
                        .build();
        Realm.setDefaultConfiguration(configuration);
    }
}


