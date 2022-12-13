package com.example.planter.UserAuth


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.fullstackapplication.utils.FBAuth
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnIntroLogin = findViewById<Button>(R.id.btnIntroLogin)

        val btnIntroJoin = findViewById<Button>(R.id.btnIntroJoin)

        val btnIntroLogOut = findViewById<Button>(R.id.btnIntroLogOut)

        val btnIntroDel = findViewById<Button>(R.id.btnIntroDel)

        val btnIntroEdit = findViewById<Button>(R.id.btnIntroEdit)

        auth = Firebase.auth





        // 로그인 버튼
        btnIntroLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }





        // 회원가입 버튼
        btnIntroJoin.setOnClickListener{
            val intent = Intent(this,JoinActivity::class.java)
            startActivity(intent)
        }





        // 로그아웃 버튼
        btnIntroLogOut.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"로그아웃하셨습니다",Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }





        // 회원탈퇴 버튼
        btnIntroDel.setOnClickListener {

            auth.currentUser?.delete()
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"탈퇴 성공",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }





        // 회원정보 수정
        btnIntroEdit.setOnClickListener {






        }
    }
}