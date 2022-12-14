package com.example.planter.UserAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.*
import com.bumptech.glide.Glide
import com.example.fullstackapplication.utils.FBAuth.Companion.auth
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var imgJoinUser : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        val etJoinPw = findViewById<EditText>(R.id.etJoinPw)
        val etJoinCk = findViewById<EditText>(R.id.etJoinCk)
        val etJoinNick = findViewById<EditText>(R.id.etJoinNick)
        val btnJoinJoin = findViewById<Button>(R.id.btnJoinJoin)
        imgJoinUser = findViewById(R.id.imgJoinUser)
        val imgJoinEditIcon = findViewById<ImageView>(R.id.imgJoinEditIcon)

        val userList = FBdataBase.getJoinRef()
        val img = intent.getStringExtra("key")

        getJoinImageData(img.toString())


        etJoinPw.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val pwRegex = """^[0-9a-zA-Z!@#$%^+\-=]*$"""
            val pwPattern = Pattern.compile(pwRegex)
            if (source.isNullOrBlank() || pwPattern.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText(this,"입력할 수 없는 문자입니다: $source",Toast.LENGTH_SHORT).show()
            ""
        })


        btnJoinJoin.setOnClickListener {

            var isJoin = true // 회원가입 조건 확인하는거임!
            val email = etJoinEmail.text.toString()
            val pw = etJoinPw.text.toString()
            val checkPw = etJoinCk.text.toString()
            val nick = etJoinNick.text.toString()




            // 닉네임 따로 보내주는거

            // 아이디 따로 보내주는거

            // 이거를 모아서 Push로 FBdataBase 에 보내주기


            // 알람받기 회원가입 정보 두 개 넣기



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

                            userList.push().setValue(JoinVO(email,nick,true,true))

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
    fun getJoinImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(imgJoinUser) //지역변수

            }
        }
    }
}