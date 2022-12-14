package com.example.planter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.planter.Api.HomeFragment
import com.example.planter.Chat.ChatFragment
import com.example.planter.MyPage.MyPageFragment
import com.example.planter.Post.PostFragment
import com.example.planter.UserAuth.IntroActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imgLogo = findViewById<ImageView>(R.id.imgLogo)
        val bnvMain = findViewById<BottomNavigationView>(R.id.bnvMain)
        val flMain = findViewById<FrameLayout>(R.id.flMain)

        imgLogo.setOnClickListener{
            val intent = Intent(this,IntroActivity::class.java)
            startActivity(intent)
        }

        bnvMain.setOnItemSelectedListener{
            item ->
            when(item.itemId){

                R.id.tab1 ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        HomeFragment()
                    ).commit()
                }


                R.id.tab2 ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        PostFragment()
                    ).commit()
                }

                R.id.tab3 ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        ChatFragment()
                    ).commit()
                }

                R.id.tab4 ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        MyPageFragment()
                    ).commit()
                }

            }

        true


        }
    }


}