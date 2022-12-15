package com.example.planter.UserAuth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.planter.R
import com.example.planter.utils.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var etJoinEmail : EditText
    lateinit var etJoinPw : EditText
    lateinit var imgJoinUser : ImageView
    lateinit var imgJoinEditBtn : ImageView
    lateinit var storage: FirebaseStorage



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        etJoinPw = findViewById<EditText>(R.id.etJoinPw)
        val etJoinCk = findViewById<EditText>(R.id.etJoinCk)
        val etJoinNick = findViewById<EditText>(R.id.etJoinNick)
        val btnJoinJoin = findViewById<Button>(R.id.btnJoinJoin)
        val uid = FBAuth.getUid()
        imgJoinUser = findViewById(R.id.imgJoinUser)
        imgJoinEditBtn = findViewById<ImageView>(R.id.imgJoinEditBtn)

        storage= FirebaseStorage.getInstance()


        // 사진 클릭 버튼
        imgJoinEditBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }




        // 비밀번호 유효성 검사
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


                            imgUpload(uid)

                            Toast.makeText(this, "플랜터에 온 걸 환영합니다", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@JoinActivity,LoginActivity::class.java)
                            intent.putExtra("email",email)
                            intent.putExtra("nick",nick)
                            startActivity(intent)
                            finish()



                        } else {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == RESULT_OK) {
            imgJoinUser.setImageURI(it.data?.data)
        }
    }

    fun imgUpload(key:String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        // Get the data from an ImageView as bytes
        imgJoinUser.isDrawingCacheEnabled = true
        imgJoinUser.buildDrawingCache()
        val bitmap = (imgJoinUser .drawable as BitmapDrawable).bitmap
        val baos = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }



}