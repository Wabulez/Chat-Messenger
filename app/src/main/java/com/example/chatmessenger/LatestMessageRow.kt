package com.example.chatmessenger

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>() {
    var ChatPartnerUser: User ?= null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.latest_msg_chats.text = chatMessage.text
        val chatParterId:String

        if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatParterId = chatMessage.toId
        } else {
            chatParterId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/Users/$chatParterId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ChatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.username_chats.text = ChatPartnerUser?.name
                val targetImageView = viewHolder.itemView.prof_pic_chats
                Picasso.get().load(ChatPartnerUser?.profileImageUrl).into(targetImageView)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }
}