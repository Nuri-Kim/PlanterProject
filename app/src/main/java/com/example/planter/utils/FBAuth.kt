package com.example.fullstackapplication.utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object{
        lateinit var auth : FirebaseAuth

        // 현재 사용자 uid 가져오는 함수
        fun getUid():String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser!!.uid
        }

        // 현재 시간 가져오는 함수
        fun getTime():String{
            // Calendar 객체는 getInstance() 매소드로 생성
            val currentTime = Calendar.getInstance().time
            val time = SimpleDateFormat(
                "yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentTime)

            return time
        }
    }
}