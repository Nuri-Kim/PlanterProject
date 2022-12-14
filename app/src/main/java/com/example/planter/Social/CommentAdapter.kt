package com.example.planter.Social

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.R

class CommentAdapter(var context: Context, var commentList : ArrayList<CommentVO>)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val tvCommentNick : TextView
        val tvCommentContent : TextView
        val tvCommentTime: TextView

        init {
            tvCommentNick = itemView.findViewById(R.id.tvCommentNick)
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent)
            tvCommentTime = itemView.findViewById(R.id.tvCommentTime)

            itemView.setOnClickListener {
                val position = adapterPosition

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.comment_list, null)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCommentNick.setText(commentList.get(position).nick)
        holder.tvCommentContent.setText(commentList.get(position).content)
        holder.tvCommentTime.setText(commentList.get(position).time)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }


}