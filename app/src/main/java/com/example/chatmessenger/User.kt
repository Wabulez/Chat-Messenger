package com.example.chatmessenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val name:String ?= null, val email:String ?= null, val phoneNumber:String ?= null, val password:String ?= null, val uid:String ?= null, val profileImageUrl:String ?= null):Parcelable{
    constructor():this("","","","","","")
}
