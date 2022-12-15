package com.example.planter.Post

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.utils.FBdataBase
import com.example.planter.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class PostFragment : Fragment() {

    //getPostData를 통해 받아온 item(PostVO)를 관리하는 배열 생성
    var PostList = ArrayList<PostVO>()
    lateinit var adapter: PostAdapter
    var keyData = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_post, container, false)

        val rvPostView = view.findViewById<RecyclerView>(R.id.rvPostView)
        val btnPostSend = view.findViewById<ImageView>(R.id.btnPostSend)

        //2. Template결정 : post_list.xml

        //3. Item결정 : PostVO


        //모든 정보 불러옴.
        getPostData()
        //4. Adapter 결정

        adapter = PostAdapter(requireContext(), PostList)

        // 각 게시글 클릭 이벤트 - 게시글 내부로 동
        adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {

                // BoardInsideActivity로 넘어가자

                val intent = Intent(requireActivity(), PostDetailJsActivity::class.java)

                intent.putExtra("title", PostList[position].title)

                intent.putExtra("content", PostList[position].content)
                intent.putExtra("nick", PostList[position].nick)
                intent.putExtra("time", PostList[position].time)
                intent.putExtra("uid", PostList[position].uid)
                
                //PostDetail로 게시글의 키 값 전달
                intent.putExtra("key", keyData[position])
                
                //PostDetail 액티비티 구동
                startActivity(intent)

            }

        })


        //5.Adapter 부착

        rvPostView.adapter = adapter
        rvPostView.layoutManager = LinearLayoutManager(requireContext())


        // 게시글작성 액티비티로 이동

        btnPostSend.setOnClickListener {
            val intent = Intent(requireContext(), PostWriteActivity::class.java)
            startActivity(intent)
        }



        return view

    }//onCreateView 밖


    // post에 있는 데이터 다~ 가져오는 함수
    fun getPostData() {
        
        //데이터베이스에서 컨텐츠 세부정보 검색
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                //        -BoardVO
                
                //저장/삭제마다 데이터 누적으로 인한 게시글 중복 저장 방지
                PostList.clear()

                //snapshot 내부 데이터 모델 형식으로 지정정
               for (model in snapshot.children) {
                    val item = model.getValue(PostVO::class.java)

                    if (item != null) {
                        //게시글 목록에 item(게시글)삽입
                        PostList.add(item)
                    }
                   //게시글 키 목록으로 String변환 넣음
                    keyData.add(model.key.toString())

                }

                // adapter 새로고침 받아오는 속도가 다르니까
                PostList.reverse()
                keyData.reverse()
                adapter.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {
                // 오류가 발생했을 경우 실행되는 함수
            }

        }

        // snapshot으로 board에 있는 모든 ~~ 데이터가 들어간다~~
        FBdataBase.getBoardRef().addValueEventListener(postListener)
    }



}