package com.example.planter.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.fullstackapplication.ChatVO
import com.example.planter.R


class ChatFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Views 초기화
        val etChatInput = view.findViewById<EditText>(R.id.etChatInput)
        val imgChatSend = view.findViewById<ImageView>(R.id.imgChatSend)

        val chatList = ArrayList<ChatVO>()




        return view
    }


}