package com.nasri.tutorial

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class PreferenceManager
{
    private lateinit var con: Context
    private lateinit var sharedPreferences : SharedPreferences

    //建構子constructor
    constructor(con: Context) {
        this.con = con
        getSharedPreference()
    }

    private fun getSharedPreference(){
        sharedPreferences = con.getSharedPreferences(con.getString(R.string.my_preference), Context.MODE_PRIVATE)

    }

    //寫入sharedPreference
    fun writePreference()
    {
        var editor : SharedPreferences.Editor = sharedPreferences.edit() //讓她可以去edit preference
        editor.putString(con.getString(R.string.my_preference_key), "INIT_OK")
        editor.commit()
    }


    //看使用者是不是第一次開啟app(to see if it's the first time opening the app)
    fun checkPreference() : Boolean
    {
        var status : Boolean = false
        if (sharedPreferences.getString(con.getString(R.string.my_preference_key), "null").equals("null"))
        {
            status = false
        }
        else
        {
            status = true
        }
        return status
    }

    //清掉紀錄
    fun clearPreference()
    {
        sharedPreferences.edit().clear().commit()
    }

}