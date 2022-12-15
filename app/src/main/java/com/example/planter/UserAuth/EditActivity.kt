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
import com.bumptech.glide.Glide
import com.example.planter.R
import com.example.planter.utils.FBAuth.Companion.getUid
import com.example.planter.utils.FBdataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class EditActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var imgEditimg: ImageView
    lateinit var imgEditBtn: ImageView

    lateinit var tvEditEmail : TextView
    lateinit var etEditPw : EditText
    lateinit var etEditNick : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        auth = Firebase.auth

        tvEditEmail = findViewById<TextView>(R.id.tvEditEmail)
        etEditPw = findViewById<EditText>(R.id.etEditPw)
        etEditNick = findViewById<EditText>(R.id.etEditNick)
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



        }

    }
    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into() //지역변수

            }
        }


    }

    fun getUserData(key: String?) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var value = snapshot.getValue<JoinVO>()

                value.email
                value.nick

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        FBdataBase.getJoinRef().child(getUid()).addValueEventListener(userListener)

    }

    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == RESULT_OK) {
            imgEditimg.setImageURI(it.data?.data)
        }
    }



}