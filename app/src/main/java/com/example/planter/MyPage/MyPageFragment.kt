package com.example.planter.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.planter.Api.HomeFragment
import com.example.planter.MainActivity
import com.example.planter.R
import com.example.planter.Social.BookmarkActivity
//import com.example.planter.Social.BookmarkActivity
import com.example.planter.UserAuth.EditActivity
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBdataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MyPageFragment : Fragment() {

    lateinit var auth : FirebaseAuth
    lateinit var nick : String
    lateinit var uid : String
    lateinit var imgMypage : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = Firebase.auth
        var uid = FBAuth.getUid()


        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        imgMypage = view.findViewById(R.id.imgMypage)
        val tvMypageNick = view.findViewById<TextView>(R.id.tvMypageNick)
        val btnMyPageLogOut = view.findViewById<Button>(R.id.btnMyPageLogOut)
        val tvMyPageDel = view.findViewById<TextView>(R.id.tvMyPageDel)
        val btnMyPageEdit = view.findViewById<Button>(R.id.btnMyPageEdit)
        val btnMyBookmark = view.findViewById<Button>(R.id.btnMyBookmark)

        val user = FBAuth.getUid()
        // 접속한 uid 닉네임 찾아오기
        FBdataBase.getJoinRef().child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nick = snapshot.child("nick").value as String

                getImageData(user)
                tvMypageNick.text = nick

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })








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
            builder.setTitle("탈퇴")
            builder.setMessage("탈퇴하시겠습니까?")
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



        btnMyBookmark.setOnClickListener {


            Log.d("나와 북마크용 uid", uid)
            val intent = Intent(requireContext(), BookmarkActivity::class.java)
            intent.putExtra("uid", uid.toString())
            startActivity(intent)

//            bookmark로 데이터 보내기

//            requireActivity().supportFragmentManager.beginTransaction().replace(
//                R.id.flMain,
//
//                BookmarkFragment()
//            ).commit()

        }


        return view





    }
    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(requireContext())
                    .load(task.result)
                    .into(imgMypage) //지역변수

            }
        }
    }


}
