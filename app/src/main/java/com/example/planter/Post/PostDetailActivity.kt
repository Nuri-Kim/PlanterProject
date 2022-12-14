package com.example.planter.Post

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBAuth.Companion.getUid
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostDetailActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var tvPostDetailUserNick: TextView

    lateinit var imgPostDetailPicture: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        auth = Firebase.auth

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this )
        val email = sharedPreferences.getString("loginId", "")
        val id = getUid()

        if (email != null) {
            Log.d("나와", email)
        }

        imgPostDetailPicture = findViewById(R.id.imgPostDetailPicture)

        val imgPostDetailUserPic = findViewById<ImageView>(R.id.imgPostDetailUserPic)
        val tvPostDetailTime = findViewById<TextView>(R.id.tvPostDetailTime)
        val tvPostDetailTitle = findViewById<TextView>(R.id.tvPostDetailTitle)
        tvPostDetailUserNick = findViewById(R.id.tvPostDetailUserNick)
        val tvPostDetailCategory = findViewById<TextView>(R.id.tvPostDetailCategory)
        val tvPostDetailModify = findViewById<TextView>(R.id.tvPostDetailModify)
        val tvPostDetailDelete = findViewById<TextView>(R.id.tvPostDetailDelete)
        val tvPostDetailContent = findViewById<TextView>(R.id.tvPostDetailContent)
        val tvPostDetailCount = findViewById<TextView>(R.id.tvPostDetailCount)
        val tvPostDetailWrite = findViewById<TextView>(R.id.tvPostDetailWrite)





        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val nick = intent.getStringExtra("nick")
        val time = intent.getStringExtra("time")
        var uid = intent.getStringExtra("uid")





        val key = intent.getStringExtra("key")

        tvPostDetailContent.text = content.toString()
        tvPostDetailTitle.text = title.toString()
        tvPostDetailUserNick.text = nick.toString()
        tvPostDetailTime.text = time.toString()



        getImageData(key.toString())
        Log.d("나와id", id)

        if (uid != null) {
            Log.d("나와uid", uid)
        }


        val sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)

        if(uid == id) {
            tvPostDetailModify.visibility = View.INVISIBLE
            tvPostDetailDelete.visibility = View.INVISIBLE

        }

//        if () {
//
//
//
//
//            tvPostDetailModify.setOnClickListener {
//
//                val db = Firebase.database
//
//                val Content = db.getReference("board").child(key.toString())
//                Content.setValue(null)
//
//                val intent = Intent(this, PostWriteActivity::class.java)
//                intent.putExtra("uid", uid)
//                Log.d("나와", uid)
//                startActivity(intent)
//
//            }
//
//            tvPostDetailDelete.setOnClickListener {
//
//            }
//
//            tvPostDetailWrite.setOnClickListener {
//                //
//            }
//
//
//        }
    }

    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(imgPostDetailPicture) //지역변수

            }
        }


    }


}


