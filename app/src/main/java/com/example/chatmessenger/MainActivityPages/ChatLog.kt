package com.example.chatmessenger.MainActivityPages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.chatmessenger.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLog : AppCompatActivity() {
    var backBtn:ImageView ?= null
    var username:TextView ?= null
    var profPicChatLog:CircleImageView ?= null
    val adapter = GroupAdapter<ViewHolder>()
    var user:User ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        backBtn = findViewById(R.id.backbtn)
        username = findViewById(R.id.user_name)
        profPicChatLog = findViewById(R.id.prof_pic_chat_log)

        user = intent.getParcelableExtra<User>(Contacts.USER_KEY)
        if (user != null) {
            username!!.setText(user!!.name)
            Picasso.get().load(user!!.profileImageUrl).into(profPicChatLog)
        }

        chat_log_recycleView.adapter = adapter

        checkForNewMessages()

        backBtn!!.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        sendBtn.setOnClickListener {
            if (edtMessage.text.toString().isEmpty()){
                Snackbar.make(chat_log_window,"Enter a message to send",Snackbar.LENGTH_LONG).show()
            } else {
                sendMessage()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun checkForNewMessages(){
        var toId = FirebaseAuth.getInstance().uid
        val fromId = user?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/User-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null){
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatToItem(chatMessage.text))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
                }
                chat_log_recycleView.scrollToPosition(adapter.itemCount -1)
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }
    private fun sendMessage(){
        val text = edtMessage.text.toString()
        val user = intent.getParcelableExtra<User>(Contacts.USER_KEY)
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = user?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/User-messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/User-messages/$toId/$fromId").push()
        val chatMessage = ChatMessage(ref.key!!,text,fromId,toId,System.currentTimeMillis()/1000)

        ref.setValue(chatMessage).addOnSuccessListener {
//            Snackbar.make(chat_log_window,"Saved our chat message: ${ref.key}",Snackbar.LENGTH_LONG).show()
            edtMessage.text.clear()
            chat_log_recycleView.scrollToPosition(adapter.itemCount - 1)
        }
        toRef.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/Latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/Latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }
}