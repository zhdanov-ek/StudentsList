package com.example.gek.studentslist.data;

/**
 * Class for const
 */

public final class Consts {
    public static final String URL_GOOGLE_BASE = "https://www.googleapis.com/plus/v1/people/";
    public static final String URL_GOOGLE_HOST = "plus.google.com";
//    public static final String FIELDS_GOOGLE_FOR_REQUEST = "displayName,image,name,url,gender";
public static final String FIELDS_GOOGLE_FOR_REQUEST = "displayName,image,name,url,gender";
    public static final String URL_GIT_BASE = "https://api.github.com/users/";
    public static final String URL_GIT_HOST = "github.com";

    public static final String TYPE_CARD = "TYPE CARD";
    public static final int TYPE_CARD_GOOGLE = 1;
    public static final int TYPE_CARD_GIT = 2;

    public static final String ID_GOOGLE = "ID GOOGLE PLUS ACCOUNT";
    public static final String ID_GIT = "ID GIT ACCOUNT";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_URL_PROFILE = "urlProfile";
    public static final String FIELD_URL_IMAGE = "urlImage";
    public static final String FIELD_GIT_REPOS = "public_repos";
    public static final String FIELD_RESULT_STATUS = "result_status";
    public static final String RESULT_STATUS_PARSING_OK = "parsing_ok";

}
