package com.example.planter.UserAuth

data class JoinVO(val email : String, val nick : String, val uid : String) {


    // 물주기알림 메시지 알람 허용
    constructor() : this("","","")
}

