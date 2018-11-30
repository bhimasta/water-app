package com.nasri.tutorial

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
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.nasri.tutorial.Services.AuthService
import com.nasri.tutorial.Services.SummaryService
import com.nasri.tutorial.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    //For Floating Action Button
    lateinit var fab_main: FloatingActionButton
    lateinit var fab_sub1: FloatingActionButton
    lateinit var fab_sub2: FloatingActionButton
    lateinit var fab_sub3: FloatingActionButton
//    lateinit var toolbar: Toolbar
    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    lateinit var rotate_cw: Animation
    lateinit var rotate_ccw: Animation
    var isOpen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!AuthService.isLoggedIn) {
            Toast.makeText(this, "Please log in",
                Toast.LENGTH_LONG).show()
            val userLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(userLoginIntent)
        } else {
            Toast.makeText(this, "Succesfully log in",
                Toast.LENGTH_LONG).show()
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar

        val butt = findViewById<Button>(R.id.progress_bar_increase) as Button

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

        //自動跑看看
//        var status = 100
//
//        Thread(Runnable{ //實現功能，刷新UI介面(to renew the UI interface)
//            while(status >= 0){
//
//                //stage 1 滿的狀態
//                while(status > 75) {
//                    status -= 1
//
//                    //要轉成flower1，不然從stage3 或是 stage2 轉匯成stage1後會沒有圖片可以接，變成flower4(convert it to flower one)
//                    this@MainActivity.runOnUiThread {
//                        this.gifImageView.setImageResource(R.drawable.plant1)
//                    }
//
//
//                    //按一下回100
//                    /*progress_bar_increase.setOnClickListener {
//                       // if(status > 0) {
//                            status = 100
//                            //gifImageView.setImageResource(R.drawable.plant1)
//                        //}else{showDialog()}
//                    }*/
//
//                    try {
//                        Thread.sleep(100)
//                        progressBar.setProgress(status)
//                    } catch (e: Exception) {
//                        Log.e("Error", e.message)
//                    }
//                }
//
//                //滿的時候不給按(Unclickable when the bar is full)
//                this@MainActivity.runOnUiThread{
//                    fab_sub1.isClickable = false
//                    fab_sub2.isClickable = false
//                    fab_sub3.isClickable = false
//                    //(fab_main as View).visibility = View.VISIBLE
//                }
//
//
//                //要用runOnUiThread，因為直接換會閃退，非UI元件不可以直接換UI，要重新設一個Thread(to change the things on UI)
//                //轉場 plant1 -> plant2
//                this@MainActivity.runOnUiThread {
//                    this.gifImageView.setImageResource(R.drawable.plant2)
//                }
////                notification() //send notification when change state
//
//                //stage 2 四分之三
//                while (status in 50..75){
//                    status -= 1
//
//                    fab_sub1.setOnClickListener{
//                        Toast.makeText(applicationContext, "One was clicked", Toast.LENGTH_LONG).show()
//                        status = 100
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant1)
//                        }
//                    }
//                    fab_sub2.setOnClickListener{
//                        Toast.makeText(applicationContext, "Two was clicked", Toast.LENGTH_LONG).show()
//                        status = 100
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant1)
//                        }
//                    }
//                    fab_sub3.setOnClickListener{
//                        Toast.makeText(applicationContext, "Three was clicked", Toast.LENGTH_LONG).show()
//                        status = 100
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant1)
//                        }
//                    }
//
//                    //開始給按(make the button clickable when it's not full)
//                    this@MainActivity.runOnUiThread{
//                        fab_sub1.isClickable = true
//                        fab_sub2.isClickable = true
//                        fab_sub3.isClickable = true
//                        //(fab_main as View).visibility = View.VISIBLE
//                    }
//
//                    try {
//                        Thread.sleep(100)
//                        progressBar.setProgress(status)
//                    } catch (e: Exception) {
//                        Log.e("Error", e.message)
//                    }
//                }
//
//                //轉場 plant2 -> plant3
//                this@MainActivity.runOnUiThread {
//                    this.gifImageView.setImageResource(R.drawable.plant3)
//                }
////                notification() //send notification when change state
//
//                //stage 3 二分之一
//                while (status in 25..50){
//                    status -= 1
//
//                    fab_sub1.setOnClickListener{
//                        Toast.makeText(applicationContext, "One was clicked", Toast.LENGTH_LONG).show()
//                        status = 100
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant1)
//                        }
//                    }
//                    fab_sub2.setOnClickListener{
//                        Toast.makeText(applicationContext, "Two was clicked", Toast.LENGTH_LONG).show()
//                        status = 100
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant1)
//                        }
//                    }
//                    fab_sub3.setOnClickListener{
//                        Toast.makeText(applicationContext, "Three was clicked", Toast.LENGTH_LONG).show()
//                        status += 25
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant2)
//                        }
//                    }
//
//                    try {
//                        Thread.sleep(100)
//                        progressBar.setProgress(status)
//                    } catch (e: Exception) {
//                        Log.e("Error", e.message)
//                    }
//                }
//
//                //轉場 plant3 -> plant4
//                this@MainActivity.runOnUiThread {
//                    this.gifImageView.setImageResource(R.drawable.plant4)
//                }
////                notification() //send notification when change state
//
//                //stage 4 四分之一
//                while (status in 0..25){
//                    status -= 1
//
//                    //0的時候重新啟動(restart when decrease to 0)
//                    stop.setOnClickListener {
//                        if(status > 0) {
//                            Toast.makeText(applicationContext, "Can't restart yet", Toast.LENGTH_LONG).show()
//                        }
//                        else{showDialog()}
//                    }
//
//                    fab_sub1.setOnClickListener {
//                        Toast.makeText(applicationContext, "One was clicked", Toast.LENGTH_LONG).show()
//                        status = 100
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant1)
//                        }
//                    }
//                    fab_sub2.setOnClickListener{
//                        Toast.makeText(applicationContext, "Two was clicked", Toast.LENGTH_LONG).show()
//                        status += 50
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant2)
//                        }
//                    }
//                    fab_sub3.setOnClickListener {
//                        Toast.makeText(applicationContext, "Three was clicked", Toast.LENGTH_LONG).show()
//                        status += 25
//                        this@MainActivity.runOnUiThread {
//                            this.gifImageView.setImageResource(R.drawable.plant3)
//                        }
//                    }
//
//                    try {
//                        Thread.sleep(100)
//                        progressBar.setProgress(status)
//                    } catch (e: Exception) {
//                        Log.e("Error", e.message)
//                    }
//
//                }
////                notification() //send notification when change state
//
//                stop.setOnClickListener {
//                    if(status > 0) {
//                        Toast.makeText(applicationContext, "Can't restart yet", Toast.LENGTH_LONG).show()
//                    }
//                    else{showDialog()}
//                }
//            }
//        }).start()

        //Floating Action Button
        //set floating button
