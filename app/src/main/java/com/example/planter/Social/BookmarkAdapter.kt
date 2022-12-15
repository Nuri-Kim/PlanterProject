package com.example.planter.Social

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.Post.PostVO
import com.example.planter.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BookmarkAdapter(var context: Context, var BookmarkList: ArrayList<PostVO>)
    : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    val database = Firebase.database

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvBookmarkTitle :TextView
        val tvBookmarkPostContent : TextView
        val tvBookmarkUserNick : TextView
        val tvBookmarkPostDate : TextView

        init {
            tvBookmarkTitle = itemView.findViewById(R.id.tvBookmarkTitle)
            tvBookmarkPostContent = itemView.findViewById(R.id.tvBookmarkPostContent)
            tvBookmarkUserNick = itemView.findViewById(R.id.tvBookmarkUserNick)
            tvBookmarkPostDate = itemView.findViewById(R.id.tvBookmarkPostDate)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.bookmark_list, null)
        return ViewHolder(view) //ViewHolder로 View를 보내줌
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBookmarkTitle.text = BookmarkList[position].title
        holder.tvBookmarkPostContent.text = BookmarkList[position].content
        holder.tvBookmarkUserNick.text = BookmarkList[position].uid

    }

    override fun getItemCount(): Int {
        return BookmarkList.size
    }


}