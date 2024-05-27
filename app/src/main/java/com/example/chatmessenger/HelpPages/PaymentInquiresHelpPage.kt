package com.example.chatmessenger.HelpPages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.FeedbackPage
import com.example.chatmessenger.R
import com.google.android.material.snackbar.Snackbar

class PaymentInquiresHelpPage : AppCompatActivity() {
    var backbtn: ImageView?= null
    var like: Button?= null
    var dislike: Button?= null
    var paymentHelpPage: ConstraintLayout?= null
    var handler: Handler?= null
    var runnable:Runnable ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_inquires_help_page)
        like = findViewById(R.id.likebtn)
        dislike = findViewById(R.id.dislikebtn)
        paymentHelpPage = findViewById(R.id.payment_helpPage)
        backbtn = findViewById(R.id.backbtn)

        backbtn!!.setOnClickListener {
            startActivity(Intent(this, HelpPage::class.java))
            finish()
        }

        like!!.setOnClickListener {
            Snackbar.make(paymentHelpPage!!,"Thank you for your feedback", Snackbar.LENGTH_LONG).show()
            handler = Handler()
            runnable = object : Runnable{
                override fun run() {
                    startActivity(Intent(this@PaymentInquiresHelpPage, HelpPage::class.java))
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