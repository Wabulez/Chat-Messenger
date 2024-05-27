package com.example.chatmessenger.HelpPages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.example.chatmessenger.R

class HelpPage : AppCompatActivity() {
    var backbtn:ImageView ?= null
    var signingUp:Button ?= null
    var editingInfo:Button ?= null
    var cantLogin:Button ?= null
    var payment:Button ?= null
    var support:Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_page)
        backbtn = findViewById(R.id.backbtn)
        signingUp = findViewById(R.id.signup_helpBtn)
        cantLogin = findViewById(R.id.cantLogin_helpBtn)
        payment = findViewById(R.id.payment_helpBtn)
        support = findViewById(R.id.callSupport_helpPage)

        backbtn!!.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        signingUp!!.setOnClickListener {
            startActivity(Intent(this, SigningUpHelpPage::class.java))
            finish()
        }
        cantLogin!!.setOnClickListener {
            startActivity(Intent(this, CantLoginHelpPage::class.java))
            finish()
        }
        payment!!.setOnClickListener {
            startActivity(Intent(this, PaymentInquiresHelpPage::class.java))
            finish()
        }
        support!!.setOnClickListener {
            startActivity(Intent(this, CallSupportHelpPage::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}