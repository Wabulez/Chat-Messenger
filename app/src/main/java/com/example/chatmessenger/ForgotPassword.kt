package com.example.chatmessenger

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    var auth: FirebaseAuth?= null
    var forgotpswdLayout:ConstraintLayout ?= null
    var backbtn:ImageView ?= null
    var email:EditText ?= null
    var sendEmail:Button ?= null
    var progressDialog:ProgressDialog ?= null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        backbtn = findViewById(R.id.backbtn)
        email = findViewById(R.id.reset_user_email)
        sendEmail = findViewById(R.id.sendEmail_btn)
        auth = FirebaseAuth.getInstance()
        forgotpswdLayout = findViewById(R.id.forgotpswd_layout)

        val logoutValue = intent.getStringExtra("Logout").toString()
        if (logoutValue.equals("Logout")){
            Snackbar.make(forgotpswdLayout!!,"Current user logged out",Snackbar.LENGTH_LONG).show()
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setMessage("Sending email...")

        backbtn!!.setOnClickListener {
            startActivity(Intent(this, LoginSignUpParent::class.java))
            finish()
        }

        sendEmail!!.setOnClickListener {
            var uEmail = email!!.text.toString().trim()

            if (uEmail.isEmpty()){
                email!!.setError("Email required")
                email!!.requestFocus()
            }
            else if (!uEmail.matches(emailPattern.toRegex())){
                email!!.setError("Invalid email address")
                email!!.requestFocus()
            }
            else {
                progressDialog!!.show()
                auth!!.sendPasswordResetEmail(uEmail).addOnCompleteListener {
                    if (it.isSuccessful){
                        progressDialog!!.dismiss()
                        Snackbar.make(forgotpswdLayout!!,"Email Sent",Snackbar.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    progressDialog!!.dismiss()
                    Snackbar.make(forgotpswdLayout!!,it.message.toString(),Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,LoginSignUpParent::class.java))
        finish()
    }
}