package com.example.planter.Post

import android.content.Context
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.planter.R
import com.example.planter.utils.FBdataBase
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class PostAdapter(var context: Context, var PostList: ArrayList<PostVO>, var keyData: ArrayList<String>)
    :RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    val database = Firebase.database

    // 리스너 커스텀
    interface  OnItemClickListener{
        fun  onItemClick(view : View, position: Int)
    }

    // 객체 저장 변수 선언
    lateinit var mOnItemClickListener : OnItemClickListener

    //객체 전달 메서드
    fun setOnItemClickListener(OnItemClickListener : OnItemClickListener){

        mOnItemClickListener = OnItemClickListener
    }




    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

            val tvPostListTitle : TextView
            val tvPostListContent : TextView
            val tvPostListWriter : TextView
            val imgPostListPicture : ImageView
            val tvPostLikeCount : TextView
            val tvPostCmtCount : TextView

            init {
                tvPostListTitle = itemView.findViewById(R.id.tvPostListTitle)
                tvPostListContent = itemView.findViewById(R.id.tvPostListContent)
                tvPostListWriter = itemView.findViewById(R.id.tvPostListWriter)
                imgPostListPicture = itemView.findViewById(R.id.imgPostListPicture)
                tvPostLikeCount = itemView.findViewById(R.id.tvPostLikeCount)
                tvPostCmtCount = itemView.findViewById(R.id.tvPostCmtCount)

                itemView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        // 버그로 인해 -1이 아닐경우에
                        mOnItemClickListener.onItemClick(itemView,position)
                    }
                }





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
        holder.tvPostListWriter.text = PostList[position].nick


        FBdataBase.getCommentRef().child(keyData[position]).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.tvPostCmtCount.text = snapshot.childrenCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        FBdataBase.getLikeRef().child(keyData[position]).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.tvPostLikeCount.text = snapshot.childrenCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




    }

    override fun getItemCount(): Int {
        return PostList.size //항목의 갯수
    }



}