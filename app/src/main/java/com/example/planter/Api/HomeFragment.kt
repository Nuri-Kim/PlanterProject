package com.example.planter.Api

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.planter.R
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates


class HomeFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    val sdf = SimpleDateFormat("HH시 mm분")

    var currentMillis by Delegates.notNull<Long>()
    lateinit var tvHomeSet: TextView
    lateinit var rvHome: RecyclerView
    lateinit var HomeLayout: ConstraintLayout
    lateinit var requestQueue: RequestQueue
    lateinit var adapter: WeatherAdapter
    private lateinit var adapter2: ArrayAdapter<String>
    lateinit var time: String

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    var weatherList = ArrayList<WeatherVO>()
    var stateRes = ArrayList<String>()
    var stateList = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //현재시간(ㅁ밀리)

        currentMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant()?.toEpochMilli() ?: 0
        //localdate로 변경
        var currentDateTime =
            Instant.ofEpochMilli(currentMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        time = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분").format(currentDateTime)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvHomeSet = view.findViewById<TextView>(R.id.tvHomeSet)
        val imgHomeReset = view.findViewById<ImageView>(R.id.imgHomeReset)
        val lvHome = view.findViewById<ListView>(R.id.lvHome)
        rvHome = view.findViewById<RecyclerView>(R.id.rvHome)
        val swHomeAlarm2 = view.findViewById<Switch>(R.id.swHomeAlarm2)


//        var uidRef = database.getReference("JoinList")
//
//                for(i in 0 until 1){
//            //push() key 타임스탬프를 찍어줌 (고유한 값을 만들어줌)
//            uidRef.push().setValue(
//                list[i]
//            )
//        }


        //물주기 알람
        swHomeAlarm2.setOnCheckedChangeListener{ compoundButton, ischecked ->

            if (ischecked) {
                var timePicker = TimePickerFragment()
                timePicker.show(requireActivity().supportFragmentManager, "Timepicker")
            } else {
                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                val intent = Intent(context, AlertReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

                alarmManager!!.cancel(pendingIntent)
                tvHomeSet.setText("설정된 알람 없음.")
            }
        }

        //Volley 활용한 네트워크 통신
        //1. volley 라이브러리 설정, 인터넷 권한 허용
        //2. RequestQueue 생성
        requestQueue = Volley.newRequestQueue(requireContext())
        adapter = WeatherAdapter(requireContext(), weatherList)
        rvHome.adapter = adapter
        rvHome.layoutManager = LinearLayoutManager(requireContext())

        //3. Request 생ㄴ성(함수안에서 작업)
        openWeather()

        adapter2 = ArrayAdapter<String>(requireActivity(), R.layout.state_list, R.id.tvStateList, stateRes)
        adapter2.notifyDataSetChanged()

        lvHome.adapter = adapter2


        //위치정보에 따라 날씨정보 최신화.
        imgHomeReset.setOnClickListener {
            stateRes.clear()

            openWeather()
            adapter.notifyDataSetChanged()

        }

        return view
    }

    fun openWeather() {

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 승인이 안되어있는 상태라면 알림창을 띄워서 승인할 수 있도록
                // ActivityCompat은 확인하는 기능, 요청하는 기능이 둘 다 들어가 있음
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
                )
                // requestCode: 내가 뭘 요청한 건지 구분하기 위한 숫자
                // label을 사용해 다시 setOnClickListener 돌아가 생명주기가 돌아가게끔
            }
        val manager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let {
            latitude = location.latitude
            longitude = location.longitude
            Log.d("loc", "{$latitude}, {$longitude}")
        }

        Log.d("sec", (currentMillis / 1000).toString())

        val requestList = ArrayList<StringRequest>()

        var url: String =
            "https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=5ac7fcaff466bab54c034066bbce6ac6&units=metric"

        var request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                Log.d("날씨:", response)
                var result = JSONObject(response)
                var weathers = result.getJSONArray("weather")
                var weather = weathers.get(0) as JSONObject
                var state = weather.getString("main")
                var stateId = weather.getString("id")
                if(stateId.toInt() in 200..350||stateId.toInt() in 500..540||stateId.toInt()>=900){
                    rvHome.setBackgroundResource(R.drawable.rain)
                }else if(stateId.toInt() in 600..623){
                    rvHome.setBackgroundResource(R.drawable.snow)
                }else{
                    rvHome.setBackgroundResource(R.drawable.sun)
                }

                var main = result.getJSONObject("main")
                var temp = main.getString("temp")
                var humidity = main.getString("humidity")

                var wind = result.getJSONObject("wind")
                var speed = wind.getString("speed")

                //강수량

                var sys = result.getJSONObject("sys")
