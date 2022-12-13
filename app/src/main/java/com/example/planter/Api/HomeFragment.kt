package com.example.planter.Api

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.planter.R
import org.json.JSONObject


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                    val result = JSONObject(response)
                    val weathers = result.getJSONArray("weather")
                    val weather = weathers.get(0) as JSONObject
                    val state = weather.getString("main")

                    val main = result.getJSONObject("main")
                    val temp = main.getString("temp")
                    val humidity = main.getString("humidity")

                    val wind = result.getJSONObject("wind")
                    val speed = wind.getString("speed")

                    Log.d("현재날씨:", "상태:$state, 온도:$temp, 습도:$humidity, 풍속:$speed")
//                    weatherList.add(WeatherVO(city, state, temp, humidity, speed, sunrise, sunset))
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