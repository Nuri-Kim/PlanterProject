package com.example.planter.Post

import android.content.Context
import android.content.Intent
import android.location.Geocoder
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
import com.example.planter.Map.MapsActivity
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBAuth.Companion.getUid
import com.example.planter.utils.FBdataBase
import com.example.planter.R
import com.example.planter.Social.CommentAdapter
import com.example.planter.Social.CommentVO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostDetailActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var tvPostDetailUserNick: TextView
    lateinit var imgPostDetailPicture: ImageView
    lateinit var adapter: CommentAdapter
    var commentList = ArrayList<CommentVO>()
    var keyData = ArrayList<String>()
    var commentCnt: Int = 0
    lateinit var tvPostDetailCount: TextView
    var bookmarkCk = false
    lateinit var imgPostDetailBookmark: ImageView
    var likeCk = false
    lateinit var imgPostDetailLike: ImageView
    lateinit var tvPostDetailLike: TextView
    lateinit var tvPostDetailLocation: TextView
    lateinit var imgPostDetailLocation: ImageView
    var addr = "위치 정보를 불러올 수 없습니다"
    var currentNick = "닉네임"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        auth = Firebase.auth

        val database = Firebase.database
        val bookMarkRef = database.getReference("BookMarkList")
        val commentRef = database.getReference("CommentList")
        val likeRef = database.getReference("LikeList")

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
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
//        val tvPostDetailCategory = findViewById<TextView>(R.id.tvPostDetailCategory)
        val tvPostDetailModify = findViewById<TextView>(R.id.tvPostDetailModify)
        val tvPostDetailDelete = findViewById<TextView>(R.id.tvPostDetailDelete)
        val tvPostDetailContent = findViewById<TextView>(R.id.tvPostDetailContent)
        tvPostDetailCount = findViewById<TextView>(R.id.tvPostDetailCount)
        val tvPostDetailWrite = findViewById<TextView>(R.id.tvPostDetailCmtAdd)

        imgPostDetailLike = findViewById<ImageView>(R.id.imgPostDetailLike)
        tvPostDetailLike = findViewById<TextView>(R.id.tvPostDetailLike)
        val imgPostDetailComment = findViewById<ImageView>(R.id.imgPostDetailComment)
        imgPostDetailBookmark = findViewById<ImageView>(R.id.imgPostDetailBookmark)
        val tvPostDetailCmtAdd = findViewById<TextView>(R.id.tvPostDetailCmtAdd)
        val etPostDetailComment = findViewById<EditText>(R.id.etPostDetailComment)
        val rvComment = findViewById<RecyclerView>(R.id.rvComment)

        tvPostDetailLocation = findViewById<TextView>(R.id.tvPostDetailLocation)
        imgPostDetailLocation = findViewById<ImageView>(R.id.imgPostDetailLocation)


        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val nick = intent.getStringExtra("nick")
        val time = intent.getStringExtra("time")
        var uid = intent.getStringExtra("uid")

        var longitude = intent.getStringExtra("longitude")
        var latitude = intent.getStringExtra("latitude")

        val prePost = PostVO(title!!, content!!, "일반", uid!!, time!!)
        Log.d("뭐야", prePost.toString())

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

        if (uid != id) {
            tvPostDetailModify.visibility = View.INVISIBLE
            tvPostDetailDelete.visibility = View.INVISIBLE

        } else {
            tvPostDetailModify.visibility = View.VISIBLE
            tvPostDetailDelete.visibility = View.VISIBLE
        }

        if (longitude!!.length == 0 && latitude!!.length == 0) {
            tvPostDetailLocation.visibility = View.INVISIBLE
            imgPostDetailLocation.visibility = View.INVISIBLE
        } else {
            tvPostDetailLocation.visibility = View.VISIBLE
            imgPostDetailLocation.visibility = View.VISIBLE

            val geocoder = Geocoder(this)
            //GRPC 오류? try catch 문으로 오류 대처
            try {
                addr =
                    geocoder.getFromLocation(latitude!!.toDouble(), longitude.toDouble(), 1).first()
                        .getAddressLine(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            tvPostDetailLocation.text = addr

        }

        tvPostDetailLocation.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("lon", longitude.toString())
            intent.putExtra("lat", latitude.toString())
            intent.putExtra("addr", addr)
            startActivity(intent)

        }


        tvPostDetailModify.setOnClickListener {

            val db = Firebase.database

            val Content = db.getReference("board").child(key.toString())
//                Content.setValue(null)

            val intent = Intent(this, PostWriteActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            intent.putExtra("key", key)

            if (uid != null) {
                Log.d("나와보내는uid", uid)
                startActivity(intent)
            }

            finish()

        }

        tvPostDetailDelete.setOnClickListener {
            val db = Firebase.database

            val Content = db.getReference("board").child(key.toString())
            Content.setValue(null)
            finish()
        }

        //댓글 정보, 리스트
        getCommentData(key)
        adapter = CommentAdapter(this, commentList, keyData, key!!)
        rvComment.adapter = adapter
        rvComment.layoutManager = LinearLayoutManager(this)

        //북마크 정보
        getBookmarkData(key)

        //좋아요 정보
        getLikeData(key)
        getLikeCntData(key)

        FBdataBase.getJoinRef().child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentNick = snapshot.child("nick").value as String
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        // 좋아요 이벤트
        imgPostDetailLike.setOnClickListener {

            if (likeCk) {
                imgPostDetailLike.setImageResource(R.drawable.icon_like_white)
                likeRef.child(key!!).child(auth.currentUser!!.uid).removeValue()
            } else {
                imgPostDetailLike.setImageResource(R.drawable.icon_like_color)
                likeRef.child(key!!).child(auth.currentUser!!.uid).setValue("good")

            }

        }

        // 북마크 이벤트
        imgPostDetailBookmark.setOnClickListener {

            if (bookmarkCk) {
                imgPostDetailBookmark.setImageResource(R.drawable.bookmark_empty)

            } else {
                bookMarkRef.child(auth.currentUser!!.uid).child(key!!).removeValue()
                imgPostDetailBookmark.setImageResource(R.drawable.bookmark_green)
                bookMarkRef.child(auth.currentUser!!.uid).child(key!!).setValue("bookmark")
            }

        }

        // 댓글 등록 이벤트
        tvPostDetailCmtAdd.setOnClickListener {

            val comment = etPostDetailComment.text.toString()
            val time = FBAuth.getTime()
            val comKey = FBdataBase.getCommentRef().child(key!!).push().key.toString()
            FBdataBase.getCommentRef().child(key).child(comKey)
                .setValue(CommentVO(currentNick, id, comment, time))

            etPostDetailComment.setText("")

        }
    }

    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")
        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(imgPostDetailPicture) //지역변수

            } else {
                imgPostDetailPicture.visibility = View.GONE
            }
        }

    }


    // 댓글 데이터 받아오기
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

                commentCnt = commentList.size
                tvPostDetailCount.text = commentCnt.toString()

                commentList.reverse()
                keyData.reverse()
                Log.d("댓글", commentCnt.toString())
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // 오류가 발생했을 경우 실행되는 함수
            }

        }

        FBdataBase.getCommentRef().child(key!!).addValueEventListener(commentListener)
    }

    // 좋아요 데이터 받아오기
    fun getLikeData(key: String?) {
        val bookmarkListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var value = snapshot.getValue<String>()

                if(value == null){
                    likeCk = false
                    imgPostDetailLike.setImageResource(R.drawable.icon_like_white)
                }else{
                    likeCk = true
                    imgPostDetailLike.setImageResource(R.drawable.icon_like_color)
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        FBdataBase.getLikeRef().child(key!!).child(getUid()).addValueEventListener(bookmarkListener)

    }

    // 좋아요 개수 데이터
    fun getLikeCntData(key: String?) {
        val likeCntListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var likeCntList = ArrayList<String>()

                for (model in snapshot.children) {
                    val item = model.getValue<String>()

                    if (item != null) {
                        likeCntList.add(item)
                    }

                }

                tvPostDetailLike.text = likeCntList.size.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                // 오류가 발생했을 경우 실행되는 함수
            }

        }

        FBdataBase.getLikeRef().child(key!!).addValueEventListener(likeCntListener)
    }


    // 북마크 데이터 받아오기
    fun getBookmarkData(key: String?) {
        val bookmarkListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var value = snapshot.getValue<String>()

                if(value == null){
                    bookmarkCk = false
                    imgPostDetailBookmark.setImageResource(R.drawable.bookmark_empty)
                }else{
                    bookmarkCk = true
                    imgPostDetailBookmark.setImageResource(R.drawable.bookmark_green)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        FBdataBase.getBookMarkRef().child(getUid()).child(key!!).addValueEventListener(bookmarkListener)

    }

}