package com.domino.mysolelife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.domino.mysolelife.MainActivity
import com.domino.mysolelife.R
import com.domino.mysolelife.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityJoinBinding


    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_join)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this,R.layout.activity_join)

        binding.joinBtn.setOnClickListener {


            var isGoToJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            // 저 입력칸에 값이 비어있는지 확인
            if(email.isEmpty()){
                Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if(password1.isEmpty()) {
                Toast.makeText(this,"비밀번호1을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin =false
            }
            if(password2.isEmpty()){
                Toast.makeText(this,"비밀번호2를 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin =false
            }

            // 비밀번호 2개가 같은지 확인
            if(!password1.equals(password2)) {
                Toast.makeText(this,"비밀번호를 똑같이 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin =false
            }
            // 비밀번호 6자리 이상
            if(password1.length<6) {
                Toast.makeText(this,"비밀번호를 6자리 이상 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin =false
            }

            // 모든 조건이 true일때 회원가입 완료
            if(isGoToJoin){
                auth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this,"success", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this,"fail", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }


    }
}