package com.nasri.tutorial.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nasri.tutorial.Controller.App
import com.nasri.tutorial.Utilities.BASE_URL
import com.nasri.tutorial.Utilities.BROADCAST_USER_DATA_CHANGE
import com.nasri.tutorial.Utilities.URL_SUMMARY
import org.json.JSONException
import org.json.JSONObject

object SummaryService {
    var id = ""
    var todayDate = ""
    var objectType = ""
    var currentHydration = 0
    var totalDie = 0
    var totalIntake = 0

    fun addIntakeOfUser (context: Context, amountIntake: Int,
                         currHydration: Int, notification: Int,
                         complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("amount_intake", amountIntake)
        jsonBody.put("current_hydration", currHydration)
        jsonBody.put("notification", notification)
        val requestBody = jsonBody.toString()

        val urlIntake = "$BASE_URL/summaries/$id/intake"

        val addIntakeRequest = object : StringRequest(Method.POST, urlIntake, Response.Listener { response ->
            try {
                println(response)
                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON","EXCEPTION: " + e.localizedMessage)
                complete(false)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR","Couldn't Add Intake: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        Volley.newRequestQueue(context).add(addIntakeRequest)
    }
}