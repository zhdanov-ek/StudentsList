package com.example.gek.studentslist;

/**
 * Класс описывающий студента для удобства работы в списке
 */

public class Student {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    private String name;
    private String googlePlus;
    private String git;

    public Student(String name, String git, String googlePlus){
        this.name = name;
        this.googlePlus = googlePlus;
        this.git = git;
    }
}
