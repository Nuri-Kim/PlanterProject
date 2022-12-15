package com.example.planter.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.utils.FBdataBase
import com.example.planter.ChatVO
import com.example.planter.utils.FBAuth
import com.example.planter.R
import com.example.planter.UserAuth.JoinVO
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class ChatFragment : Fragment() {

    // 로그인 user 전체 채팅 목록 보여주는 창

    val userList = ArrayList<JoinVO>()
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


        //더미데이터 생성
        val chatListRef = FBdataBase.getChatListRef()
        val nowTime = FBAuth.getTime()
        //chatListRef.push().setValue(ChatVO(user,"누리","7pznDONL9xce9CB7xue6goh6F362","test msg","",nowTime))








        // 어댑터 생성
        adapter = ChatFragmentAdapter(requireContext(),userList)
        rvChat.adapter = adapter
        rvChat.layoutManager = GridLayoutManager(requireContext(),2)






        // 객체 클릭 이벤트
        adapter.setOnItemClickListener(object : ChatFragmentAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(requireContext(),ChatActivity::class.java)


                startActivity(intent)

            }
        })


        return view
    }

    fun getChatList(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(model in snapshot.children){
                    val item = model.getValue(JoinVO::class.java)
                    if(item != null){
                        userList.add(item)
                    }
                    keyData.add(model.key.toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        // 모든 유저 가져오기
        FBdataBase.getJoinRef().addValueEventListener(postListener)

    }


}