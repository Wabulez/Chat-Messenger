package com.example.chatmessenger.LoginSignUpPages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.example.chatmessenger.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class LoginSignUpParent : AppCompatActivity() {
    var loginSignUpParent_page:ConstraintLayout ?= null
    var viewPager:ViewPager ?= null
    var tabLayout:TabLayout ?= null
    var x:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup_parent)
        viewPager = findViewById(R.id.page_Viewer)
        tabLayout = findViewById(R.id.tab_layout)
        loginSignUpParent_page = findViewById(R.id.loginSignupParent_page)

        val logoutValue = intent.getStringExtra("Logout").toString()
        val accountDeleted = intent.getStringExtra("Account_Deleted").toString()
        if (logoutValue.equals("Logout")){
            Snackbar.make(loginSignUpParent_page!!,"Current user logged out", Snackbar.LENGTH_LONG).show()
        }
        if (accountDeleted.equals("Account_Deleted")){
            Snackbar.make(loginSignUpParent_page!!,"Account Deletion Successful", Snackbar.LENGTH_LONG).show()
        }

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Login"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Register"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = LoginAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}
