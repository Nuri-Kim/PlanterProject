package com.example.planter.Post

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.planter.Map.MapsActivity
import com.example.planter.R
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBAuth.Companion.getUid
import com.example.planter.utils.FBdataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class PostWriteActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var imgPostWritePicture: ImageView
    lateinit var etPostWriteTitle: EditText
    lateinit var etPostWriteContent: EditText
    lateinit var tvPostWriteUserNick: TextView
    lateinit var nick: String
    var imgUpload = false
    lateinit var tvPostWriteLocation : TextView
    var longitude = 0.0
    var latitude = 0.0
    var addr = "위치 정보를 불러올 수 없습니다"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)
        auth = Firebase.auth
        val geocoder = Geocoder(this)

        //user?.email.toString()

        imgPostWritePicture = findViewById(R.id.imgPostWritePicture)
        etPostWriteTitle = findViewById<EditText>(R.id.etPostWriteTitle)
        etPostWriteContent = findViewById<EditText>(R.id.etPostWriteContent)
        tvPostWriteLocation = findViewById<TextView>(R.id.tvPostWriteLocation)

        val imgPostWriteUserNick = findViewById<TextView>(R.id.tvPostWriteUserNick)
        val btnPostWriteSend = findViewById<Button>(R.id.btnPostWriteSend)
        val imgPostWriteLocation = findViewById<ImageView>(R.id.imgPostWriteLocation)

        val key = intent.getStringExtra("key")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val email = sharedPreferences.getString("loginId", "")
        val id = getUid()
        val uid = FBAuth.getUid()
        val uidsended = intent.getStringExtra(uid)

        FBdataBase.getJoinRef().child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nick = snapshot.child("nick").value as String

                //getImageData(id)
                imgPostWriteUserNick.text = nick

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //getImageData(key.toString())
        if (key != null) {
            Log.d("수정 key 찍기 : ", key)
            etPostWriteTitle.setText(title)
            etPostWriteContent.setText(content)
        }


        imgPostWriteUserNick.setText(email)

        fun getImageData(key: String) {
            val storageReference = Firebase.storage.reference.child("$key.png")

            storageReference.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                    Glide.with(this)
                        .load(task.result)
                        .into(imgPostWritePicture) //지역변수

                }
            }
        }


        imgPostWritePicture.setOnClickListener {
            //사진 데이터 준비
            imgUpload= true
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
                    latitude = location.latitude
                    longitude = location.longitude
                    val accuracy = location.accuracy
                    Log.d("gps 받아오기","{$latitude}, {$longitude}")

                    //GRPC 오류? try catch 문으로 오류 대처
                    try {
                        addr = geocoder.getFromLocation(latitude, longitude, 1).first().getAddressLine(0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    tvPostWriteLocation.text = addr

//                    var citylist = geocoder.getFromLocation(latitude, longitude, 10);
//                    var city = citylist.get(0).getAddressLine(0)
                    Log.d("주소뭐임","$addr")
                    Toast.makeText(this, "$addr", Toast.LENGTH_SHORT).show()

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
                intent.putExtra("lon", location?.longitude.toString())
                intent.putExtra("lat", location?.latitude.toString())
                intent.putExtra("addr",addr)

                startActivity(intent)

            }

        }


        btnPostWriteSend.setOnClickListener {
            val db = Firebase.database
            //Firebase에 업로드하기

            val title = etPostWriteTitle.text.toString()
            val content = etPostWriteContent.text.toString()
            val time = FBAuth.getTime()
            val uid = FBAuth.getUid()


            var newKey = FBdataBase.getBoardRef().push().key.toString()

            if (key == newKey) {

                val Content = db.getReference("board").child(key.toString())
                Content.setValue(null)

                editPostData(key.toString())

                if (imgUpload) imgUpload(key!!)
            }


            Log.d("작성 글 key 찍기", newKey)

//                    var etPost = FBdataBase.getBoardRef().child(key2)
//                        .setValue(PostVO(title, content, "일반", uid, time))

            FBdataBase.getBoardRef().child(newKey!!)
                .setValue(PostVO(title, content, nick, uid, time))
//                    Log.d("etPost", etPost.toString())

            finish()

        }

    } // onCreate 밖


    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //받아올 결과값이 맞는지 확인 과정
        if (it.resultCode == RESULT_OK) imgPostWritePicture.setImageURI(it.data?.data)

    }

    fun imgUpload(key: String) {

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

//    fun getImageData(key: String) {
//        val storageReference = Firebase.storage.reference.child("$key.png")
//
//        storageReference.downloadUrl.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
//                Glide.with(this)
//                    .load(task.result)
//                    .into(imgPostWritePicture) //지역변수
//
//            }
//        }
//
//
//    }

    fun editPostData(key: String) {
        FBdataBase.getBoardRef().child(key).setValue(
            PostVO(
                etPostWriteTitle.text.toString(),
                etPostWriteContent.text.toString(),
                "일반",
                FBAuth.getUid(),
                FBAuth.getTime()
            )
        )
        // 수정 확인 메시지
        Toast.makeText(this, "게시글이 수정되었습니다", Toast.LENGTH_SHORT).show()
        finish()
    }


}