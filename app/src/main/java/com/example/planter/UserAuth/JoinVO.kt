package com.example.planter.UserAuth

data class JoinVO(val email : String, val nick : String,  val waterAlram : Boolean, val messageAlram : Boolean) {


    // 물주기알림 메시지 알람 허용
    constructor() : this("","",true,true)
}

