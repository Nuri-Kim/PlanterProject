package com.example.planter.UserAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class JoinActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        val etJoinPw = findViewById<EditText>(R.id.etJoinPw)
        val etJoinCk = findViewById<EditText>(R.id.etJoinCk)
        val etJoinNick = findViewById<EditText>(R.id.etJoinNick)
        val btnJoinJoin = findViewById<Button>(R.id.btnJoinJoin)


        btnJoinJoin.setOnClickListener {

            var isJoin = true // 회원가입 조건 확인하는거임!
            val email = etJoinEmail.text.toString()
            val pw = etJoinPw.text.toString()
            val ckPw = etJoinCk.text.toString()
            val nick = etJoinNick.text.toString()

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
}