package com.nasri.tutorial.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

class SharedPrefs(context: Context) {

    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0) //content private

    val IS_FIRST_TIME = "isFirstTime"
    val IS_LOGGED_IN = "isLoggedIn"
    val AUTH_TOKEN = "authToken"
    val USER_EMAIL = "userEmail"
    val USER_NAME = "userName"

    var isFirstTime: Boolean
        get() = prefs.getBoolean(IS_FIRST_TIME, false)
        set(value) = prefs.edit().putBoolean(IS_FIRST_TIME, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String
        get() = prefs.getString(AUTH_TOKEN, "")
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String
        get() = prefs.getString(USER_EMAIL,"")
        set(value) = prefs.edit().putString(USER_EMAIL, value).apply()

    var userName: String
        get() = prefs.getString(USER_NAME,"")
        set(value) = prefs.edit().putString(USER_NAME, value).apply()

    var requestQueue = Volley.newRequestQueue(context)
}