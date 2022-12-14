package com.example.fullstackapplication.utils

import android.provider.ContactsContract.Data
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
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
            return dataBase.getReference("BookMarkList")
        }

        fun getContentRef():DatabaseReference{
            return dataBase.getReference("content")
        }

        fun getChatListRef():DatabaseReference{
            return dataBase.getReference("ChatList")
        }

        fun getChatListMyFilterRef(user : String): Query {

            val data = FirebaseDatabase.getInstance().getReference("ChatList")
            val myChatList = data.orderByChild("recUser").equalTo(user)

            return myChatList
        }

        fun getJoinRef():DatabaseReference{
            return dataBase.getReference("JoinList")
        }

        fun getCommentRef():DatabaseReference{
            return dataBase.getReference("CommentList")
        }

        fun getLikeRef():DatabaseReference{
            return dataBase.getReference("LikeList")
        }

    }
}