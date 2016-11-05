/**
 * Класс описывающий студента для удобства работы в списке
 */

package com.example.gek.studentslist.data;


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

    // return last 21 char - user id in Google+
    // https://plus.google.com/u/0/108482088578879737406
    public String getIdGoogle(){
        String idGoogle = googlePlus.subSequence(
                googlePlus.length()-21,
                googlePlus.length())
                .toString();
        return idGoogle;
    }

    public String getIdGit(){
        return git.substring(19);
    }
}