//        toolbar = findViewById(R.id.toolbar) as Toolbar
        //setSupportActionBar(toolbar)
        fab_main = findViewById(R.id.fabmain) as FloatingActionButton
        fab_sub1 = findViewById(R.id.fab1) as FloatingActionButton
        fab_sub2 = findViewById(R.id.fab2) as FloatingActionButton
        fab_sub3 = findViewById(R.id.fab3) as FloatingActionButton

        //set animation for floating button
        fab_open = AnimationUtils.loadAnimation(applicationContext,R.anim.open_fab)
        fab_close = AnimationUtils.loadAnimation(applicationContext,R.anim.close_fab)
        rotate_cw = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_clockwise)
        rotate_ccw = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_counterclockwise)

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

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Update to the newest current state of summary
            if (AuthService.isLoggedIn) {
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

    fun loadSlides(v: View)
    {
        PreferenceManager(this).clearPreference()
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()

    }

    //到0的時候可以重新啟動(restart function)
    private fun loadAgain()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

//    private fun showDialog(){
//        lateinit var dialog: AlertDialog
//        var builder = AlertDialog.Builder(this)
//        builder.setTitle("You Bad Bad")
//        builder.setMessage("You bad bad the item died lol!")
//
//        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
//            when(which){
//                DialogInterface.BUTTON_POSITIVE -> {
//                    toast("You are going to continue the challenge!!")
//                    loadAgain()
//                }
//                DialogInterface.BUTTON_NEGATIVE -> toast("You fuck off")
//                DialogInterface.BUTTON_NEUTRAL -> toast("You are staying in the same page")
//            }
//        }
//        // Set the alert dialog positive/yes button
//        builder.setPositiveButton("YES",dialogClickListener)
//        // Set the alert dialog negative/no button
//        builder.setNegativeButton("NO",dialogClickListener)
//        // Set the alert dialog neutral/cancel button
//        builder.setNeutralButton("CANCEL",dialogClickListener)
//
//        // Initialize the AlertDialog using builder object
//        dialog = builder.create()
//        // Finally, display the alert dialog
//        dialog.show()
//    }

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
