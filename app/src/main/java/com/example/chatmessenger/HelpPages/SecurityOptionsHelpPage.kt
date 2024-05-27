package com.example.chatmessenger.HelpPages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.example.chatmessenger.R
import com.example.chatmessenger.UserAuthPage

class SecurityOptionsHelpPage : AppCompatActivity() {
//    var deleteAccount:TextView ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_options_help_page)
//        deleteAccount = findViewById(R.id.delete_accountTxt)

//        deleteAccount!!.setOnClickListener {
//            val i = Intent(this, UserAuthPage::class.java)
//            i.putExtra("Intent", "Delete_Account")
//            startActivity(i)
//            finish()
//        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}