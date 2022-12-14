package com.example.planter.Post

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBAuth.Companion.getUid
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.R
import com.example.planter.Social.CommentAdapter
import com.example.planter.Social.CommentVO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostDetailJsActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var tvPostDetailUserNick: TextView

    lateinit var imgPostDetailPicture: ImageView

    //~정선추가
    lateinit var adapter: CommentAdapter
    var commentList = ArrayList<CommentVO>()
    var keyData = ArrayList<String>()
    //~

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail_js)

        auth = Firebase.auth

        //~정선추가
        val database = Firebase.database
        val bookMarkRef = database.getReference("BookMarkList")
        val commentRef = database.getReference("CommentList")
        val likeRef = database.getReference("LikeList")
        //~

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this )
        val email = sharedPreferences.getString("loginId", "")
        val id = getUid()

        if (email != null) {
            Log.d("나와", email)
        }

        imgPostDetailPicture = findViewById(R.id.imgCommentPicture)

        val imgPostDetailUserPic = findViewById<ImageView>(R.id.imgPostDetailUserPic)
        val tvPostDetailTime = findViewById<TextView>(R.id.tvPostDetailTime)
        val tvPostDetailTitle = findViewById<TextView>(R.id.tvPostDetailTitle)
        tvPostDetailUserNick = findViewById(R.id.tvPostDetailUserNick)
        val tvPostDetailCategory = findViewById<TextView>(R.id.tvPostDetailCategory)
        val tvPostDetailModify = findViewById<TextView>(R.id.tvPostDetailModify)
        val tvPostDetailDelete = findViewById<TextView>(R.id.tvPostDetailDelete)
        val tvPostDetailContent = findViewById<TextView>(R.id.tvPostDetailContent)
        val tvPostDetailCount = findViewById<TextView>(R.id.tvPostDetailCount)
//        val tvPostDetailWrite = findViewById<TextView>(R.id.tvPostDetailWrite)

        // ~정선 추가
        val imgPostDetailLike = findViewById<ImageView>(R.id.imageView4)
        val imgPostDetailComment = findViewById<ImageView>(R.id.imgPostDetailComment)
        val imgPostDetailBookmark = findViewById<ImageView>(R.id.imgPostDetailBookmark)
        val tvPostDetailCmtAdd = findViewById<TextView>(R.id.tvPostDetailCmtAdd)
        val etPostDetailComment = findViewById<EditText>(R.id.etPostDetailComment)
        val rvComment = findViewById<RecyclerView>(R.id.rvComment)


//        if(bookmarkList.contains(keyData[position])){
//            imgPostDetailLike.setImageResource(R.drawable.icon_like_color)
//        }else{
//            imgPostDetailLike.setImageResource(R.drawable.icon_like_white)
//        }

        // ~



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


        // ~정선 추가 - 소셜 기능
        val cmtData = ArrayList<CommentVO>()
        getCommentData(key)
        adapter = CommentAdapter(this, commentList)
        rvComment.adapter = adapter
        rvComment.layoutManager = LinearLayoutManager(this)

        // ~


       imgPostDetailLike.setOnClickListener{

           imgPostDetailLike.setImageResource(R.drawable.icon_like_color)

           likeRef.child(key!!).child("count").setValue("good")
//           likeRef.child(key!!).child("count").setValue("good")

           likeRef.child(key!!).child(auth.currentUser!!.uid).setValue("good")

        }

        imgPostDetailBookmark.setOnClickListener {

            imgPostDetailBookmark.setImageResource(R.drawable.bookmark_green)
            bookMarkRef.child(auth.currentUser!!.uid).child(key!!).setValue("bookmark")

        }

        tvPostDetailCmtAdd.setOnClickListener {

            val comment = etPostDetailComment.text.toString()
            val time = FBAuth.getTime()
            val comKey = FBdataBase.getCommentRef().child(key!!).push().key.toString()
            FBdataBase.getCommentRef().child(key).child(comKey)
                .setValue(CommentVO("닉네임", id,comment,time))
//            FBdataBase.getCommentRef().child(key)
//                .setValue(CommentVO("닉네임", id,comment,time))

            etPostDetailComment.setText("")

        }


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


    // 정선 추가
    // post에 있는 데이터 다~ 가져오는 함수
    fun getCommentData(key: String?) {
        val commentListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                //        -BoardVO
                commentList.clear()
                for (model in snapshot.children) {
                    val item = model.getValue(CommentVO::class.java)

                    if (item != null) {
                        commentList.add(item)
                    }
                    keyData.add(model.key.toString())

                }

                // adapter 새로고침 받아오는 속도가 다르니까
                commentList.reverse()
                keyData.reverse()
                adapter.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {
                // 오류가 발생했을 경우 실행되는 함수
            }

        }

        FBdataBase.getCommentRef().child(key!!).addValueEventListener(commentListener)
    }



}


