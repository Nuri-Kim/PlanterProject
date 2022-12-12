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
        val imgChatList : ImageView
        val tvChatTemplateName : TextView
        val tvChatTemplateContent : TextView
        val tvChatTemplateMyContent : TextView

        init {
            imgChatList=itemView.findViewById(R.id.imgChatList)
            tvChatTemplateName=itemView.findViewById(R.id.tvChatTemplateName)
            tvChatTemplateContent=itemView.findViewById(R.id.tvChatTemplateContent)
            tvChatTemplateMyContent=itemView.findViewById(R.id.tvChatTemplateMyContent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.chat_list_template,null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}