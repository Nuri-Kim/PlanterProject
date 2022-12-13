package com.example.planter.Post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostAdapter(var context: Context, var PostList: ArrayList<PostVO>)
    :RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    val database = Firebase.database

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

            val tvPostListTitle : TextView
            val tvPostListContent : TextView
            val tvPostListCategory : TextView

            init {
                tvPostListTitle = itemView.findViewById(R.id.tvPostListTitle)
                tvPostListContent = itemView.findViewById(R.id.tvPostListContent)
                tvPostListCategory = itemView.findViewById(R.id.tvPostListCategory)


            }

    }




    //xml파일을 view로, imflat작업 필요
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.post_list, null)
        return ViewHolder(view) //ViewHolder로 View를 보내줌
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvPostListTitle.text = PostList[position].title
        holder.tvPostListContent.text = PostList[position].content
        holder.tvPostListCategory.text = PostList[position].category


    }

    override fun getItemCount(): Int {
        return PostList.size //항목의 갯수
    }
}