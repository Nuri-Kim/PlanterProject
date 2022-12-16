package com.example.planter.Chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.planter.ChatVO
import com.example.planter.R
import com.example.planter.UserAuth.JoinVO
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


// 현재 로그인 유저 전체 채팅 목록

class ChatFragmentAdapter (val context: Context,
                           val userList : ArrayList<JoinVO>,
                          ) : RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder>() {


    // 없는 기능을 인터페이스를 생성하여 커스텀하여 사용
    // 리스너 커스텀
    interface OnItemClickListener{
        fun onItemClick(view : View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener:OnItemClickListener

    // 객체 전달 메서드
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }



    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvChatListOther : TextView
        val imgChatList : ImageView
        val btnChatList : Button
        init {
            tvChatListOther=itemView.findViewById(R.id.tvChatListOther)
            imgChatList = itemView.findViewById(R.id.imgChatList)
            btnChatList = itemView.findViewById(R.id.btnChatList)


            // 채팅 리스트 내 행 클릭 시 채팅창으로 이동
            btnChatList.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    mOnItemClickListener.onItemClick(itemView,position)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.chat_list_template,null)

        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        //imgChatList.setImageResource(R.drawable.de_profile)
        holder.tvChatListOther.text = userList[position].nick




        // fireBase chatList 내 receiver 가 login user 와 일치하는 경우
        // 같은 발신자가 보낸 메세제 통합 작업 필요
        // 다음 position 발신자가 이미 리스트에 있다면 데이터 대체하기


        val storageReference = Firebase.storage.reference.child("${userList[position].uid}.png")

            storageReference.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                    Glide.with(context)
                        .load(task.result)
                        .into(holder.imgChatList) //지역변수

                }
            }



    }

    override fun getItemCount(): Int {
        return userList.size
    }




}