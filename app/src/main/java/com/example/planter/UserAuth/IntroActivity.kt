package com.example.planter.UserAuth


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnIntroLogin = findViewById<Button>(R.id.btnIntroLogin)
        val btnIntroJoin = findViewById<Button>(R.id.btnIntroJoin)




        auth = Firebase.auth


        // 로그인 버튼
        btnIntroLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }


        // 회원가입 버튼
        btnIntroJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }


    }
}

//private fun AlertDialog.Builder.setPositiveButton(s: String, onClickListener: DialogInterface.OnClickListener, function: () -> Unit) {

