package com.example.planter.Social

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.R
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBdataBase.Companion.getCommentRef
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CommentAdapter(var context: Context, var commentList : ArrayList<CommentVO>,
                     var keyData: ArrayList<String>, var key:String)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    val database = Firebase.database

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val tvCommentNick : TextView
        val tvCommentContent : TextView
        val tvCommentTime: TextView
        val tvCommentDelete: TextView

        init {
            tvCommentNick = itemView.findViewById(R.id.tvCommentNick)
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent)
            tvCommentTime = itemView.findViewById(R.id.tvCommentTime)
            tvCommentDelete = itemView.findViewById(R.id.tvCommentDelete)

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

        holder.tvCommentDelete.visibility = View.INVISIBLE

        if(FBAuth.getUid()==commentList.get(position).id){
            holder.tvCommentDelete.visibility = View.VISIBLE
        }


        holder.tvCommentDelete.setOnClickListener {
            getCommentRef().child(key).child(keyData[position]).removeValue()
        }

    }

    override fun getItemCount(): Int {
        return commentList.size
    }


}