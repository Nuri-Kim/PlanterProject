package com.example.planter.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fullstackapplication.ChatVO
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)



        // 실질적 채팅하는 창

        val user = FBAuth.getUid()

        val etChatInput = findViewById<EditText>(R.id.etChatInput)
        val imgChatSend = findViewById<ImageView>(R.id.imgChatSend)
        val rvChat = findViewById<RecyclerView>(R.id.rvChat)

        val chatList = ArrayList<ChatVO>()

        val chatListRef = FBdataBase.getChatListRef()
        val nowTime = FBAuth.getTime()

        val adapter = ChatActivityAdapter(this,chatList,user)
        rvChat.adapter=adapter
        rvChat.layoutManager=LinearLayoutManager(this)

        imgChatSend.setOnClickListener{
            val msg = etChatInput.text.toString()
            chatListRef.push().setValue(ChatVO(user,"sender",msg,"",nowTime))

            etChatInput.text = null
        }

        chatListRef.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue<ChatVO> () as ChatVO

                chatList.add(chatItem)

                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }
}