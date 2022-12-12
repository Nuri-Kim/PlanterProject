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

class ChatAdapter (val context: Context,
                   val chatList : ArrayList<ChatVO>,
                   val loginId : String) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imgChat : ImageView
        val tvChatListName : TextView
        val tvChatListContent : TextView
        val tvChatListMyContent : TextView

        init {
            imgChat=itemView.findViewById(R.id.imgChatList)
            tvChatListName=itemView.findViewById(R.id.tvChatListName)
            tvChatListContent=itemView.findViewById(R.id.tvChatListContent)
            tvChatListMyContent=itemView.findViewById(R.id.tvChatListMyContent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.chat_list,null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}