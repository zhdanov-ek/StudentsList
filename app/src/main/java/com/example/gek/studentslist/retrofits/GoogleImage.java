package com.example.gek.studentslist.retrofits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Модель GoogleImage, которая приходит в JSON как объект с полями
 */

public class GoogleImage {

    @SerializedName("url")
    @Expose
    private String urlImage;

    @SerializedName("isDefault")
    @Expose
    private Boolean isDefault;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
