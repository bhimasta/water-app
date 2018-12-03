package com.nasri.tutorial.Workers

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nasri.tutorial.Controller.App
import com.nasri.tutorial.Services.SummaryService
import com.nasri.tutorial.Utilities.BROADCAST_USER_DATA_CHANGE
import com.nasri.tutorial.Utilities.URL_SUMMARY
import org.json.JSONException

private const val LOG_TAG = "UpdateWorker"

class UpdateWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result = try {

        Log.d(LOG_TAG, "Updating Current Summary!")
        val findUserRequest = object : JsonObjectRequest(Method.GET, URL_SUMMARY, null, Response.Listener { response ->
            try {
                SummaryService.id = response.getString("id")
                SummaryService.todayDate = response.getString("date_start")
                SummaryService.objectType = response.getString("living_object")
                SummaryService.currentHydration = response.getInt("current_hydration")
                SummaryService.totalIntake = response.getInt("total_intakes")
                SummaryService.totalDie = response.getInt("total_die")
                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(userDataChange)

//                val output = Data.Builder()
//                    .putString()
//                    .putInt(KEY_RESULT, timeToSleep.toInt())
//                    .build()
//
//                outputData = output
                Log.d(LOG_TAG, "Update Success!")
            } catch (e: JSONException) {
                Log.d("JSON","EXCEPTION: " + e.localizedMessage)
                Result.FAILURE
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR","Couldn't find user: $error")
            Result.FAILURE
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserRequest)
        Result.SUCCESS

    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        Result.FAILURE
    }
}
