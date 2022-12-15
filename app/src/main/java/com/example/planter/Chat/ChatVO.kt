package com.example.planter

data class ChatVO(val sendUser : String,val senderName : String,val recUser:String, val msg : String, val img : String, val time : String) {

    constructor() : this("","","","","", "")

}