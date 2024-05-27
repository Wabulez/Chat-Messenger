package com.example.chatmessenger

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.view.*
import kotlinx.android.synthetic.main.chat_log_sender_row.view.*

class ChatFromItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.sender_msg.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_log_sender_row
    }
}