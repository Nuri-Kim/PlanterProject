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
import com.example.fullstackapplication.utils.FBdataBase
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
        val btnPostSend = view.findViewById<Button>(R.id.btnPostSend)

        //2. Template결정 : post_list.xml

        //3. Item결정 : PostVO

//        PostList.add(PostVO("지연지연","a","a","a"))
//        PostList.add(PostVO("정선정선","a","a","a"))


        getPostData()
        //4. Adapter 결정

        adapter = PostAdapter(requireContext(), PostList)

        // 각 게시글 클릭 이벤트 - 게시글 내부로 이동
        adapter.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {

                // BoardInsideActivity로 넘어가자

                val intent = Intent(requireActivity(), PostDetailActivity::class.java)

                intent.putExtra("title", PostList[position].title)

                intent.putExtra("content", PostList[position].content)
                intent.putExtra("category", PostList[position].category)
                intent.putExtra("time", PostList[position].time)
                intent.putExtra("uid", PostList[position].uid)

                intent.putExtra("key", keyData[position])
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
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase에서 snapshot으로 데이터를 받아온 경우
                // 게시물의 uid
                //        -BoardVO
                PostList.clear()
                for (model in snapshot.children) {
                    val item = model.getValue(PostVO::class.java)

                    if (item != null) {
                        PostList.add(item)
                    }
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