package com.example.planter.UserAuth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBdataBase
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
        val loginId = sharedPreferences.getString("loginId","")
        val loginPw = sharedPreferences.getString("loginPw","")

        val etLoginEmail = findViewById<TextView>(R.id.etLoginEmail)
        val etLoginPw = findViewById<TextView>(R.id.etLoginPw)
        val btnLoginLogin = findViewById<Button>(R.id.btnLoginLogin)
        val tvLoginJoin = findViewById<TextView>(R.id.tvLoginJoin)
        etLoginEmail.text = loginId
        etLoginPw.text = loginPw

        val sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)




        btnLoginLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val pw = etLoginPw.text.toString()


            auth.signInWithEmailAndPassword(email,pw)
                .addOnCompleteListener(this){task ->

                    if(task.isSuccessful){
                        Toast.makeText(this," ${email}님 반가워요",Toast.LENGTH_SHORT).show()

                        val editor = sharedPreferences.edit()
                        editor.putString("loginId",email)
                        editor.putString("loginPw",pw)
                        editor.commit()

                        val editorSp = sp.edit()
                        editorSp.putString("loginId",email)
                        editorSp.commit()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this,"아이디 또는 비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show()

                    }
                }

        }

        tvLoginJoin.setOnClickListener{
            val intent = Intent(this@LoginActivity,JoinActivity::class.java)
            startActivity(intent)
        }

    }
}