package com.example.planter.Social

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.Post.PostAdapter
import com.example.planter.Post.PostVO
import com.example.planter.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BookmarkAdapter(var context: Context, var PostList:ArrayList<PostVO>
, var BookmarkList: ArrayList<String>, var keyData:ArrayList<String>)
    : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    val database = Firebase.database

    interface  OnItemClickListener{
        fun  onItemClick(view : View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener : BookmarkAdapter.OnItemClickListener

    //객체 전달 메서드
    fun setOnItemClickListener(OnItemClickListener : BookmarkAdapter.OnItemClickListener){

        mOnItemClickListener = OnItemClickListener
    }

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

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    // 버그로 인해 -1이 아닐경우에
                    mOnItemClickListener.onItemClick(itemView,position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.bookmark_list, null)
        return ViewHolder(view) //ViewHolder로 View를 보내줌
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvBookmarkUserNick.text = PostList[position].uid
        holder.tvBookmarkPostContent.text = PostList[position].content
        holder.tvBookmarkTitle.text = PostList[position].title
        holder.tvBookmarkUserNick.text = PostList[position].nick

    }

    override fun getItemCount(): Int {
        return PostList.size
    }


}