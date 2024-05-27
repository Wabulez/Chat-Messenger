package com.example.chatmessenger

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.contacts_new_message.view.*

class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.name
        viewHolder.itemView.email.text = user.email
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.prof_pic_contacts)
    }

    override fun getLayout(): Int {
        return R.layout.contacts_new_message
    }
}