package com.example.planter.UserAuth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.planter.MyPage.MyPageFragment
import com.example.planter.R
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBdataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class EditActivity : AppCompatActivity() {


    lateinit var email : String
    lateinit var nick : String

    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var imgEditimg: ImageView
    lateinit var imgEditBtn: ImageView
    lateinit var btnEditEdit : Button
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        auth = Firebase.auth

        val user = FBAuth.getUid()

        val tvEditEmail = findViewById<TextView>(R.id.tvEditEmail)
        val etEditPw = findViewById<EditText>(R.id.etEditPw)
        val etEditNick = findViewById<EditText>(R.id.etEditNick)
        val imgEditBtn = findViewById<ImageView>(R.id.imgEditBtn)
        btnEditEdit = findViewById(R.id.btnEditEdit)
        imgEditimg = findViewById(R.id.imgEditimg)
        val img = intent.getStringExtra("key")

        getImageData(img.toString())

        storage = FirebaseStorage.getInstance()

        // ???????????? setValue
        // ???????????? setValue
        // updatepassword




        // ????????? uid ????????? ????????????
        FBdataBase.getJoinRef().child(user).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                email = snapshot.child("email").value as String

                getImageData(user)
                tvEditEmail.text = email

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        // ????????? uid ????????? ????????????
        FBdataBase.getJoinRef().child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nick = snapshot.child("nick").value as String


                etEditNick.setHint(nick)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        // ?????? ????????? ?????? ?????? ??????
        imgEditBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }

        btnEditEdit.setOnClickListener {
            //val uid = FBAuth.getUid()
            var isEdit = true // ?????? ???????????? ??????!
            val pw = etEditPw.text.toString()

            if (pw.isEmpty()) {
                isEdit = false
                Toast.makeText(this, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show()

            }
            if (pw.length < 8 ){
                isEdit = false
                Toast.makeText(this, "??????????????? 8?????? ?????? ??????????????????", Toast.LENGTH_SHORT).show()
            }

            if (isEdit) {

                val intent = Intent(this@EditActivity, MyPageFragment::class.java)
                startActivity(intent)
                finish()
            }
        }





}// oncreate ???

fun imgUpload(key : String) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val mountainsRef = storageRef.child("$key.png")

    imgEditimg.isDrawingCacheEnabled = true
    imgEditimg.buildDrawingCache()
    val bitmap = (imgEditimg.drawable as BitmapDrawable).bitmap
    val baos = ByteArrayOutputStream()
    //quality:?????? ????????? 1~100.
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val data = baos.toByteArray()

    //mountainsRef : ???????????? ?????? ???????????? ?????????.
    var uploadTask = mountainsRef.putBytes(data)
    uploadTask.addOnFailureListener {
        // Handle unsuccessful uploads
    }.addOnSuccessListener { taskSnapshot ->
        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        // ...
    }
}



fun getImageData(key: String) {
    val storageReference = Firebase.storage.reference.child("$key.png")

    storageReference.downloadUrl.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            //Gilde: ?????? ?????? ????????? ???????????? ???????????????
            Glide.with(this)
                .load(task.result)
                .into(imgEditimg) //????????????

        }
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