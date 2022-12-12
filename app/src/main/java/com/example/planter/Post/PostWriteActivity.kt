package com.example.planter.Post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.planter.R

class PostWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)


        val imgPostWriteImg = findViewById<ImageView>(R.id.imgPostWriteImg)

    }
}