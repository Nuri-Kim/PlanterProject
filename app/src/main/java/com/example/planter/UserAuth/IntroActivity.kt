package com.example.planter.UserAuth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.planter.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnIntroLogin = findViewById<Button>(R.id.btnIntroLogin)
        val btnIntroJoin = findViewById<Button>(R.id.btnIntroJoin)

        btnIntroLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        btnIntroJoin.setOnClickListener{
            val intent = Intent(this,JoinActivity::class.java)
            startActivity(intent)
        }

    }
}