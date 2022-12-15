package com.example.planter.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.planter.MainActivity
import com.example.planter.Post.PostFragment
import com.example.planter.R
import com.example.planter.Social.BookmarkFragment
import com.example.planter.UserAuth.EditActivity
import com.example.planter.utils.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MyPageFragment : Fragment() {

    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = Firebase.auth

        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        val btnMyPageLogOut = view.findViewById<Button>(R.id.btnMyPageLogOut)
        val tvMyPageDel = view.findViewById<TextView>(R.id.tvMyPageDel)
        val btnMyPageEdit = view.findViewById<Button>(R.id.btnMyPageEdit)
        val btnMyBookmark = view.findViewById<Button>(R.id.btnMyBookmark)



        btnMyPageEdit.setOnClickListener {

            val intent = Intent(context,EditActivity::class.java)
            startActivity(intent)
        }



        btnMyPageLogOut.setOnClickListener {
            auth.signOut()
            Toast.makeText(context,"로그아웃 하셨습니다", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        tvMyPageDel.setOnClickListener {

            // AlertDialog 옵션 정보를 담고 builder 생성!
            // AlertDialog.Builder(this).setTitle("asd").setMessage("asdf")
            // 이런 식으로 한 줄에 쓸 수도 있음
            val builder = AlertDialog.Builder(requireContext())


//            builder.setPositiveButton("탈퇴", DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getApplicationContext(), "Yeah!!", Toast.LENGTH_LONG).show();
//                }
            builder.setPositiveButton("탈퇴하기"){dialogInterface, it ->


                auth.currentUser?.delete()
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "탈퇴 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)

            }
            builder.setNegativeButton("취소", null)

            // 주의
            // builder를 통해 옵션을 단 이후
            // 맨 마지막에는 무조건!! show()함수를 달아야 한다

            builder.show()


        }

//        btnMyBookmark.setOnClickListener {
//
////            var uid = FBAuth.getUid()
////            Log.d("나와 북마크용 uid", uid)
//            val intent = Intent(requireContext(), BookmarkFragment::class.java)
////            intent.putExtra("uid", uid.toString())
//            startActivity(intent)
//
//        }


        return view





    }


}
