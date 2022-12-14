package com.example.planter.MyPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.fullstackapplication.utils.FBAuth.Companion.auth
import com.example.planter.MainActivity
import com.example.planter.R
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
        val btnMyPageDel = view.findViewById<Button>(R.id.btnMyPageDel)



        btnMyPageLogOut.setOnClickListener {
            auth.signOut()
            Toast.makeText(this,"로그아웃 하셨습니다", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnMyPageDel.setOnClickListener {


        }


        return view





    }


}
