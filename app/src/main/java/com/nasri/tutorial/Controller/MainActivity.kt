package com.nasri.tutorial.Controller

import com.pusher.pushnotifications.PushNotifications
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.nasri.tutorial.R
import com.nasri.tutorial.Services.AuthService
import com.nasri.tutorial.Services.SummaryService
import com.nasri.tutorial.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.os.HandlerCompat.postDelayed
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.nasri.tutorial.Workers.UpdateWorker
import android.arch.lifecycle.Observer
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    // For Floating Action Button
    lateinit var fab_main: FloatingActionButton
    lateinit var fab_sub1: FloatingActionButton
    lateinit var fab_sub2: FloatingActionButton
    lateinit var fab_sub3: FloatingActionButton
    // lateinit var toolbar: Toolbar
    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    lateinit var rotate_cw: Animation
    lateinit var rotate_ccw: Animation
    var isOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // need to be put in the sharedPreferences
        if(App.prefs.isLoggedIn) {
            Toast.makeText(this, "Succesfully log in",
                Toast.LENGTH_LONG).show()
            //update the UI
            updateCurrentSummaries()
        } else {
            Toast.makeText(this, "Please log in",
                Toast.LENGTH_LONG).show()
            val userLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(userLoginIntent)
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar

        // val butt = findViewById<Button>(R.id.progress_bar_increase) as Button

        // 自動跑看看

        fab1.setOnClickListener{
            drinkWater(400)
        }

        fab2.setOnClickListener{
            drinkWater(200)
        }

        fab3.setOnClickListener{
            drinkWater(100)
        }

        reduceHydrationBtn.setOnClickListener{
            beHydrate(34)
        }

        refreshHydrationBtn.setOnClickListener{
            updateCurrentSummaries()
        }

        logoutBtn.setOnClickListener{
            App.prefs.isFirstTime = false
            App.prefs.isLoggedIn = false
            val firstTimeIntent = Intent(this, WelcomeActivity::class.java)
            startActivity(firstTimeIntent)
        }
        // Push Notification
        PushNotifications.start(getApplicationContext(), "a236022d-cd36-4b3f-b85f-a108953a7c20");
        PushNotifications.subscribe("hello");
        PushNotifications.subscribe(App.prefs.userName.toString().replace(" ",""));

        // Recurring Worker : Refresh every 15 minutes
        val reccuringWork : PeriodicWorkRequest = PeriodicWorkRequest.Builder(UpdateWorker::class.java,
            15,TimeUnit.MINUTES).addTag("update-summary").build()

        val workManager = WorkManager.getInstance()
        workManager.enqueue(reccuringWork)

        //Floating Action Button
        //set floating button
        //toolbar = findViewById(R.id.toolbar) as Toolbar
        //setSupportActionBar(toolbar)
        fab_main = findViewById(R.id.fabmain) as FloatingActionButton
        fab_sub1 = findViewById(R.id.fab1) as FloatingActionButton
        fab_sub2 = findViewById(R.id.fab2) as FloatingActionButton
        fab_sub3 = findViewById(R.id.fab3) as FloatingActionButton

        //set animation for floating button
        fab_open = AnimationUtils.loadAnimation(applicationContext, R.anim.open_fab)
        fab_close = AnimationUtils.loadAnimation(applicationContext, R.anim.close_fab)
        rotate_cw = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_clockwise)
        rotate_ccw = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_counterclockwise)

        //set listener for floating buttons
        fab_main.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v : View?) {
                if (isOpen)
                {
                    fab_main.setImageResource(R.drawable.add)
                    fab_sub1.startAnimation(fab_close)
                    fab_sub2.startAnimation(fab_close)
                    fab_sub3.startAnimation(fab_close)
                    fab_main.startAnimation(rotate_ccw)
                    //cast it to a view
                    (fab_sub1 as View).visibility = View.GONE
                    (fab_sub2 as View).visibility = View.GONE
                    (fab_sub3 as View).visibility = View.GONE
                    isOpen = false
                }
                else{
                    fab_sub1.startAnimation(fab_open)
                    fab_sub2.startAnimation(fab_open)
                    fab_sub3.startAnimation(fab_open)
                    fab_main.startAnimation(rotate_cw)
                    (fab_sub1 as View).visibility = View.VISIBLE
                    (fab_sub2 as View).visibility = View.VISIBLE
                    (fab_sub3 as View).visibility = View.VISIBLE
                    fab_sub1.isClickable = true
                    fab_sub2.isClickable = true
                    fab_sub3.isClickable = true
                    isOpen = true
                    fab_main.setImageResource(R.drawable.add)
                }
            }
        })
    }

    fun drinkWater(amount: Int) {
        //save to Db
        val ifNotif = 0
        val currHydration = SummaryService.currentHydration
        SummaryService.addIntakeOfUser(this, amount, currHydration, ifNotif) { intakeSuccess ->
            if(intakeSuccess) {
                //I think just call update function ??
                updateCurrentSummaries()
                Toast.makeText(this, "$amount mL were added", Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(this, "Something is wrong!!", Toast.LENGTH_LONG).show()

            }
        }
    }

    fun changeObjectStage(current: Int, type: String) {
        var imgName = ""
        when {
            current > 300 -> imgName = type + "1"
            current > 200 -> imgName = type + "2"
            current > 100 -> imgName = type + "3"
            current >= 0 -> imgName = type + "4"
        }
        val resourceId = resources.getIdentifier(imgName, "drawable", packageName)
        gifImageView.setImageResource(resourceId)
    }

    fun updateCurrentSummaries() {
        AuthService.findTodaySummaryOfUser(this) { getSuccess ->
            currentObjectType.text = SummaryService.objectType
            currentHydrationTxt.text = SummaryService.currentHydration.toString()
            todayIntakeTxt.text = SummaryService.totalIntake.toString()
            todayDieTxt.text = SummaryService.totalDie.toString()
            progressBar.setProgress(SummaryService.currentHydration)
            changeObjectStage(SummaryService.currentHydration,
                SummaryService.objectType.toLowerCase())
        }
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )
        updateCurrentSummaries()
        super.onResume()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }


    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Update to the newest current state of summary
            if (App.prefs.isLoggedIn) {
                currentObjectType.text = SummaryService.objectType
                currentHydrationTxt.text = SummaryService.currentHydration.toString()
                todayIntakeTxt.text = SummaryService.totalIntake.toString()
                todayDieTxt.text = SummaryService.totalDie.toString()
                progressBar.setProgress(SummaryService.currentHydration)
                changeObjectStage(SummaryService.currentHydration,
                    SummaryService.objectType.toLowerCase())
            }
        }
    }

    // delete this later
    fun beHydrate(amount: Int) {
        var new_hydration = SummaryService.currentHydration - amount
        if (new_hydration <= 0) {
            new_hydration = 400
            val new_die = SummaryService.totalDie + 1
            SummaryService.totalDie = new_die
            todayDieTxt.text = new_die.toString()
        }
        progressBar.setProgress(new_hydration)
        changeObjectStage(new_hydration, SummaryService.objectType.toLowerCase())
        SummaryService.currentHydration = new_hydration
        currentHydrationTxt.text = new_hydration.toString()
        todayIntakeTxt.text = SummaryService.totalIntake.toString()
        Toast.makeText(this, "$amount mL hydration reduced", Toast.LENGTH_LONG).show()
        var h = SummaryService.currentHydration
        when {
            h < 100 -> notification()
            h >= 160 && h <= 200 -> notification()
            h >= 270 && h <= 300 -> notification()
        }
    }

    fun refreshDataRegularly() {
        updateCurrentSummaries()
        val h = SummaryService.currentHydration
        when {
            h < 100 -> notification()
            h >= 160 && h <= 200 -> notification()
            h >= 270 && h <= 300 -> notification()
        }
    }

    //到0的時候可以重新啟動(restart function)
    private fun loadAgain()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //function notification
    private fun notification(){

        var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var notificationChannel : NotificationChannel
        lateinit var builder : Notification.Builder
        val channelId = "com.nasri.notification"
        val description = "Go drink some water, stay hydrated"

        val landingIntent = Intent(this, MainActivity::class.java)
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, landingIntent, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Drink_Notification")
                .setContentText("Test Notification")
                .setSmallIcon(R.drawable.one)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.one))
                .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(this)
                .setContentTitle("Drink_Notification")
                .setContentText("Test Notification")
                .setSmallIcon(R.drawable.two)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.two))
                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(1234, builder.build())
    }

}


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
