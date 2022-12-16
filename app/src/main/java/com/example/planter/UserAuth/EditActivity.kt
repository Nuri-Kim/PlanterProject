package com.example.planter.UserAuth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class EditActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var imgEditimg: ImageView
    lateinit var imgEditBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        auth = Firebase.auth

        val tvEditEmail = findViewById<TextView>(R.id.tvEditEmail)
        val etEditPw = findViewById<EditText>(R.id.etEditPw)
        val etEditNick = findViewById<EditText>(R.id.etEditNick)
        val imgEditBtn = findViewById<ImageView>(R.id.imgEditBtn)
        val btnEditEdit = findViewById<Button>(R.id.btnEditEdit)
        imgEditimg = findViewById(R.id.imgEditimg)


        storage = FirebaseStorage.getInstance()


        val email = tvEditEmail.text.toString()
        val pw = etEditPw.text.toString()
        val nick = etEditNick.text.toString()


        // 회원 프로필 사진 변경 버튼
        imgEditBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }


        // 회원정보수정 완료 버튼
        btnEditEdit.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {
                displayName = "Jane Q. User"
                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
            }

            finish()
        }

    }

    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == RESULT_OK) {
            imgEditimg.setImageURI(it.data?.data)
        }
    }
}