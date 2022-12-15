package com.example.planter.UserAuth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.planter.R
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBAuth.Companion.auth
import com.example.planter.utils.FBAuth.Companion.getUid
import com.example.planter.utils.FBdataBase
import com.google.android.gms.common.config.GservicesValue.value
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class EditActivity : AppCompatActivity() {
    //전역 변수
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var imgEditimg: ImageView
    lateinit var imgEditBtn: ImageView

    lateinit var tvEditEmail : TextView
    lateinit var etEditPw : EditText
    lateinit var etEditNick : EditText
    ////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


        // 초기화
        auth = Firebase.auth
        tvEditEmail = findViewById<TextView>(R.id.tvEditEmail)
        etEditPw = findViewById<EditText>(R.id.etEditPw)
        etEditNick = findViewById<EditText>(R.id.etEditNick)
        val imgEditBtn = findViewById<ImageView>(R.id.imgEditBtn)
        val btnEditEdit = findViewById<Button>(R.id.btnEditEdit)
        imgEditimg = findViewById(R.id.imgEditimg)
        var key = FBdataBase.getJoinRef().push().key.toString()
        val img = intent.getStringExtra("key")
        storage = FirebaseStorage.getInstance()
        //////////



        getImageData(img.toString())



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

            var isJoin = true
            val email = tvEditEmail.text.toString()
            val pw = etEditPw.text.toString()
            val nick = etEditNick.text.toString()
            val uid = FBAuth.getUid()

            var key = FBdataBase.getJoinRef().push().key.toString()
            FBdataBase.getJoinRef().child(key).setValue(uid)

            // 버튼 조건 줌
            if(pw.isEmpty()){
                isJoin = false
                Toast.makeText(this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(pw.length < 8){
                isJoin = false
                Toast.makeText(this,"비밀번호는 8자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            if(nick.isEmpty()){
                isJoin = false
                Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()
            }

            if(isJoin) {
                auth.currentUser?.updatePassword(etEditPw.toString())
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "정보 수정 완료", Toast.LENGTH_SHORT).show()

                            val uid = FBAuth.getUid()
                            val getPw = intent.getStringExtra("email").toString()
                            val getNick = intent.getStringExtra("nick").toString()

                            if(getPw != null && getNick != null){
                                val userList = FBdataBase.getJoinRef()
                                userList.child(uid).setValue(JoinVO(email,nick,uid))
                            }
                            updateData(etEditPw.toString())
                            imgUpload(uid)

                            val intent = Intent(this@EditActivity,LoginActivity::class.java)
                            intent.putExtra("email",email)
                            intent.putExtra("nick",nick)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "정보를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                        }

            }
        }
    }

} // oncreate 밖

fun getImageData(key: String) {
    val storageReference = Firebase.storage.reference.child("$key.png")

    storageReference.downloadUrl.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            //Gilde: 웹에 있는 이미지 적용하는 라이브러리
            Glide.with(this)
                .load(task.result)
                .into(imgEditimg) //지역변수

        }
    }
}

fun updateData(newPassword: String) {
    auth?.currentUser?.updatePassword(newPassword)

        ?.addOnCompleteListener { task ->
            if (task.isSuccessful)
                Toast.makeText(this, "패스워드 변경 성공", Toast.LENGTH_LONG).show()
        }
}



fun imgUpload(key : String) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val mountainsRef = storageRef.child("$key.png")

    imgEditimg.isDrawingCacheEnabled = true
    imgEditimg.buildDrawingCache()
    val bitmap = (imgEditimg.drawable as BitmapDrawable).bitmap
    val baos = ByteArrayOutputStream()
    //quality:압축 퀄리티 1~100.
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val data = baos.toByteArray()

    //mountainsRef : 스토리지 경로 지정하는 키워드.
    var uploadTask = mountainsRef.putBytes(data)
    uploadTask.addOnFailureListener {
        // Handle unsuccessful uploads
    }.addOnSuccessListener { taskSnapshot ->
        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        // ...
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