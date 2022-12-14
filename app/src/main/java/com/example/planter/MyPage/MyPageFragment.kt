package com.example.planter.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.planter.Api.HomeFragment
import com.example.planter.MainActivity
import com.example.planter.R
import com.example.planter.Social.BookmarkActivity
//import com.example.planter.Social.BookmarkActivity
import com.example.planter.UserAuth.EditActivity
import com.example.planter.utils.FBAuth
import com.example.planter.utils.FBdataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MyPageFragment : Fragment() {

    lateinit var auth : FirebaseAuth
    lateinit var nick : String
    lateinit var uid : String
    lateinit var imgMypage : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = Firebase.auth
        var uid = FBAuth.getUid()


        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        imgMypage = view.findViewById(R.id.imgMypage)
        val tvMypageNick = view.findViewById<TextView>(R.id.tvMypageNick)
        val btnMyPageLogOut = view.findViewById<Button>(R.id.btnMyPageLogOut)
        val tvMyPageDel = view.findViewById<TextView>(R.id.tvMyPageDel)
        val btnMyPageEdit = view.findViewById<Button>(R.id.btnMyPageEdit)
        val btnMyBookmark = view.findViewById<Button>(R.id.btnMyBookmark)

        val user = FBAuth.getUid()
        // ????????? uid ????????? ????????????
        FBdataBase.getJoinRef().child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nick = snapshot.child("nick").value as String

                getImageData(user)
                tvMypageNick.text = nick

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })








        btnMyPageEdit.setOnClickListener {

            val intent = Intent(context,EditActivity::class.java)
            startActivity(intent)
        }



        btnMyPageLogOut.setOnClickListener {
            auth.signOut()
            Toast.makeText(context,"???????????? ???????????????", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        tvMyPageDel.setOnClickListener {

            // AlertDialog ?????? ????????? ?????? builder ??????!
            // AlertDialog.Builder(this).setTitle("asd").setMessage("asdf")
            // ?????? ????????? ??? ?????? ??? ?????? ??????
            val builder = AlertDialog.Builder(requireContext())


//            builder.setPositiveButton("??????", DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getApplicationContext(), "Yeah!!", Toast.LENGTH_LONG).show();
//                }
            builder.setTitle("??????")
            builder.setMessage("?????????????????????????")
            builder.setPositiveButton("????????????"){dialogInterface, it ->


                auth.currentUser?.delete()
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "?????? ??????", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)

            }
            builder.setNegativeButton("??????", null)

            // ??????
            // builder??? ?????? ????????? ??? ??????
            // ??? ??????????????? ?????????!! show()????????? ????????? ??????

            builder.show()


        }



        btnMyBookmark.setOnClickListener {


            Log.d("?????? ???????????? uid", uid)
            val intent = Intent(requireContext(), BookmarkActivity::class.java)
            intent.putExtra("uid", uid.toString())
            startActivity(intent)

//            bookmark??? ????????? ?????????

//            requireActivity().supportFragmentManager.beginTransaction().replace(
//                R.id.flMain,
//
//                BookmarkFragment()
//            ).commit()

        }


        return view





    }
    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: ?????? ?????? ????????? ???????????? ???????????????
                Glide.with(requireContext())
                    .load(task.result)
                    .into(imgMypage) //????????????

            }
        }
    }


}
