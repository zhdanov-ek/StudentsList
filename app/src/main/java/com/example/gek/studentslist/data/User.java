package com.example.gek.studentslist.data;

import android.net.Uri;

/**
 * Created by gek on 29.11.2016.
 */

public class User {

    private String name;
    private String phone;
    private String email;
    private Uri icon;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Uri getIcon() {
        return icon;
    }

    public void setIcon(Uri icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
