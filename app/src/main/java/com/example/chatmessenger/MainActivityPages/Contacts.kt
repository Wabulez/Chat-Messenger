package com.example.chatmessenger.MainActivityPages

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.chatmessenger.R
import com.example.chatmessenger.User
import com.example.chatmessenger.UserItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_contacts.*

class Contacts : AppCompatActivity() {
    var backBtn:ImageView ?= null
    var progressDialog:ProgressDialog ?= null
    var notice:TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        backBtn = findViewById(R.id.backbtn)
        notice = findViewById(R.id.no_contactsTxt)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please wait...")
        progressDialog!!.setMessage("Loading Contacts...")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)

        backBtn!!.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        fetchUsers()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers(){
        progressDialog!!.show()
        val ref = FirebaseDatabase.getInstance().getReference("/Users")
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                if (snapshot != null && snapshot.children != null && snapshot.children.iterator().hasNext()) {
                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        if (user != null) {
                            if (currentUser != user.uid) {
                                adapter.add(UserItem(user))
                                progressDialog!!.dismiss()
                            } else {
                                notice!!.setText(R.string.contacts_notice)
                                progressDialog!!.dismiss()
                            }
                        }
                    }
                } else {
                    notice!!.setText(R.string.contacts_notice)
                    progressDialog!!.dismiss()
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(this@Contacts, ChatLog::class.java)
                    intent.putExtra(USER_KEY,item.user)
                    startActivity(intent)
                    finish()
                }

                contacts_recycleView.adapter = adapter

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}