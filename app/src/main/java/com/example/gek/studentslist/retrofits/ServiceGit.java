package com.example.gek.studentslist.retrofits;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ServiceGit {
    // Указываем куда  будем отпрвлять запрос. Этот путь добавится к базовому
    // который указали при создании объекта Retrofit. При вызове loadUserInfo
    // передаваемый параметр будет добавлен в URL, а именно добавлен в конец
    @GET("{id}")

    // Описываем метод, который будет возвращать строку, а на вход принимать map
    // где будут параметры запроса: key, fields
//    Call<Object> loadUserInfo(@FieldMap Map<String, String> map);
    Call<ResponseGit> loadUserInfo(@Path("id") String id);
}