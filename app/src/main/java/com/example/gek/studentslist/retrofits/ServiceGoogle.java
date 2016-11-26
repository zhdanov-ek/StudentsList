package com.example.gek.studentslist.retrofits;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ServiceGoogle {
    // Указываем куда  будем отпрвлять запрос. Этот путь добавится к базовому
    // который указали при создании объекта Retrofit. При вызове loadUserInfo
    // передаваемый параметр будет добавлен в URL, а именно добавлен в конец
    @GET("{id}")

    // Описываем метод, который будет возвращать данные в виде объекта ResponseGoogle,
    // а на вход принимать ID, который будет добавлен к основному URL
    // и параметры fields, apiKey
    Call<ResponseGoogle> loadUserInfo(@Path("id") String id,
                                            @Query("fields") String fields,
                                            @Query("key") String apiKey);
}