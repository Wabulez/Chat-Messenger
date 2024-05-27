package com.example.chatmessenger.HelpPages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.FeedbackPage
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.example.chatmessenger.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SigningUpHelpPage : AppCompatActivity() {
    var auth:FirebaseAuth ?= null
    var backbtn:ImageView ?= null
    var createAccountLink:TextView ?= null
    var like:Button ?= null
    var dislike:Button ?= null
    var signUpHelpPage:ConstraintLayout ?= null
    var handler:Handler ?= null
    var runnable:Runnable ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signing_up_help_page)
        createAccountLink = findViewById(R.id.link)
        like = findViewById(R.id.likebtn)
        dislike = findViewById(R.id.dislikebtn)
        signUpHelpPage = findViewById(R.id.signup_helpPage)
        backbtn = findViewById(R.id.backbtn)
        auth = FirebaseAuth.getInstance()

        backbtn!!.setOnClickListener {
            startActivity(Intent(this, HelpPage::class.java))
            finish()
        }

        createAccountLink!!.setOnClickListener {
            auth!!.signOut()
            var i = Intent(this, LoginSignUpParent::class.java).apply {
                putExtra("Logout","Logout")
            }
            startActivity(i)
            finish()
        }
        like!!.setOnClickListener {
            Snackbar.make(signUpHelpPage!!,"Thank you for your feedback",Snackbar.LENGTH_LONG).show()
            handler = Handler()
            runnable = object : Runnable{
                override fun run() {
                    startActivity(Intent(this@SigningUpHelpPage, HelpPage::class.java))
                    finish()
                }
            }
            handler!!.postDelayed(runnable!!,800)
        }

        dislike!!.setOnClickListener {
            startActivity(Intent(this, FeedbackPage::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HelpPage::class.java))
        finish()
    }
}