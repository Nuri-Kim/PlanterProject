package com.example.fullstackapplication.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBdataBase {

    // realTime dataBase 사용은 이 클라스를 통해 진행

    companion object{
        val dataBase = Firebase.database

        fun getBoardRef():DatabaseReference{
            return dataBase.getReference("board")
        }

        fun getBookMarkRef():DatabaseReference{
            return dataBase.getReference("bookMarkList")
        }

        fun getContentRef():DatabaseReference{
            return dataBase.getReference("content")
        }
    }
}