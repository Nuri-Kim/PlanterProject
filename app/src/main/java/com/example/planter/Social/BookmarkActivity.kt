//package com.example.planter.Social
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.planter.Post.PostDetailActivity
//import com.example.planter.Post.PostVO
//import com.example.planter.R
//import com.example.planter.utils.FBAuth
//import com.example.planter.utils.FBdataBase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//
//class BookmarkActivity : AppCompatActivity() {
//
//
//    var BookmarkList = ArrayList<PostVO>()
//    lateinit var adapter: BookmarkAdapter
//    var keyData = ArrayList<String>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bookmark2)
//
//        val rvBookmark = findViewById<RecyclerView>(R.id.rvBookmark)
//        val key = intent.getStringExtra("key")
//
//        //2. template 결정 : bookmark_list
//
//        //3.Item 결정 : BookmarkVO
//
//        getBookmarkData()
//
//        //4.어댑터 결정
//        adapter = BookmarkAdapter(this, BookmarkList)
//
//        adapter.setOnItemClickListener(object : BookmarkAdapter.OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {
//
//                var uid = FBAuth.getUid()
//
//                // BoardInsideActivity로 넘어가자
//                val intent = Intent(this@BookmarkActivity, PostDetailActivity::class.java)
//
//                intent.putExtra("uid", BookmarkList[position].uid)
//                intent.putExtra("nick", BookmarkList[position].nick)
//                intent.putExtra("title", BookmarkList[position].title)
//                intent.putExtra("content", BookmarkList[position].content)
//
//
//                //PostDetail로 게시글의 키 값 전달
//                intent.putExtra("key", keyData[position])
//
//                //PostDetail 액티비티 구동
//                startActivity(intent)
//
//            }
//        })
//
//
//        //5.adapter부착
//        rvBookmark.adapter = adapter
//        rvBookmark.layoutManager = LinearLayoutManager(this)
//
//
//    }
//
//    fun getBookmarkData() {
//
//    }
////    getBookmarkData(key)
//
//    fun getPostData() {
//
//        //데이터베이스에서 컨텐츠 세부정보 검색
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // Firebase에서 snapshot으로 데이터를 받아온 경우
//                // 게시물의 uid
//                //        -BoardVO
//
//                //저장/삭제마다 데이터 누적으로 인한 게시글 중복 저장 방지
//                BookmarkList.clear()
//
//                //snapshot 내부 데이터 모델 형식으로 지정정
//                for (model in snapshot.children) {
//                    val item = model.getValue(PostVO::class.java)
//
//                    if (item != null) {
//                        //게시글 목록에 item(게시글)삽입
//                        BookmarkList.add(item)
//                    }
//                    //게시글 키 목록으로 String변환 넣음
//                    keyData.add(model.key.toString())
//
//                }
//
//                // adapter 새로고침 받아오는 속도가 다르니까
//                BookmarkList.reverse()
//                keyData.reverse()
//                adapter.notifyDataSetChanged()
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // 오류가 발생했을 경우 실행되는 함수
//            }
//
//        }
//        imgPostDetailBookmark.setOnClickListener {
//
//            if (bookmarkCk) {
//                imgPostDetailBookmark.setImageResource(R.drawable.bookmark_empty)
//                bookmarkRef.child(auth.currentUser!!.uid).child(key!!).removeValue()
//            } else {
//                imgPostDetailBookmark.setImageResource(R.drawable.bookmark_green)
//                bookmarkRef.child(auth.currentUser!!.uid).child(key!!).setValue("bookmark")
//            }
//
//
//        }
//
//        // 북마크 데이터 받아오기
//        fun getBookmarkData(key: String?) {
//            val bookmarkListener = object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var value = snapshot.getValue<String>()
//
//                    if (value == null) {
//                        bookmarkCk = false
//                        imgPostDetailBookmark.setImageResource(R.drawable.bookmark_empty)
//                    } else {
//                        bookmarkCk = true
//                        imgPostDetailBookmark.setImageResource(R.drawable.bookmark_green)
//
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            }
//            // snapshot으로 board에 있는 모든 ~~ 데이터가 들어간다~~
//            FBdataBase.getBookMarkRef().addValueEventListener(postListener)
//        }
//
//
//    }
//
//}
//
//
