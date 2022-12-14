package com.example.planter.UserAuth


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.planter.MainActivity
import com.example.planter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnIntroLogin = findViewById<Button>(R.id.btnIntroLogin)
        val btnIntroJoin = findViewById<Button>(R.id.btnIntroJoin)
        val btnIntroLogOut = findViewById<Button>(R.id.btnIntroLogOut)
        val btnIntroDel = findViewById<Button>(R.id.btnIntroDel)
        val btnIntroEdit = findViewById<Button>(R.id.btnIntroEdit)



        auth = Firebase.auth


        // 로그인 버튼
        btnIntroLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        // 회원가입 버튼
        btnIntroJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }


        // 로그아웃 버튼
        btnIntroLogOut.setOnClickListener {

            auth.signOut()
            Toast.makeText(this, "로그아웃하셨습니다", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        // 회원탈퇴 버튼
        btnIntroDel.setOnClickListener {


            // AlertDialog 옵션 정보를 담고 builder 생성!
            // AlertDialog.Builder(this).setTitle("asd").setMessage("asdf")
            // 이런 식으로 한 줄에 쓸 수도 있음
            val builder = AlertDialog.Builder(this)

            builder.setIcon(R.drawable.dotol)
            builder.setTitle("도토리")
            builder.setMessage("정말 탈퇴할거야?")

//            builder.setPositiveButton("탈퇴", DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getApplicationContext(), "Yeah!!", Toast.LENGTH_LONG).show();
//                }
            builder.setPositiveButton("탈퇴하기"){dialogInterface, it ->


                auth.currentUser?.delete()
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "탈퇴 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                
            }
            builder.setNegativeButton("취소", null)

            // 주의
            // builder를 통해 옵션을 단 이후
            // 맨 마지막에는 무조건!! show()함수를 달아야 한다

            builder.show()

        }


        // 회원정보 수정
        btnIntroEdit.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)

        }
    }
}

private fun AlertDialog.Builder.setPositiveButton(s: String, onClickListener: DialogInterface.OnClickListener, function: () -> Unit) {

}
