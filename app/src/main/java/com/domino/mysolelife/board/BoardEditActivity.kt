package com.domino.mysolelife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.domino.mysolelife.R
import com.domino.mysolelife.databinding.ActivityBoardEditBinding
import com.domino.mysolelife.utils.FBAuth
import com.domino.mysolelife.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String

    private lateinit var binding : ActivityBoardEditBinding

    private val TAG = BoardEditActivity::class.java.simpleName

    private lateinit var writerUid : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_board_edit)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)


        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }
    }


    private fun editBoardData(key : String){

        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(binding.titleArea.text.toString(),
                binding.contentArea.text.toString(),
                writerUid,
                FBAuth.getTime())
            )

        Toast.makeText(this,"수정완료",Toast.LENGTH_LONG).show()

        finish()

    }

    private fun getImageData(key : String){

        // db로 들어가는 경로 설정
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task->
            if(task.isSuccessful)
            {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{

            }
        })
    }


    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                    binding.titleArea.setText(dataModel?.title)
                    binding.contentArea.setText(dataModel?.content)
                    writerUid= dataModel!!.uid.toString()


            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // .child를 붙여주어 key를 타고 안으로 들어가게 함
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
}