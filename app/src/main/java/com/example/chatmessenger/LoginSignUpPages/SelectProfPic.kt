package com.example.chatmessenger.LoginSignUpPages

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.example.chatmessenger.R
import com.example.chatmessenger.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class SelectProfPic : AppCompatActivity() {
    var auth: FirebaseAuth?= null
    var selectedPhotoUri: Uri?= null
    var selectPhoto:Button ?= null
    var selectedPhoto:CircleImageView ?= null
    var profPicStatus:TextView ?= null
    var createAccount:Button ?= null
    var selectPicLayout:ConstraintLayout ?= null
    var progressDialog:ProgressDialog ?= null
    var handler:Handler ?= null
    var runnable:Runnable ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_prof_pic)
        selectPhoto = findViewById(R.id.select_photo)
        selectedPhoto = findViewById(R.id.selected_img)
        profPicStatus = findViewById(R.id.profPic_status)
        createAccount = findViewById(R.id.create_accountBtn)
        selectPicLayout = findViewById(R.id.selectPic_layout)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setMessage("Creating Account...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)

        val name = intent.getStringExtra("name").toString()
        val email = intent.getStringExtra("email").toString()
        val number = intent.getStringExtra("number").toString()
        val password = intent.getStringExtra("password").toString()

        selectPhoto!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        createAccount!!.setOnClickListener {
            if (profPicStatus!!.text.equals("No profile picture selected")){
                Snackbar.make(selectPicLayout!!, "Please select a profile picture to continue", Snackbar.LENGTH_LONG).show()
            } else {
                progressDialog!!.show()
                auth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val uid = FirebaseAuth.getInstance().uid
                        val filename = UUID.randomUUID().toString()
                        val storage = FirebaseStorage.getInstance().getReference("/Images/$filename")

                        if (selectedPhotoUri == null) {
                            return@addOnCompleteListener
                        }

                        storage.putFile(selectedPhotoUri!!).addOnSuccessListener {
                            storage.downloadUrl.addOnSuccessListener {
                                val userData = User(name,email,number,password,uid,it.toString())
                                val reference = FirebaseDatabase.getInstance().getReference("/Users/$uid")

                                reference.setValue(userData).addOnSuccessListener {
                                    progressDialog!!.dismiss()
                                    var snackbar = Snackbar.make(selectPicLayout!!,"Account created successfully", Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                    handler = Handler()
                                    runnable = object : Runnable {
                                        override fun run() {
                                            var i = Intent(this@SelectProfPic, MainActivity::class.java)
                                            i.putExtra("Login","Login")
                                            startActivity(i)
                                            finish()
                                        }
                                    }
                                    handler!!.postDelayed(runnable!!,500)
                                }.addOnFailureListener {
                                    progressDialog!!.dismiss()
                                    Snackbar.make(selectPicLayout!!,it.message.toString(), Snackbar.LENGTH_LONG).show()
                                }
                            }.addOnFailureListener {
                                progressDialog!!.dismiss()
                                Snackbar.make(selectPicLayout!!,it.message.toString(), Snackbar.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener{
                            progressDialog!!.dismiss()
                            Snackbar.make(selectPicLayout!!,it.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }.addOnFailureListener {
                    progressDialog!!.dismiss()
                    Snackbar.make(selectPicLayout!!,it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectedPhoto!!.setImageBitmap(bitmap)
            selectPhoto!!.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectPhoto!!.setBackgroundDrawable(bitmapDrawable)
            if (bitmap != null){
                profPicStatus!!.setText("")
            }
        }
    }

    private fun uploadImageToFirebaseStorage(){
//        if (selectedPhotoUri == null) return
//
//        val filename = UUID.randomUUID().toString()
//        val storage = FirebaseStorage.getInstance().getReference("/Images/$filename")
//
//        storage.putFile(selectedPhotoUri!!).addOnSuccessListener {
//
//        }
//        storage.downloadUrl.addOnCompleteListener {
//
//        }
    }
}