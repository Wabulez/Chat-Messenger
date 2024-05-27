package com.example.chatmessenger.LoginSignUpPages

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.chatmessenger.LoginSignUpPages.LoginTabFragment
import com.example.chatmessenger.LoginSignUpPages.SignUpTabFragment

class LoginAdapter(private val context: Context, fm: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem (position:Int): Fragment {
        when(position){
            0->{
                //val logintabfragment:LoginTabFragment = LoginTabFragment()
                return LoginTabFragment()
            }
            1->{
                return SignUpTabFragment()
            }
            else->{
                return SignUpTabFragment()
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
