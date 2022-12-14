package com.example.planter.UserAuth

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
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


        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }



        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }






        // 회원 프로필 사진 변경 버튼
        btnEditImg.setOnClickListener {




        }





        // 회원정보수정 완료 버튼
        btnEditEdit.setOnClickListener {

            finish()
        }
    }
}