package com.example.planter.Social

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.Post.PostDetailActivity
import com.example.planter.Post.PostVO
import com.example.planter.R
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBAuth.Companion.getUid
import com.example.planter.utils.FBdataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BookmarkActivity : AppCompatActivity() {


    var BookmarkList = ArrayList<String>()
    var PostList = ArrayList<PostVO>()
    lateinit var adapter: BookmarkAdapter
    var keyData = ArrayList<String>()
    val database = Firebase.database
    val bookmarkRef = database.getReference("BookMarkList")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark2)


        val rvBookmark = findViewById<RecyclerView>(R.id.rvBookmark)
        val key = intent.getStringExtra("key")

        //2. template 결정 : bookmark_list

        //3.Item 결정 : BookmarkVO

        Log.d("dd",getUid().toString())
//        getBookmarkListData(getUid())

        val bookmarkListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                //        -BoardVO
                //저장/삭제마다 데이터 누적으로 인한 게시글 중복 저장 방지
                BookmarkList.clear()
                //snapshot 내부 데이터 모델 형식으로 지정정
                for (model in snapshot.children) {
                    Log.d("갖고 snapshot", snapshot.toString())
                    Log.d("갖고 model", model.toString())
                    val item = model.key
                    Log.d("갖고 item", item.toString())

                    if (item != null) {
                        //게시글 목록에 item(게시글)삽입
                        BookmarkList.add(item)
                        Log.d("갖고 in", BookmarkList.toString() )
                    }
                    //게시글 키 목록으로 String변환 넣음
                    keyData.add(model.key.toString())




                }
                // adapter 새로고침 받아오는 속도가 다르니까
                BookmarkList.reverse()
                keyData.reverse()

                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // 오류가 발생했을 경우 실행되는 함수
            }
        }

        FBdataBase.getBookMarkRef().child(FBAuth.getUid()).addValueEventListener(bookmarkListener)





        Log.d("갖고 오냐고", BookmarkList.toString())

        for( i in keyData) {
            FBdataBase.getBoardRef().child(i).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (model in snapshot.children) {
                        val item = model.getValue(PostVO::class.java)
                        if (item != null) {
                            //게시글 목록에 item(게시글)삽입
                            PostList.add(item)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        Log.d("갖고 오냐고", PostList.toString())

        Log.d("갖고 오냐고", keyData.toString())


        //4.어댑터 결정
        adapter = BookmarkAdapter(this, PostList)

        adapter.setOnItemClickListener(object : BookmarkAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {

                var uid = FBAuth.getUid()

                // BoardInsideActivity로 넘어가자
                val intent = Intent(this@BookmarkActivity, PostDetailActivity::class.java)

                intent.putExtra("uid", PostList[position].uid)
                intent.putExtra("nick", PostList[position].nick)
                intent.putExtra("title", PostList[position].title)
                intent.putExtra("content",PostList[position].content)


                //PostDetail로 게시글의 키 값 전달
                intent.putExtra("key", keyData[position])

                //PostDetail 액티비티 구동
                startActivity(intent)

            }
        })


        //5.adapter부착
        rvBookmark.adapter = adapter
        rvBookmark.layoutManager = LinearLayoutManager(this)


    }


    fun getBookmarkListData(key: String?) {
        //데이터베이스에서 컨텐츠 세부정보 검색
        val bookmarkListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                //        -BoardVO
                //저장/삭제마다 데이터 누적으로 인한 게시글 중복 저장 방지
                BookmarkList.clear()
                //snapshot 내부 데이터 모델 형식으로 지정정
                for (model in snapshot.children) {
                    Log.d("북마크내부", snapshot.toString())
                    Log.d("북마크내부", model.toString())
                    val item = model.key
                    Log.d("item", item.toString())

                    if (item != null) {
                        //게시글 목록에 item(게시글)삽입
                        BookmarkList.add(item)
                        Log.d("in", BookmarkList.toString() )
                    }
                    //게시글 키 목록으로 String변환 넣음
                    keyData.add(model.key.toString())
                }
                // adapter 새로고침 받아오는 속도가 다르니까
                BookmarkList.reverse()
                keyData.reverse()

                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // 오류가 발생했을 경우 실행되는 함수
            }
        }

        FBdataBase.getBookMarkRef().child(key!!).addValueEventListener(bookmarkListener)

    }

//    // post에 있는 데이터 다~ 가져오는 함수
//    fun getPostData(keyData:ArrayList<String>) {
//        //데이터베이스에서 컨텐츠 세부정보 검색
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // Firebase에서 snapshot으로 데이터를 받아온 경우
//                // 게시물의 uid
//                //        -BoardVO
//                //저장/삭제마다 데이터 누적으로 인한 게시글 중복 저장 방지
//                PostList.clear()
//
//                //snapshot 내부 데이터 모델 형식으로 지정정
//                for (model in snapshot.children) {
//                    val item = model.getValue(PostVO::class.java)
//
//                    if (item != null && model.key == )  {
//                        //게시글 목록에 item(게시글)삽입
//                        PostList.add(item)
//                    }
//
//                }
//
//                // adapter 새로고침 받아오는 속도가 다르니까
//                PostList.reverse()
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
//        // snapshot으로 board에 있는 모든 ~~ 데이터가 들어간다~~
//        FBdataBase.getBoardRef().child(keyData).addValueEventListener(postListener)
//
//
//    }



}





        // 북마크 데이터 받아오기
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







