package com.example.planter.Api

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.planter.R
import org.json.JSONObject
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    val sdf = SimpleDateFormat("HH시 mm분")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //현재시간(ㅁ밀리)
        val currentMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant()?.toEpochMilli() ?: 0
        //localdate로 변경
        val currentDateTime =
            Instant.ofEpochMilli(currentMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val time = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분").format(currentDateTime)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val imgHomeReset = view.findViewById<ImageView>(R.id.imgHomeReset)

        val lvHome = view.findViewById<ListView>(R.id.lvHome)
        val rvHome = view.findViewById<RecyclerView>(R.id.rvHome)

        val weatherList = ArrayList<WeatherVO>()

        //Volley 활용한 네트워크 통신
        //1. volley 라이브러리 설정, 인터넷 권한 허용
        //2. RequestQueue 생성
        val requestQueue = Volley.newRequestQueue(requireContext())
        val adapter = WeatherAdapter(requireContext(), weatherList)
        rvHome.adapter = adapter
        rvHome.layoutManager = LinearLayoutManager(requireContext())

        //3. Request 생성 , //city는 gps에서 정보를 받아온 후 수정
        imgHomeReset.setOnClickListener {

            var city = "Gwangju"

            val requestList = ArrayList<StringRequest>()

            var url: String =
                "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=5ac7fcaff466bab54c034066bbce6ac6&units=metric"

            var request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    Log.d("날씨:", response)
                    var result = JSONObject(response)
                    var weathers = result.getJSONArray("weather")
                    var weather = weathers.get(0) as JSONObject
                    var state = weather.getString("main")

                    var main = result.getJSONObject("main")
                    var temp = main.getString("temp")
                    var humidity = main.getString("humidity")

                    var wind = result.getJSONObject("wind")
                    var speed = wind.getString("speed")

                    var sys = result.getJSONObject("sys")
//                    val time1 = Instant.ofEpochMilli((sys.getString("sunrise").toLong())*1000).
//                    atZone(ZoneId.systemDefault()).toLocalDateTime()
//                    DateTimeFormatter.ofPattern("HH시 mm분").format(time1)
                    val time1 = (sys.getString("sunrise").toLong())*1000
                    val date1 = Date(time1)
                    var sunrise = sdf.format(date1)

                    val time2 = Instant.ofEpochMilli((sys.getString("sunset").toLong())*1000).
                    atZone(ZoneId.systemDefault()).toLocalDateTime()
                    var sunset = DateTimeFormatter.ofPattern("HH시 mm분").format(time2)
//
                    Log.d("날씨1:", "${sys.getString("sunrise").toLong()},${sys.getString("sunset").toLong()}")
                    Log.d("날씨2:", "$date1,$time2")
                    Log.d("현재날씨:", "상태:$state, 온도:$temp, 습도:$humidity, 풍속:$speed")
                    weatherList.add(WeatherVO(city, state, temp, humidity, speed, sunrise, sunset, time))
                    adapter.notifyDataSetChanged()
                },
                {})

            requestList.add(request)
            requestQueue.add(requestList.get(0))

            adapter.notifyDataSetChanged()
        }

        return view
    }

}