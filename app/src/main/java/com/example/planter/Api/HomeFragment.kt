package com.example.planter.Api

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fullstackapplication.utils.FBAuth
import com.example.planter.R
import com.example.planter.UserAuth.JoinVO
import com.example.planter.databinding.ActivityMainBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


    class HomeFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    val sdf = SimpleDateFormat("HH시 mm분")

    lateinit var tvHomeSet : TextView
    val mNotificationHelper = NotificationHelper(context)

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

        tvHomeSet = view.findViewById<TextView>(R.id.tvHomeSet)
        val imgHomeReset = view.findViewById<ImageView>(R.id.imgHomeReset)
        val lvHome = view.findViewById<ListView>(R.id.lvHome)
        val rvHome = view.findViewById<RecyclerView>(R.id.rvHome)
        val swHomeAlarm1 = view.findViewById<Switch>(R.id.swHomeAlarm1)
        val swHomeAlarm2 = view.findViewById<Switch>(R.id.swHomeAlarm2)

        val list = ArrayList<JoinVO>()
        val weatherList = ArrayList<WeatherVO>()

//        var uidRef = database.getReference("JoinList")
//
//                for(i in 0 until 1){
//            //push() key 타임스탬프를 찍어줌 (고유한 값을 만들어줌)
//            uidRef.push().setValue(
//                list[i]
//            )
//        }
//
        //메시지 알람
        swHomeAlarm1.setOnCheckedChangeListener { compoundButton, ischecked ->
            if(ischecked){

            }else{

            }
        }

        //물주기 알람
        swHomeAlarm2.setOnCheckedChangeListener { compoundButton, ischecked ->
            if(ischecked){
                var timePicker = TimePickerFragment()
                timePicker.show(childFragmentManager,"Time picker")
            }else{
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                val intent = Intent(context, AlertReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

                alarmManager!!.cancel(pendingIntent)
                tvHomeSet.setText("설정된 알람 없음.")
            }
        }
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

//    fun sendOnChannel1(title: String?, message: String?) {
//        val nb: NotificationCompat.Builder =
//            mNotificationHelper.getChannel1Notification(title, message)!!
//        mNotificationHelper.getManager()?.notify(1, nb.build())
//    }

    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d("test", hourOfDay.toString())
        val c = Calendar.getInstance()

        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, 0)

        updateTimeText(c)
        startAlarm(c)
    }

    fun sendOnChannel2(title: String?, message: String?) {
        val nb: NotificationCompat.Builder =
            mNotificationHelper.getChannel2Notification(title, message)!!
        mNotificationHelper.getManager()?.notify(2, nb.build())
    }

    fun startAlarm(c: Calendar) {
        val alarmManager: AlarmManager? = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1*60*1000 ,  pendingIntent);
    }

    fun updateTimeText(c: Calendar){
        var timeText = "물주기 알람시간 : "
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        tvHomeSet.setText(timeText)
        var intent = Intent(requireContext(), AlertReceiver:: class.java)
        intent.putExtra("time", timeText)
    }

    //저장되어 있는 child값 전부 한번 돌고 조건을 줘서 이메일이 같으면 해당 세팅값만 변경하면됨

}