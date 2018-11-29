package com.nasri.tutorial

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mPager : ViewPager
    private var layouts : IntArray = intArrayOf(R.layout.first_slide, R.layout.second_slide, R.layout.third_slide)
    private lateinit var mpagerAdapter: MPagerAdapter

    private lateinit var Dots_Layout : LinearLayout
    private lateinit var dots : Array<ImageView>

    private lateinit var btnSkip : Button
    private lateinit var btnNext : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //開啟前先看看是否為第一次打開app(to is if it's the first time opening the app)
        if(PreferenceManager(this).checkPreference())
        {
            loadHome()
        }

        //讓狀態列透明化(to make the toolbar translucent)
        if(Build.VERSION.SDK_INT >= 19)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        else
        {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        setContentView(R.layout.activity_welcome)

        mPager = findViewById(R.id.viewPager) as ViewPager
        mpagerAdapter = MPagerAdapter(layouts, this)
        mPager.adapter = mpagerAdapter

        Dots_Layout = findViewById(R.id.dotsLayout) as LinearLayout

        //換頁按紐
        btnNext = findViewById(R.id.btnNext) as Button
        btnSkip = findViewById(R.id.btnSkip) as Button
        btnNext.setOnClickListener(this)
        btnSkip.setOnClickListener(this)

        createDots(0)

        //創造點點跟著圖片滑動(to make the dots change stage with the graph)
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                //點點跟圖片滑動
                createDots(position)

                //到最後一頁改變button功能與字(change the function of the button and word)
                if(position == layouts.size - 1) //最後一頁
                {
                    btnNext.setText("Start")
                    btnSkip.visibility=View.INVISIBLE
                }
                else //還有下一頁
                {
                    btnNext.setText("Next")
                    btnSkip.visibility = View.VISIBLE
                }

            }
        })

    }

    //頁面跳轉按鈕
    override fun onClick(v: View?) {

        when(v!!.getId())
        {
            R.id.btnSkip ->
            {
                loadHome()
                PreferenceManager(this).writePreference() //每做一個動作就記錄起來
            }
            R.id.btnNext ->
            {
                loadNextSlide()
            }
        }
    }

    //回到Home
    private fun loadHome()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //Next頁面的按鈕功能
    private fun loadNextSlide()
    {
        var nextSlide: Int = mPager.currentItem + 1

        //有next就跳轉，沒有就跳回主畫面
        if(nextSlide < layouts.size)
        {
            mPager.setCurrentItem(nextSlide)
        }
        else
        {
            loadHome()
            PreferenceManager(this).writePreference() //紀錄動作
        }
    }

    //隨頁面轉換的點點
    private fun createDots(current_position: Int)
    {
        if (Dots_Layout!= null)
            Dots_Layout.removeAllViews()

        dots = Array(layouts.size, {i -> ImageView(this)})

        for (i in 0..layouts.size - 1)
        {
            dots[i] = ImageView(this)
            if (i == current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots))
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots))
            }

            //parameter for linear layout
            var paramas: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            paramas.setMargins(4,0,4,0)
            //放到linear layout裡面
            Dots_Layout.addView(dots[i],paramas)
        }
    }

}
