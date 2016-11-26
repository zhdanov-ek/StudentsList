package com.example.gek.studentslist.retrofits;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ServiceGit {
    // Указываем куда  будем отпрвлять запрос. Этот путь добавится к базовому
    // который указали при создании объекта Retrofit. При вызове loadUserInfo
    // передаваемый параметр будет добавлен в URL, а именно добавлен в конец
    @GET("{id}")

    // Описываем метод, который будет возвращать данные в виде объекта ResponseGit,
    // а на вход принимать ID в виде строки
    Call<ResponseGit> loadUserInfo(@Path("id") String id);
}