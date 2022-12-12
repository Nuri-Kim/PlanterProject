package com.example.planter.UserAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.planter.MainActivity
import com.example.planter.R

class LoginActivity : AppCompatActivity() {

    //lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //auth = Firebase.auth

        val etLoginEmail = findViewById<TextView>(R.id.etLoginEmail)
        val etLoginPw = findViewById<TextView>(R.id.etLoginPw)
        val btnLoginLogin = findViewById<Button>(R.id.btnLoginLogin)

        btnLoginLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val pw = etLoginPw.text.toString()

            if(email){

            }

            val intent = Intent(this, MainActivity::class.java)





        }


    }
}