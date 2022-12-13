package com.example.planter.Map

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.planter.R
import com.example.planter.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var discriptor: BitmapDescriptor
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient  // 위칫값 사용하기 위해
    private lateinit var locationCallback: LocationCallback  // 위칫값 요청에 대한 갱신 정보를 받기 위해


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

//        binding = ActivityMapsBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 현재 위치를 검색하기 위해서 FusedLocationProviderClient 사용
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        updateLocation()

        /* 마커 아이콘 설정 */
        var bitmapDrawable: BitmapDrawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bitmapDrawable = getDrawable(R.drawable.icon_here) as BitmapDrawable
        } else {
            bitmapDrawable = resources.getDrawable(R.drawable.icon_here) as BitmapDrawable
        }
        discriptor = BitmapDescriptorFactory.fromBitmap(bitmapDrawable.bitmap)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(35.14982776049108, 126.91994477076531)
        mMap.addMarker(MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15F))

//        updateLocation()

    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        /* 위치 정보를 요청할 locationRequest 생성하고 정확도와 주기를 설정 */
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback() {  // 해당 주기마다 반환받을 locationCallback
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for(location in it.locations) {
                        Log.d("Location", "${location.latitude} , ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }

    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)  // 전달받은 위치를 좌표로 마커를 생성

        /* 마커 추가 및 옵션 */
        val markerOptions = MarkerOptions()  // 마커 추가
            .position(LATLNG)  // 마커의 좌표
            .title("Here!")  // 마커의 제목
            .icon(discriptor)
        // .snippet("정보창 추가")

        /* 카메라 위치를 현재 위치로 세팅하고 마커와 함께 지도에 반영 */
        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)  // 카메라의 목표 지점
            .zoom(15.0f)  // 카메라 줌
            .build()
        // bearing : 지도의 수직선이 북쪽을 기준으로 시계 방향 단위로 측정되는 방향
        // tilt : 카메라의 기울기

        mMap.clear()  // 마커를 지도에 반영하기 전에 clear를 사용해서 이전에 그려진 마커가 있으면 지운다.
        mMap.addMarker(markerOptions)  // 지도에 마커를 추가
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        // CameraUpdateFactory.newCameraPosition(cameraPosition) : 카메라 포지션에 지도에서 사용할 수 있는 카메라 정보가 생성된다.
        // mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)) : 카메라 포지션을 기준으로 지도의 위치, 배율, 기울기 등이 변경돼서 표시된다.
    }

}