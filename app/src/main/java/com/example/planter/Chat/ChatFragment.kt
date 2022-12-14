package com.example.planter.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fullstackapplication.ChatVO
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class ChatFragment : Fragment() {

    // 로그인 user 전체 채팅 목록 보여주는 창

    val chatList = ArrayList<ChatVO>()
    lateinit var adapter : ChatFragmentAdapter
    var keyData = ArrayList<String>()
    val user = FBAuth.getUid()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_chat, container, false)


        // Views 초기화
        val rvChat = view.findViewById<RecyclerView>(R.id.rvChat)

        getChatList()

        val chatListRef = FBdataBase.getChatListRef()
        val nowTime = FBAuth.getTime()
        //chatListRef.push().setValue(ChatVO("edxi2TvzmvWhaL0VZb1kCcpmNGj2",user,"test msg","",nowTime))








        // 어댑터 생성
        adapter = ChatFragmentAdapter(requireContext(),chatList)
        rvChat.adapter = adapter
        rvChat.layoutManager = LinearLayoutManager(requireContext())






        // 객체 클릭 이벤트
        adapter.setOnItemClickListener(object : ChatFragmentAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(requireContext(),ChatActivity::class.java)
                intent.putExtra("sendUser",chatList[position].sendUser)
                intent.putExtra("msg",chatList[position].msg)
                intent.putExtra("img",chatList[position].img)
                intent.putExtra("time",chatList[position].time)

                startActivity(intent)

            }
        })


        return view
    }

    fun getChatList(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for(model in snapshot.children){
                    val item = model.getValue(ChatVO::class.java)
                    if(item != null){
                        chatList.add(item)
                    }
                    keyData.add(model.key.toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }


        FBdataBase.getChatListMyFilterRef(user).addValueEventListener(postListener)

    }


}