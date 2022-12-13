package com.example.planter.Post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.planter.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostDetailActivity : AppCompatActivity() {

    lateinit var imgPostDetailPicture : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        imgPostDetailPicture = findViewById(R.id.imgPostDetailPicture)

        val imgPostDetailUserPic = findViewById<ImageView>(R.id.imgPostDetailUserPic)
        val tvPostDetailTime = findViewById<TextView>(R.id.tvPostDetailTime)
        val tvPostDetailTitle = findViewById<TextView>(R.id.tvPostDetailTitle)
        val tvPostDetailUserNick = findViewById<TextView>(R.id.tvPostDetailUserNick)
        val tvPostDetailCategory = findViewById<TextView>(R.id.tvPostDetailCategory)
        val tvPostDetailModify = findViewById<TextView>(R.id.tvPostDetailModify)
        val tvPostDetailDelete = findViewById<TextView>(R.id.tvPostDetailDelete)
        val tvPostDetailContent = findViewById<TextView>(R.id.tvPostDetailContent)
        val tvPostDetailCount = findViewById<TextView>(R.id.tvPostDetailCount)
        val tvPostDetailWrite = findViewById<TextView>(R.id.tvPostDetailWrite)


        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        val key = intent.getStringExtra("key")

        tvPostDetailContent.text = content.toString()
        tvPostDetailTitle.text = title.toString()

        getImageData(key.toString())



        tvPostDetailModify.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }






    }

    fun getImageData(key : String){
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task->
            if (task.isSuccessful){
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(imgPostDetailPicture) //지역변수

            }
        }


    }


}