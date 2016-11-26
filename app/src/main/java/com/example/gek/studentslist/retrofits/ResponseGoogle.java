package com.example.gek.studentslist.retrofits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Модель описывающая формат JSON объекта
 * Сложные поля name и image описаны дополнительными моделями
 */

public class ResponseGoogle {

    @SerializedName("name")
    @Expose
    private GoogleName name;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("url")
    @Expose
    private String urlUser;

    @SerializedName("image")
    @Expose
    private GoogleImage image;

    public GoogleName getName() {
        return name;
    }

    public void setName(GoogleName name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUrlUser() {
        return urlUser;
    }

    public void setUrlUser(String urlUser) {
        this.urlUser = urlUser;
    }

    public GoogleImage getImage() {
        return image;
    }

    public void setImage(GoogleImage image) {
        this.image = image;
    }
}
