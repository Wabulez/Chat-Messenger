package com.example.chatmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.HelpPages.HelpPage
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.google.android.material.snackbar.Snackbar

class FeedbackPage : AppCompatActivity() {
    var feedbackLayout:ConstraintLayout ?= null
    var edtSuggest:EditText ?= null
    var submit:Button ?= null
    var cancel:TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_page)
        edtSuggest = findViewById(R.id.suggestion_edt)
        feedbackLayout = findViewById(R.id.feedback_window)
        submit = findViewById(R.id.submit)
        cancel = findViewById(R.id.cancelTxt)

        submit!!.setOnClickListener {
            if (edtSuggest!!.text.toString().isEmpty()){
                edtSuggest!!.setError("Please enter a suggestion to continue")
                edtSuggest!!.requestFocus()
            } else {
                edtSuggest!!.text.clear()
                Snackbar.make(feedbackLayout!!,"Thank you for your feedback",Snackbar.LENGTH_LONG).show()
                Handler().postDelayed(object : Runnable{
                    override fun run() {
                        startActivity(Intent(this@FeedbackPage, MainActivity::class.java))
                        finish()
                    }
                },2000)
            }
        }

        cancel!!.setOnClickListener {
            startActivity(Intent(this, HelpPage::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HelpPage::class.java))
        finish()
    }
}