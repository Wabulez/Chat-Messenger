package com.example.chatmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    var auth:FirebaseAuth ?= null
    var backgroundImg:ImageView ?= null
    var appName:TextView ?= null
    var lottie:LottieAnimationView ?= null
    var handler:Handler ?= null
    var runnable:Runnable ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        backgroundImg = findViewById(R.id.background_img)
        appName = findViewById(R.id.app_name)
        lottie = findViewById(R.id.chat_anim)
        auth = FirebaseAuth.getInstance()

        appName!!.animate().translationY(1800f).setDuration(1000).setStartDelay(4000)
        lottie!!.animate().translationY(1800f).setDuration(1000).setStartDelay(4000)

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                checkUser()
            }
        }
        handler!!.postDelayed(runnable!!,5000)
    }
    private fun checkUser(){
        var firebaseUser = auth!!.currentUser
        if (firebaseUser != null){
            var i = Intent(this, MainActivity::class.java).apply {
                putExtra("Login","Login")
            }
            startActivity(i)
            finish()
        } else {
            var i = Intent(this, LoginSignUpParent::class.java)
            startActivity(i)
            finish()
        }
    }
}