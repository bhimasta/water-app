package com.nasri.tutorial.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nasri.tutorial.Utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""
    var userName = ""

    fun loginUser (context: Context, username: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("username", username)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            try {
                val account = response.getJSONObject("account")
                userName = account.getString("username")
                userEmail = account.getString("email")
                authToken = response.getString("auth_token")
                isLoggedIn = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON","EXCEPTION: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Couldn't login user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun findTodaySummaryOfUser (context: Context, complete: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, URL_SUMMARY, null, Response.Listener { response ->
            try {
                println(response)
                SummaryService.id = response.getString("id")
                SummaryService.todayDate = response.getString("date_start")
                SummaryService.objectType = response.getString("living_object")
                SummaryService.currentHydration = response.getInt("current_hydration")
                SummaryService.totalIntake = response.getInt("total_intakes")
                SummaryService.totalDie = response.getInt("total_die")
                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON","EXCEPTION: " + e.localizedMessage)
                complete(false)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR","Couldn't find user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }

        Volley.newRequestQueue(context).add(findUserRequest)
    }
}