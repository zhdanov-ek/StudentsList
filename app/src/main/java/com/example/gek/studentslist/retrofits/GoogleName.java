package com.example.gek.studentslist.retrofits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Модель GoogleName, которая приходит в JSON как объект с полями
 */

public class GoogleName {
    @SerializedName("familyName")
    @Expose
    private String familyName;

    @SerializedName("givenName")
    @Expose
    private String givenName;


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
}
