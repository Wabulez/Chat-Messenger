package com.example.chatmessenger.MainActivityPages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.HelpPages.HelpPage
import com.example.chatmessenger.HelpPages.SecurityOptionsHelpPage
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.example.chatmessenger.R
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    var auth: FirebaseAuth?= null
    var signout: ImageView?= null
    var helpBtn:Button ?= null
    var securityOptionsBtn:Button ?= null
    var loginLayout: ConstraintLayout?= null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_settings, container, false)
        signout = root.findViewById(R.id.signoutBtn)
        helpBtn = root.findViewById(R.id.helpBtn)
        securityOptionsBtn = root.findViewById(R.id.securityOptionsBtn)
        loginLayout = root.findViewById(R.id.loginLayout)
        auth = FirebaseAuth.getInstance()

        signout!!.setOnClickListener {
            auth!!.signOut()
            var i = Intent(getActivity(), LoginSignUpParent::class.java).apply {
                putExtra("Logout","Logout")
            }
            startActivity(i)
            activity?.finish()
        }

        helpBtn!!.setOnClickListener {
            startActivity(Intent(getActivity(), HelpPage::class.java))
            activity?.finish()
        }

        securityOptionsBtn!!.setOnClickListener {
            startActivity(Intent(getActivity(), SecurityOptionsHelpPage::class.java))
            activity?.finish()
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}