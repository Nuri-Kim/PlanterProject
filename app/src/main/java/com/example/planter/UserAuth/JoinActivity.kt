package com.example.planter.UserAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.planter.MainActivity
import com.example.planter.R
import org.w3c.dom.Text

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        val etJoinPw = findViewById<EditText>(R.id.etJoinPw)
        val etJoinCk = findViewById<EditText>(R.id.etJoinCk)
        val etJoinNick = findViewById<EditText>(R.id.etJoinNick)
        val btnJoinJoin = findViewById<Button>(R.id.btnJoinJoin)


        btnJoinJoin.setOnClickListener {

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
}