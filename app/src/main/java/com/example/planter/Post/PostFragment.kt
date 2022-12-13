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
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.R



class PostFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_post, container, false)



        val rvPostView = view.findViewById<RecyclerView>(R.id.rvPostView)
        val btnPostSend = view.findViewById<Button>(R.id.btnPostSend)

        //2. Template결정 : post_list.xml

        //3. Item결정 : PostVO

        val PostList = ArrayList<PostVO>()
        PostList.add(PostVO("지연지연","a","a","a"))
        PostList.add(PostVO("정선정선","a","a","a"))

        //4. Adapter 결정

        val adapter = PostAdapter(requireContext(), PostList)

        //5.Adapter 부착

        rvPostView.adapter = adapter
        rvPostView.layoutManager = GridLayoutManager(requireContext(), 2)


        btnPostSend.setOnClickListener {
            val intent = Intent(requireContext(), PostWriteActivity::class.java)
            startActivity(intent)
        }



        return view



    }
}