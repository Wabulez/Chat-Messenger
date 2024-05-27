package com.example.chatmessenger.MainActivityPages

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatmessenger.EditInfoPage
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.example.chatmessenger.R
import com.example.chatmessenger.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    var auth:FirebaseAuth ?= null
    var signout: ImageView?= null
    var loginLayout: ConstraintLayout?= null
    var nameActionBar:TextView ?= null
    var nameInfo:TextView ?= null
    var emailInfo:TextView ?= null
    var numberInfo:TextView ?= null
    var profPic:CircleImageView ?= null
    var editInfo:Button ?= null
    var progressDialog:ProgressDialog ?= null
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
        var root = inflater.inflate(R.layout.fragment_profile, container, false)
        signout = root.findViewById(R.id.signoutBtn)
        loginLayout = root.findViewById(R.id.loginLayout)
        nameInfo = root.findViewById(R.id.user_name_profile)
        nameActionBar = root.findViewById(R.id.username_profile)
        emailInfo = root.findViewById(R.id.user_email_profile)
        numberInfo = root.findViewById(R.id.user_number_profile)
        profPic = root.findViewById(R.id.prof_pic_profile)
        editInfo = root.findViewById(R.id.editInfoBtn)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(getActivity())
        progressDialog!!.setTitle("Please wait...")
        progressDialog!!.setMessage("Loading information...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()

        fetchCurrentUser()

        signout!!.setOnClickListener {
            auth!!.signOut()
            var i = Intent(getActivity(), LoginSignUpParent::class.java).apply {
                putExtra("Logout","Logout")
            }
            startActivity(i)
            activity?.finish()
        }
        editInfo!!.setOnClickListener {
            startActivity(Intent(getActivity(),EditInfoPage::class.java))
            activity?.finish()
        }
        return root
    }

    companion object {
        var currentUser:User ?= null
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun fetchCurrentUser(){
        val uid = auth!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                if (currentUser != null){
                    val abTxt = "Welcome, ${currentUser!!.name}"
                    Picasso.get().load(currentUser!!.profileImageUrl).into(profPic)
                    nameActionBar!!.setText(abTxt)
                    nameInfo!!.setText(currentUser!!.name)
                    emailInfo!!.setText(currentUser!!.email)
                    numberInfo!!.setText(currentUser!!.phoneNumber)
                    progressDialog!!.dismiss()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}