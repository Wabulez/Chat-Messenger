package com.example.chatmessenger.MainActivityPages

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.chatmessenger.ChatMessage
import com.example.chatmessenger.LatestMessageRow
import com.example.chatmessenger.LoginSignUpPages.LoginSignUpParent
import com.example.chatmessenger.MainActivityPages.Contacts.Companion.USER_KEY
import com.example.chatmessenger.R
import com.example.chatmessenger.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chats.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
    var auth:FirebaseAuth ?= null
    var signout:ImageView ?= null
    var chatsRecyclerView:RecyclerView ?= null
    var chatsLayout:ConstraintLayout ?= null
    var notice:TextView ?= null
    var fab:FloatingActionButton ?= null
    val adapter = GroupAdapter<ViewHolder>()
    val latestMessagesMap = HashMap<String, ChatMessage>()
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
        var root = inflater.inflate(R.layout.fragment_chats, container, false)
        chatsLayout = root.findViewById(R.id.chats_window)
        signout = root.findViewById(R.id.signoutBtn)
        fab = root.findViewById(R.id.open_contacts)
        chatsRecyclerView = root.findViewById(R.id.chats_recyclerView)
        notice = root.findViewById(R.id.chats_noticeMsg)
        auth = FirebaseAuth.getInstance()
        chatsRecyclerView!!.adapter = adapter
        chatsRecyclerView!!.addItemDecoration(DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val row = item as LatestMessageRow
            val i = Intent(getActivity(),ChatLog::class.java)
            i.putExtra(USER_KEY, row.ChatPartnerUser)
            startActivity(i)
        }

        progressDialog = ProgressDialog(getActivity())
        progressDialog!!.setTitle("Please wait...")
        progressDialog!!.setMessage("Loading chats...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()

        listenForNewMessages()

        var fromId =FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("/Latest-messages/$fromId")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null && snapshot.children != null && snapshot.children.iterator().hasNext()){
                    progressDialog!!.dismiss()
                }else{
                    notice!!.setText(R.string.chats_notice)
                    progressDialog!!.dismiss()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        signout!!.setOnClickListener {
            auth!!.signOut()
            var i = Intent(getActivity(), LoginSignUpParent::class.java).apply {
                putExtra("Logout","Logout")
            }
            startActivity(i)
            activity?.finish()
        }
        fab!!.setOnClickListener {
            startActivity(Intent(getActivity(), Contacts::class.java))
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
         * @return A new instance of fragment ChatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun listenForNewMessages(){
        val fromId =FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Latest-messages/$fromId")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        progressDialog!!.dismiss()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }
}