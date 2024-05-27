package com.example.chatmessenger.LoginSignUpPages

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.chatmessenger.ForgotPassword
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.example.chatmessenger.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginTabFragment:Fragment() {
    var auth:FirebaseAuth ?= null
    var email:EditText ?= null
    var password:EditText ?= null
    var forgotPasswordTxt:TextView ?= null
    var loginBtn:Button ?= null
    var loginLayout:ConstraintLayout ?= null
    var progressDialog:ProgressDialog ?= null
    var handler:Handler ?= null
    var runnable:Runnable ?= null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.login_tab_fragment, container, false)
        email = root.findViewById(R.id.login_user_email)
        password = root.findViewById(R.id.login_user_password)
        forgotPasswordTxt = root.findViewById(R.id.forgot_pswdTxt)
        loginBtn = root.findViewById(R.id.loginBtn)
        loginLayout = root.findViewById(R.id.loginLayout)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(getActivity())
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setMessage("Logging In...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)

        forgotPasswordTxt!!.setOnClickListener {
            startActivity(Intent(getActivity(), ForgotPassword::class.java))
            activity?.finish()
        }

        loginBtn!!.setOnClickListener {
            var uEmail = email!!.text.toString().trim()
            var uPassword = password!!.text.toString().trim()

            if (uEmail.isEmpty()){
                email!!.setError("Email required")
                email!!.requestFocus()
            }
            else if (!uEmail.matches(emailPattern.toRegex())){
                email!!.setError("Invalid email address")
                email!!.requestFocus()
            }
            else if (uPassword.isEmpty()){
                password!!.setError("Password required")
                password!!.requestFocus()
            }
            else{
                progressDialog!!.show()
                auth!!.signInWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener {
                    if (it.isSuccessful){
                        progressDialog!!.dismiss()
                        email!!.text.clear()
                        password!!.text.clear()
                        var snackbar = Snackbar.make(loginLayout!!,"Login successful", Snackbar.LENGTH_SHORT)
                        snackbar.show()
                        handler = Handler()
                        runnable = object : Runnable {
                            override fun run() {
                                var i = Intent(getActivity(), MainActivity::class.java).apply {
                                    putExtra("Login","Login")
                                }
                                startActivity(i)
                                activity?.finish()
                            }
                        }
                        handler!!.postDelayed(runnable!!,500)
                    }
                }.addOnFailureListener {
                    progressDialog!!.dismiss()
                    Snackbar.make(loginLayout!!,it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
        return root
    }
}