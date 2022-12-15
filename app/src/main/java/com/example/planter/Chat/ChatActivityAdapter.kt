package com.example.planter.Chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.ChatVO
import com.example.planter.R

class ChatActivityAdapter(val context: Context,
                          val chatList : ArrayList<ChatVO>,
                          val loginId : String):RecyclerView.Adapter<ChatActivityAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val imgChatList : ImageView
        val tvChatTemplateName : TextView
        val tvChatTemplateContent : TextView
        val tvChatTemplateMyContent : TextView
        val tvChatTemplateTime : TextView
        val tvChatTemplateMyTime : TextView

        init {

             imgChatList = itemView.findViewById(R.id.imgChatList)
             tvChatTemplateName = itemView.findViewById(R.id.tvChatTemplateName)
             tvChatTemplateContent = itemView.findViewById(R.id.tvChatTemplateContent)
             tvChatTemplateMyContent = itemView.findViewById(R.id.tvChatTemplateMyContent)
             tvChatTemplateTime = itemView.findViewById(R.id.tvChatTemplateTime)
             tvChatTemplateMyTime = itemView.findViewById(R.id.tvChatTemplateMyTime)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.chat_template,null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = chatList[position].sendUser

        if( loginId == name){
            holder.imgChatList.visibility=View.GONE
            holder.tvChatTemplateName.visibility=View.GONE
            holder.tvChatTemplateContent.visibility=View.GONE
            holder.tvChatTemplateTime.visibility=View.GONE

            holder.tvChatTemplateMyContent.visibility=View.VISIBLE
            holder.tvChatTemplateMyTime.visibility=View.VISIBLE

            holder.tvChatTemplateMyContent.text=chatList[position].msg
            holder.tvChatTemplateMyTime.text=chatList[position].time

        }else{

            holder.imgChatList.visibility=View.VISIBLE
            holder.tvChatTemplateName.visibility=View.VISIBLE
            holder.tvChatTemplateContent.visibility=View.VISIBLE
            holder.tvChatTemplateTime.visibility=View.VISIBLE

            holder.tvChatTemplateMyContent.visibility=View.GONE
            holder.tvChatTemplateMyTime.visibility=View.GONE

            holder.imgChatList.setImageResource(R.drawable.de_profile)
            holder.tvChatTemplateName.text=chatList[position].sendUser
            holder.tvChatTemplateContent.text=chatList[position].msg
            holder.tvChatTemplateTime.text = chatList[position].time

        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

}