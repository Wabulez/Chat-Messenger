package com.example.chatmessenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.chatmessenger.MainActivityPages.MainActivity
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_edit_info_page.*

class EditInfoPage : AppCompatActivity() {
    var backbtn: ImageView?= null
    var selectPhoto: Button?= null
    var selectedPhoto: CircleImageView?= null
    var profPicStatus: TextView?= null
    var selectedPhotoUri: Uri?= null
    var editedName:EditText ?= null
    var editedEmail:EditText ?= null
    var editedNumber:EditText ?= null
    var editedPassword:EditText ?= null
    var updateInfo:Button ?= null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info_page)
        backbtn = findViewById(R.id.backbtn)
        selectPhoto = findViewById(R.id.select_photo)
        selectedPhoto = findViewById(R.id.selected_img)
        profPicStatus = findViewById(R.id.profPic_status)
        editedName = findViewById(R.id.editInfo_user_name)
        editedEmail = findViewById(R.id.editInfo_user_email)
        editedNumber = findViewById(R.id.editInfo_user_number)
        editedPassword = findViewById(R.id.editInfo_user_password)
        updateInfo = findViewById(R.id.updateInfoBtn)

        backbtn!!.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        selectPhoto!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        updateInfo!!.setOnClickListener {
            val name = editedName!!.text.toString().trim()
            val email = editedEmail!!.text.toString().trim()
            val number = editedNumber!!.text.toString().trim()
            val password = editedPassword!!.text.toString().trim()
            val imageUri = selectedPhotoUri.toString().trim()
            val nothing = "Nothing"

            if (imageUri.equals("null") && name.isEmpty() && email.isEmpty() && number.isEmpty() && password.isEmpty()){
                Snackbar.make(edit_info_window,"Nothing changed",Snackbar.LENGTH_LONG).show()
            } else if (!email.isEmpty() && !email.matches(emailPattern.toRegex())){
                editedEmail!!.setError("Invalid email address")
                editedEmail!!.requestFocus()
            } else if (!number.isEmpty() && number.length != 10){
                editedNumber!!.setError("Invalid Phone Number")
                editedNumber!!.requestFocus()
            } else if (!password.isEmpty() && password.length < 6){
                editedPassword!!.setError("Password too short")
                editedPassword!!.requestFocus()
            }
            else {
                val i = Intent(this, UserAuthPage::class.java)
                i.putExtra("Intent", "Edit_Info")

                if (imageUri.equals("null")){
                    i.putExtra("New_Uri",nothing)
//                Snackbar.make(edit_info_window,"No image selected",Snackbar.LENGTH_LONG).show()
                } else {
                    i.putExtra("New_Uri",imageUri)
//                Snackbar.make(edit_info_window,imageUri,Snackbar.LENGTH_LONG).show()
                }
                if (name.isEmpty()){
                    i.putExtra("New_Name",nothing)
                } else {
                    i.putExtra("New_Name",name)
                }

                if (email.isEmpty()){
                    i.putExtra("New_Email",nothing)
                } else{
                    i.putExtra("New_Email",email)
                }

                if (number.isEmpty()){
                    i.putExtra("New_Number",nothing)
                } else {
                    i.putExtra("New_Number",number)
                }

                if (password.isEmpty()){
                    i.putExtra("New_Password",nothing)
                } else {
                    i.putExtra("New_Password",password)
                }
                startActivity(i)
                finish()
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

            if (bitmap != null){
                profPicStatus!!.setText("")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}