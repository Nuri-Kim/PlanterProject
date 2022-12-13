package com.example.planter.Api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planter.R

class WeatherAdapter(val context: Context, val weatherList: ArrayList<WeatherVO>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvHomeWind: TextView
        val tvHomeHour: TextView
        val tvHomeTemp: TextView
        val tvHomeHum: TextView
        val tvHomeSun: TextView
        val tvHomeLoc: TextView
        val tvHomeState: TextView

        init {
            tvHomeWind = itemView.findViewById<TextView>(R.id.tvHomeWind)
            tvHomeHour = itemView.findViewById<TextView>(R.id.tvHomeHour)
            tvHomeTemp = itemView.findViewById<TextView>(R.id.tvHomeTemp)
            tvHomeHum = itemView.findViewById<TextView>(R.id.tvHomeHum)
            tvHomeSun = itemView.findViewById<TextView>(R.id.tvHomeSun)
            tvHomeLoc = itemView.findViewById<TextView>(R.id.tvHomeLoc)
            tvHomeState = itemView.findViewById<TextView>(R.id.tvHomeState)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.weather_list,null)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvHomeLoc.setText(weatherList.get(position).city)
        holder.tvHomeState.setText(weatherList.get(position).state)
        holder.tvHomeTemp.setText(weatherList.get(position).temp)
        holder.tvHomeHum.setText(weatherList.get(position).humidity)
        holder.tvHomeWind.setText(weatherList.get(position).speed)
        holder.tvHomeSun.setText("일출:${weatherList.get(position).sunrise}" +
                "\n일몰:${weatherList.get(position).sunset}")
    }
    override fun getItemCount(): Int {
        return weatherList.size
    }
}