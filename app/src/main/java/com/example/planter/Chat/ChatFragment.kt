package com.example.planter.Chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fullstackapplication.ChatVO
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.R


class ChatFragment : Fragment() {

    // 로그인 user 전체 채팅 목록 보여주는 창


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Views 초기화
        val rvChat = view.findViewById<RecyclerView>(R.id.rvChat)

        FBdataBase.getChatListRef()


        val chatList = ArrayList<ChatVO>()




        return view
    }


}