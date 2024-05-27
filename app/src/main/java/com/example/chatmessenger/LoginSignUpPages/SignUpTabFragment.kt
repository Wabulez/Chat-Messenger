package com.example.chatmessenger.LoginSignUpPages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.chatmessenger.R

class SignUpTabFragment:Fragment() {
    var name:EditText ?= null
    var email: EditText?= null
    var phoneNumber:EditText ?= null
    var password: EditText?= null
    var confirmPassword:EditText ?= null
    var registerBtn: Button?= null
    var registerLayout: ConstraintLayout?= null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.signup_tab_fragment, container, false)
        name = root.findViewById(R.id.signup_user_name)
        email = root.findViewById(R.id.signup_user_email)
        phoneNumber = root.findViewById(R.id.signup_user_number)
        password = root.findViewById(R.id.signup_user_password)
        confirmPassword = root.findViewById(R.id.signup_user_confirm_password)
        registerBtn = root.findViewById(R.id.registerBtn)
        registerLayout = root.findViewById(R.id.registerLayout)

        registerBtn!!.setOnClickListener {
            var uName = name!!.text.toString().trim()
            var uEmail = email!!.text.toString().trim()
            var uPassword = password!!.text.toString().trim()
            var uConPassword = confirmPassword!!.text.toString().trim()
            var uNumber = phoneNumber!!.text.toString().trim()


            if (uName.isEmpty()){
                name!!.setError("Name required")
                name!!.requestFocus()
            }
            else if (uEmail.isEmpty()) {
                email!!.setError("Email address required")
                email!!.requestFocus()
            }
            else if (!uEmail.matches(emailPattern.toRegex())){
                email!!.setError("Invalid email address")
                email!!.requestFocus()
            }
            else if (uNumber.isEmpty()) {
                phoneNumber!!.setError("Phone Number required")
                phoneNumber!!.requestFocus()
            }
            else if (uNumber.length != 10){
                phoneNumber!!.setError("Invalid Phone Number")
                phoneNumber!!.requestFocus()
            }
            else if (uPassword.isEmpty()){
                password!!.setError("Password required")
                password!!.requestFocus()
            }
            else if (uPassword.length < 6){
                password!!.setError("Password too short")
                password!!.requestFocus()
            }
            else if (!uConPassword.equals(uPassword) || uConPassword.isEmpty()){
                confirmPassword!!.setError("Passwords do not match")
                confirmPassword!!.requestFocus()
            }
            else {
                val i = Intent(getActivity(), SelectProfPic::class.java).apply {
                    putExtra("name", uName)
                    putExtra("email", uEmail)
                    putExtra("number", uNumber)
                    putExtra("password", uPassword)
                }
                startActivity(i)
                activity?.finish()

//                progressDialog!!.show()
//                auth!!.createUserWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener{
//                    if (it.isSuccessful){
//                        val uid = FirebaseAuth.getInstance().uid
//                        val userData = User(uName,uEmail,uNumber,uPassword)
//                        val reference = FirebaseDatabase.getInstance().getReference("/Users/$uid")
//
//                        reference.setValue(userData).addOnSuccessListener {
//                            progressDialog!!.dismiss()
//                            var snackbar = Snackbar.make(registerLayout!!,"Account created. Please log in to continue", Snackbar.LENGTH_LONG)
//                            snackbar.show()
//                            name!!.text.clear()
//                            email!!.text.clear()
//                            phoneNumber!!.text.clear()
//                            password!!.text.clear()
//                            confirmPassword!!.text.clear()
//                        }.addOnFailureListener {
//                            progressDialog!!.dismiss()
//                            Snackbar.make(registerLayout!!,"Error! Account data not added to database", Snackbar.LENGTH_LONG).show()
//                        }
//                    }
//                }.addOnFailureListener {
//                    progressDialog!!.dismiss()
//                    Snackbar.make(registerLayout!!,it.message.toString(), Snackbar.LENGTH_LONG).show()
//                }
            }
        }
        return root
    }
}
