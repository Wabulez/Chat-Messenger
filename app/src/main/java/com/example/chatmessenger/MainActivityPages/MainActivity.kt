package com.example.chatmessenger.MainActivityPages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.chatmessenger.R
import com.example.chatmessenger.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    var bottomNavbar:BottomNavigationView ?= null
    var mainLayout:ConstraintLayout ?= null
    private val chatsFragment = ChatsFragment()
    private val profileFragment = ProfileFragment()
    private val settingsFragment = SettingsFragment()
    var auth:FirebaseAuth ?= null
    var ref:DatabaseReference ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavbar = findViewById(R.id.bottom_navbar)
        mainLayout = findViewById(R.id.activity_main_layout)
        replaceFragment(chatsFragment)

        checkUser()

        bottomNavbar!!.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_chats -> replaceFragment(chatsFragment)
                R.id.ic_profile -> replaceFragment(profileFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
            }
            true
        }
    }
    private fun replaceFragment(fragment:Fragment){
        if (fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_main_layout, fragment)
            transaction.commit()
        }
    }
    private fun checkUser(){
        val login = intent.getStringExtra("Login").toString()

        if (login.equals("Login")){
            ref = FirebaseDatabase.getInstance().reference
            auth = FirebaseAuth.getInstance()
            var uid = auth!!.currentUser?.uid
            var uidRef = ref!!.child("Users").child(uid!!)
            uidRef.get().addOnCompleteListener {
                if (it.isSuccessful){
                    val snapshot = it.result
                    val name = snapshot.child("name").getValue(String::class.java)
                    Snackbar.make(mainLayout!!,"Welcome, $name",Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(mainLayout!!,it.exception!!.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}