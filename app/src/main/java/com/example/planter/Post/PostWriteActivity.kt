package com.example.planter.Post

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.R
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostWriteActivity : AppCompatActivity() {

    lateinit var imgPostWritePicture : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)

        imgPostWritePicture = findViewById(R.id.imgPostWritePicture)
        val imgPostWriteUserNick = findViewById<TextView>(R.id.imgPostWriteUserNick)
        val etPostWriteTitle = findViewById<EditText>(R.id.etPostWriteTitle)
        val etPostWriteContent = findViewById<EditText>(R.id.etPostWriteContent)
        val btnPostWriteSend = findViewById<Button>(R.id.btnPostWriteSend)


        imgPostWritePicture.setOnClickListener {
            //사진 데이터 준비
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }



        btnPostWriteSend.setOnClickListener {

            //Firebase에 업로드하기
            val title = etPostWriteTitle.text.toString()
            val content = etPostWriteContent.text.toString()

            var key =  FBdataBase.getBoardRef().push().key.toString()
            FBdataBase.getBoardRef().child(key).setValue(PostVO("지연",title, content,"일반"))
            imgUpload(key)
            finish()

        }




    }


    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //받아올 결과값이 맞는지 확인 과정
        if (it.resultCode == RESULT_OK) imgPostWritePicture.setImageURI(it.data?.data)

    }

    fun imgUpload(key : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        imgPostWritePicture.isDrawingCacheEnabled = true
        imgPostWritePicture.buildDrawingCache()
        val bitmap = (imgPostWritePicture.drawable as BitmapDrawable).bitmap
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
}