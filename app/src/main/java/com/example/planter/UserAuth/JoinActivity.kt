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
import com.bumptech.glide.Glide
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBAuth.Companion.auth
import com.example.planter.utils.FBdataBase
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var imgJoinUser : ImageView
    lateinit var storage: FirebaseStorage
    lateinit var etJoinPw : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        etJoinPw = findViewById(R.id.etJoinPw)
        val etJoinCk = findViewById<EditText>(R.id.etJoinCk)
        val etJoinNick = findViewById<EditText>(R.id.etJoinNick)
        val btnJoinJoin = findViewById<Button>(R.id.btnJoinJoin)
        imgJoinUser = findViewById(R.id.imgJoinUser)
        val imgJoinEditIcon = findViewById<ImageView>(R.id.imgJoinEditBtn)


        storage = FirebaseStorage.getInstance()

        var key = FBdataBase.getJoinRef().push().key.toString()
        val img = intent.getStringExtra("key")

        getJoinImageData(img.toString())




        // 회원 프로필 사진 변경 버튼
        imgJoinEditIcon.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }








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

                            val uid = FBAuth.getUid()
                            val getEmail = intent.getStringExtra("email").toString()
                            val getNick = intent.getStringExtra("nick").toString()

                            if(getEmail != null && getNick != null){
                                val userList = FBdataBase.getJoinRef()
                                userList.child(uid).setValue(JoinVO(email,nick,uid))
                            }


                            imgUpload(uid)


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
    }// onCreate 밖
    fun imgUpload(key:String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        // Get the data from an ImageView as bytes
        imgJoinUser.isDrawingCacheEnabled = true
        imgJoinUser.buildDrawingCache()
        val bitmap = (imgJoinUser.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
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



    fun getJoinImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    //.into(imgJoinUser) //지역변수

            }
        }
    }
    //이메일 유효성
    fun verifyEmail() {
        auth?.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "유효성 체크 됨", Toast.LENGTH_LONG).show()
                }
            }
    }
    //패스워드 유효성
    fun passwordCk() {
        etJoinPw.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val pwRegex = """^[0-9a-zA-Z!@#$%^+\-=]*$"""
            val pwPattern = Pattern.compile(pwRegex)
            if (source.isNullOrBlank() || pwPattern.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText(this, "입력할 수 없는 문자입니다: $source", Toast.LENGTH_SHORT).show()
            ""
        })
    }

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == RESULT_OK) {
                imgJoinUser.setImageURI(it.data?.data)
            }
        }


    }

