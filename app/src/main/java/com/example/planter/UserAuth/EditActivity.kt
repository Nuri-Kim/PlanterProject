package com.example.planter.UserAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        auth = Firebase.auth

        val etEditEmail = findViewById<EditText>(R.id.etEditEmail)
        val etEditPw = findViewById<EditText>(R.id.etEditPw)
        val etEditNick = findViewById<EditText>(R.id.etEditNick)
        val btnEditImg = findViewById<Button>(R.id.btnEditImg)
        val btnEditEdit = findViewById<Button>(R.id.btnEditEdit)



        val email = etEditEmail.text.toString()
        val pw = etEditPw.text.toString()
        val nick = etEditNick.text.toString()



auth.currentUser.toString()

        btnEditImg.setOnClickListener {




        }






        btnEditEdit.setOnClickListener {




        }



    }
}