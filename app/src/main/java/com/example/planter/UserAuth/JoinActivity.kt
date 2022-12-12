package com.example.planter.UserAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
            val checkPw = etJoinCk.text.toString()
            val nick = etJoinNick.text.toString()


            if(email.isEmpty()){
                isJoin = false
                Toast.makeText(this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(pw.isEmpty()){
                isJoin = false
                Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(checkPw.isEmpty()){
                isJoin = false
                Toast.makeText(this,"비밀번호 확인을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(nick.isEmpty()){
                isJoin = false
                Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(nick.isEmpty()){
                isJoin = false
                Toast.makeText(this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(pw != checkPw){
                isJoin = false
                Toast.makeText(this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
            }
            if(pw.length < 8){
                isJoin = false
                Toast.makeText(this,"비밀번호는 8자리 이상 입력해주세요",Toast.LENGTH_SHORT).show()
            }

            if(isJoin){
                auth.createUserWithEmailAndPassword(email,pw)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "플랜터에 온 걸 환영합니다", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@JoinActivity,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}