package com.example.planter.Post

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.fullstackapplication.utils.FBAuth
import com.example.fullstackapplication.utils.FBAuth.Companion.getUid
import com.example.fullstackapplication.utils.FBdataBase
import com.example.planter.Map.MapsActivity
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
        val imgPostWriteUserNick = findViewById<TextView>(R.id.tvPostWriteUserNick)
        val etPostWriteTitle = findViewById<EditText>(R.id.etPostWriteTitle)
        val etPostWriteContent = findViewById<EditText>(R.id.etPostWriteContent)
        val btnPostWriteSend = findViewById<Button>(R.id.btnPostWriteSend)
        val imgPostWriteLocation = findViewById<ImageView>(R.id.imgPostWriteLocation)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this )
        val email = sharedPreferences.getString("loginId", "")
        val id = getUid()
        val uid = FBAuth.getUid()



        if (email != null) {
            Log.d("나와", email)
        }
        Log.d("나와id", id)

        if (uid != null) {
            Log.d("나와uid", uid)
        }




        imgPostWriteUserNick.setText(email)



        imgPostWritePicture.setOnClickListener {
            //사진 데이터 준비
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            launcher.launch(intent)
        }


        imgPostWriteLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 승인이 안되어있는 상태라면 알림창을 띄워서 승인할 수 있도록
                // ActivityCompat은 확인하는 기능, 요청하는 기능이 둘 다 들어가 있음
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
                )
                // requestCode: 내가 뭘 요청한 건지 구분하기 위한 숫자

                return@setOnClickListener
                // label을 사용해 다시 setOnClickListener 돌아가 생명주기가 돌아가게끔
            } else{

                val manager = getSystemService(LOCATION_SERVICE) as LocationManager
                val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let{
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val accuracy = location.accuracy
                    Log.d("gps 받아오기","{$latitude}, {$longitude}")

                }

                val locationListener : LocationListener = object : LocationListener{
                    override fun onLocationChanged(location: Location) {
                        Log.d("gps 받아오기2","${location?.latitude},${location?.longitude}")
                    }

                    override fun onProviderEnabled(provider: String) {
                        super.onProviderEnabled(provider)
                    }

                    override fun onProviderDisabled(provider: String) {
                        super.onProviderDisabled(provider)
                    }
                }

                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10_000L,10f,locationListener)
//                manager.removeUpdates(locationListener)

                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()


            }




        }


            btnPostWriteSend.setOnClickListener {

                //Firebase에 업로드하기

                val title = etPostWriteTitle.text.toString()
                val content = etPostWriteContent.text.toString()
                val time = FBAuth.getTime()
                val uid = FBAuth.getUid()


                var key =  FBdataBase.getBoardRef().push().key.toString()
                FBdataBase.getBoardRef().child(key).setValue(PostVO(title, content,"일반", uid, time ))
                imgUpload(key)
                finish()

            }


////        uid = intent.getStringExtra("uid")
//        if(uid != null){
//            val uid = intent.hasExtra("uid").toString()
//            etPostWriteTitle.setText(title.toString())
//
//            Log.d("뭔데",uid )
//
//        }


    } // onCreate 밖


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