package com.domino.mysolelife.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.domino.mysolelife.R
import com.domino.mysolelife.auth.IntroActivity
import com.domino.mysolelife.utils.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val logoutBtn : Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            auth.signOut()

            Toast.makeText(this,"로그아웃",Toast.LENGTH_LONG).show()

            val intent = Intent(this,IntroActivity::class.java)

            // 로그아웃 후에 뒤로가기를 누르면 다시 로그아웃 창이 나온다
            // 이를 해결하기위해 이전 액티비티를 다 지움
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP


            startActivity(intent)
        }


    }
}