//                    val time1 = Instant.ofEpochMilli((sys.getString("sunrise").toLong())*1000).
//                    atZone(ZoneId.systemDefault()).toLocalDateTime()
//                    DateTimeFormatter.ofPattern("HH시 mm분").format(time1)
                var city =  "위치요청 실패."
                if(sys.has("country")) {
                    city = sys.getString("country")
                }

                val time1 = (sys.getString("sunrise").toLong()) * 1000
                val date1 = Date(time1)
                var sunrise = sdf.format(date1)

                val time2 = Instant.ofEpochMilli((sys.getString("sunset").toLong()) * 1000)
                    .atZone(ZoneId.systemDefault()).toLocalDateTime()
                var sunset = DateTimeFormatter.ofPattern("HH시 mm분").format(time2)

                var h1:String? = null

                if(result.has("rain")){
                    var rain = result.getJSONObject("rain")
                    h1 = rain.getString("1h")
                }

                stateAdd(stateId, temp, speed, humidity, h1)

                Log.d(
                    "날씨1:",
                    "${sys.getString("sunrise").toLong()},${sys.getString("sunset").toLong()}"
                )
                Log.d("날씨2:", "$date1,$time2")
                Log.d("현재날씨:", "상태:$state, 온도:$temp, 습도:$humidity, 풍속:$speed")
                weatherList.add(
                    WeatherVO(
                        city,
                        state,
                        temp,
                        humidity,
                        speed,
                        sunrise,
                        sunset,
                        time
                    )
                )
                adapter2.notifyDataSetChanged()
                adapter.notifyDataSetChanged()
                Log.d("state", stateRes.toString())
            },
            {})

        requestList.add(request)
        requestQueue.add(requestList.get(0))

        adapter.notifyDataSetChanged()
    }

    fun stateAdd(stateId:String, temp:String, speed:String, humidity:String, h1:String?){
        stateRes.add("일몰 시간을 잘 체크해주세요.")
        var StateId = stateId.toInt()
        var Temp = temp.toDouble()
        var Rain = h1?.toDouble()
        var Speed = speed.toDouble()
        if(StateId in 200..350 || StateId>=900){
            stateRes.add("천둥,재해 등 안전한 곳으로 식물을 대피시켜 주세요.")
        }else if(StateId in 500..501 ){
            stateRes.add("가벼운 비가 내려요")
        }else if(StateId in 502..531){
            stateRes.add("비가 많이 오니 상태를 보고 환기시켜주세요.")
        }else if(StateId in 600..622){
            stateRes.add("눈이 오니 온도와 습도를 체크해주세요.")
        }else if(StateId == 800){
            stateRes.add("날씨가 맑으니 평소처럼 식물을 관리해주세요")
        }
        //관엽식물, 다육식물
        if(Temp>=20){
            stateRes.add("관엽식물은 햇빛양을 조절해주세요.")
        }else{
            stateRes.add("다육식물을 햇빛받게 해주세요.")
        }

        if(Speed>6){
            stateRes.add("강풍이 부니 창문을 조금만 열거나 닫아주세요.")
        }else{
            stateRes.add("연풍이 부니 통풍을 시켜주세요.")
        }

        if(humidity.toInt()<=30 || humidity.toInt()>=70){
            stateRes.add("습하거나 건조하니 물주는 양을 조금 줄여주세요.")
            if(humidity.toInt()<=10){
                stateRes.add("날씨가 매우 건조하니 분무기로 공중에 물을 뿌려주세요.")
            }
        }

        if(Rain!=null){
            if(Rain<=3&&StateId==500){
                stateRes.add("가벼운 비가 내리니 일부를 제외, 비를 맞아도 돼요.")
            }
            else if(Rain>3&&StateId in 500..599 ){
                stateRes.add("비가 많이 오거나 강하게 내리니 환기를 조절해주세요.")
            }
        }

        }


    //    fun sendOnChannel1(title: String?, message: String?) {
//        val nb: NotificationCompat.Builder =
//            mNotificationHelper.getChannel1Notification(title, message)!!
//        mNotificationHelper.getManager()?.notify(1, nb.build())
//    }

//    private fun SwitchStateSave(key: String, value: Boolean) {
//        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
//            "switch_State",
//            MODE_PRIVATE
//        ) // "switch_state"라는 이름으로 파일생성, MODE_PRIVATE는 자기 앱에서만 사용하도록 설정하는 기본값
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(key, value) // 키와 값을 boolean으로 저장
//        editor.apply() // 실제로 저장
//    }
//
//    private fun UpdateState(key: String): Boolean {
//        val sharedPreferences: SharedPreferences =
//            requireActivity().getSharedPreferences("switch_State", MODE_PRIVATE)
//        return sharedPreferences.getBoolean(key, false)
//    }



    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {

    }

    //저장되어 있는 child값 전부 한번 돌고 조건을 줘서 이메일이 같으면 해당 세팅값만 변경하면됨

}