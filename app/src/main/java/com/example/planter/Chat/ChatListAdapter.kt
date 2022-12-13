package com.example.planter.Chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fullstackapplication.ChatVO
import com.example.planter.R


    // 현재 로그인 유저 전체 채팅 목록
class ChatListAdapter (val context: Context,
                       val chatList : ArrayList<ChatVO>,
                       val loginId : String) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {


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
        val imgChatList : ImageView
        val tvChatListOther : TextView
        val tvChatListContent : TextView
        val tvChatTime : TextView

        init {
            imgChatList=itemView.findViewById(R.id.imgChatList)
            tvChatListOther=itemView.findViewById(R.id.tvChatListOther)
            tvChatListContent=itemView.findViewById(R.id.tvChatListContent)
            tvChatTime = itemView.findViewById(R.id.tvChatTime)


            // 채팅 리스트 내 행 클릭 시 채팅창으로 이동
            itemView.setOnClickListener{
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recUser = chatList[position].recUser

        // fireBase chatList 내 receiver 가 login user 와 일치하는 경우
        // 같은 발신자가 보낸 메세제 통합 작업 필요
        // 다음 position 발신자가 이미 리스트에 있다면 데이터 대체하기

        if( "receiver" == recUser){
            holder.imgChatList.setImageResource(R.drawable.de_profile)
            holder.tvChatListOther.text = chatList[position].sendUser
            holder.tvChatListContent.text = chatList[position].msg
            holder.tvChatTime.text = chatList[position].time
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}