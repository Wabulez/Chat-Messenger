package com.example.chatmessenger

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.core.net.toUri
import com.example.chatmessenger.HelpPages.SecurityOptionsHelpPage
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.example.chatmessenger.MainActivityPages.Contacts
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_user_auth_page.*
import java.util.*

class UserAuthPage : AppCompatActivity() {
    var user:User ?= null
    var backBtn:ImageView ?= null
    var confirmationText:TextView ?= null
    var edtPassword:EditText ?= null
    var cfmBtn:Button ?= null
    var auth:FirebaseAuth ?= null
    var credential:AuthCredential ?= null
    var progressDialogDelete:ProgressDialog ?= null
    var progressDialogUpdate:ProgressDialog ?= null
    var handler:Handler ?= null
    var runnable:Runnable ?= null
    var query1:Query ?= null
    var query2:Query ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_auth_page)
        confirmationText = findViewById(R.id.confirmation_txt)
        edtPassword = findViewById(R.id.confirmation_user_password)
        backBtn = findViewById(R.id.backbtn)
        cfmBtn = findViewById(R.id.confirmBtn)
        auth = FirebaseAuth.getInstance()

        progressDialogDelete = ProgressDialog(this)
        progressDialogDelete!!.setTitle("Please Wait...")
        progressDialogDelete!!.setMessage("Deleting account")
        progressDialogDelete!!.setCanceledOnTouchOutside(false)
        progressDialogDelete!!.setCancelable(false)

        progressDialogUpdate = ProgressDialog(this)
        progressDialogUpdate!!.setTitle("Please Wait...")
        progressDialogUpdate!!.setMessage("Updating account details")
        progressDialogUpdate!!.setCanceledOnTouchOutside(false)
        progressDialogUpdate!!.setCancelable(false)

        val receivedIntent = intent.getStringExtra("Intent").toString()
        val imageUri = intent.getStringExtra("New_Uri").toString()
        val name = intent.getStringExtra("New_Name").toString()
        val email = intent.getStringExtra("New_Email").toString()
        val number = intent.getStringExtra("New_Number").toString()
        val password = intent.getStringExtra("New_Password").toString()

        if (receivedIntent.equals("Delete_Account")){
            confirmationText!!.setText(R.string.delete_auth_text)
        }
        backBtn!!.setOnClickListener {
            if (receivedIntent.equals("Delete_Account")){
                startActivity(Intent(this, SecurityOptionsHelpPage::class.java))
                finish()
            } else {
                startActivity(Intent(this, EditInfoPage::class.java))
                finish()
            }
        }

        cfmBtn!!.setOnClickListener {
            val currentUser = auth!!.currentUser
            val enteredPassword = edtPassword!!.text.toString()
            val uid = currentUser!!.uid
            val fromUid = auth!!.uid
            val reference = FirebaseDatabase.getInstance().getReference("/Users/$uid")
            val latestMsgRef = FirebaseDatabase.getInstance().getReference("/Latest-messages/$uid")
            val userMsgRef = FirebaseDatabase.getInstance().getReference("/User-messages/$uid")

            val photoRef = FirebaseStorage.getInstance()
            val filename = UUID.randomUUID().toString()
            val storage = FirebaseStorage.getInstance().getReference("/Images/$filename")


            if (enteredPassword.isEmpty()){
                edtPassword!!.setError("Password required")
                edtPassword!!.requestFocus()
            }
            else{
                credential = EmailAuthProvider.getCredential(currentUser.email.toString(), enteredPassword)
                currentUser.reauthenticate(credential!!).addOnCompleteListener {
                    if (it.isSuccessful){
                        if (receivedIntent.equals("Delete_Account")){
                            progressDialogDelete!!.show()
                            currentUser.delete().addOnCompleteListener {
                                reference.addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        values = snapshot.getValue(User::class.java)
                                        if (values != null) {
                                            photoRef.getReferenceFromUrl(values!!.profileImageUrl.toString()).delete().addOnSuccessListener {
                                                latestMsgRef.removeValue()
                                                userMsgRef.removeValue()
                                                reference.removeValue()
                                            }.addOnFailureListener {
                                                progressDialogDelete!!.dismiss()
                                                Snackbar.make(confirmation_window, it.message.toString(), Snackbar.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                                progressDialogDelete!!.dismiss()
                                auth!!.signOut()
                                val i = Intent(this, LoginSignUpParent::class.java)
                                i.putExtra("Account_Deleted", "Account_Deleted")
                                startActivity(i)
                                finish()
                        }.addOnFailureListener {
                            progressDialogDelete!!.dismiss()
                            Snackbar.make(confirmation_window, it.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                    } else {
                            progressDialogUpdate!!.show()
                            reference.get().addOnSuccessListener {
                                if (it.exists()) {
                                    val savedImgUri = it.child("profileImageUrl").value.toString()
                                    val savedName = it.child("name").value.toString()
                                    val savedEmail = it.child("email").value.toString()
                                    val savedNumber = it.child("phoneNumber").value.toString()
                                    val savedPassword = it.child("password").value.toString()

                                    if (!imageUri.equals("Nothing")){
                                        photoRef.getReferenceFromUrl(savedImgUri).delete()
                                        storage.putFile(imageUri.toUri()).addOnSuccessListener {
                                            storage.downloadUrl.addOnSuccessListener {
                                                val userData = User(savedName,savedEmail,savedNumber,savedPassword,uid,it.toString())
                                                reference.setValue(userData)
                                            }.addOnFailureListener {
                                                progressDialogUpdate!!.dismiss()
                                                Snackbar.make(confirmation_window,it.message.toString(),Snackbar.LENGTH_LONG).show()
                                            }
                                        }.addOnFailureListener {
                                            progressDialogUpdate!!.dismiss()
                                            Snackbar.make(confirmation_window,it.message.toString(),Snackbar.LENGTH_LONG).show()
                                        }
                                    }
                                    if (!name.equals("Nothing")){
                                        val userData = User(name,savedEmail,savedNumber,savedPassword,uid,savedImgUri)
                                        reference.setValue(userData)
                                    }
                                    if (!email.equals("Nothing")){
                                        currentUser.updateEmail(email).addOnCompleteListener {
                                            if (it.isSuccessful){
                                                val userData = User(savedName,email,savedNumber,savedPassword,uid,savedImgUri)
                                                reference.setValue(userData)
                                            } else{
                                                progressDialogUpdate!!.dismiss()
                                                Snackbar.make(confirmation_window,it.exception.toString(),Snackbar.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                    if (!number.equals("Nothing")){
                                        val userData = User(savedName,savedEmail,number,savedPassword,uid,savedImgUri)
                                        reference.setValue(userData)
                                    }
                                    if (!password.equals("Nothing")){
                                        currentUser.updatePassword(password).addOnCompleteListener {
                                            if (it.isSuccessful){
                                                val userData = User(savedName,savedEmail,savedNumber,password,uid,savedImgUri)
                                                reference.setValue(userData)
                                            } else {
                                                progressDialogUpdate!!.dismiss()
                                                Snackbar.make(confirmation_window,it.exception.toString(),Snackbar.LENGTH_LONG).show()
                                            }
                                        }.addOnFailureListener {
                                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    progressDialogUpdate!!.dismiss()
                                    Snackbar.make(confirmation_window,"Account details updated successfully",Snackbar.LENGTH_LONG).show()
                                    handler = Handler()
                                    runnable = object : Runnable{
                                        override fun run() {
                                            startActivity(Intent(this@UserAuthPage, MainActivity::class.java))
                                            finish()
                                        }
                                    }
                                    handler!!.postDelayed(runnable!!, 500)
                                } else {
                                    Snackbar.make(confirmation_window,"User not found",Snackbar.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }.addOnFailureListener {
                    progressDialogDelete!!.dismiss()
                    Snackbar.make(confirmation_window, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
    companion object{
        var values:User?= null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val receivedIntent = intent.getStringExtra("Intent").toString()
        if (receivedIntent.equals("Delete_Account")){
            startActivity(Intent(this, SecurityOptionsHelpPage::class.java))
            finish()
        } else {
            startActivity(Intent(this, EditInfoPage::class.java))
            finish()
        }
    }
}