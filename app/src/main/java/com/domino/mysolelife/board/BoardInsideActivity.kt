package com.domino.mysolelife.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.domino.mysolelife.R
import com.domino.mysolelife.comment.CommentLVAdapter
import com.domino.mysolelife.comment.CommentModel
import com.domino.mysolelife.databinding.ActivityBoardInsideBinding
import com.domino.mysolelife.utils.FBAuth
import com.domino.mysolelife.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key : String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_board_inside)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener {

            showDialog()
        }

        // 첫번째 방법은 잠시 주석으로
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text = time

        // 두번째 방법

        key = intent.getStringExtra("key").toString()

        // Toast.makeText(this,key,Toast.LENGTH_LONG).show()
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

        getCommentData(key)

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter



    }

    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)

                }
                commentAdapter.notifyDataSetChanged()


            }


            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }



    fun insertComment(key : String){
        // db 형식 스케치
        // comment
        //  - BoardKey(이미 알고있는 key값)
        //      - CommentKey(자동생성됨)
        //          - CommentData
        FBRef
            .commentRef
            .child(key)
            .push()
            .setValue(CommentModel(binding.commentArea.text.toString(),FBAuth.getTime()))

        Toast.makeText(this,"댓글 입력 완료",Toast.LENGTH_LONG).show()
        binding.commentArea.setText("") // 댓글입력창 깨끗하게 리프레시

    }

    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this,"수정버튼 눌렀음",Toast.LENGTH_LONG).show()

            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)


        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this,"삭제완료",Toast.LENGTH_LONG).show()
            finish()

        }


    }

    private fun getImageData(key : String){

        // db로 들어가는 경로 설정
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task->
            if(task.isSuccessful)
            {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{
                binding.getImageArea.isVisible = false
            }
        })
    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 클릭한 게시글의 uid만 필요하므로 원래있던 for문은 필요가 없다

                try{
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, "트라이문")

                    binding.titleArea.text=dataModel!!.title
                    binding.textArea.text=dataModel!!.content
                    binding.timeArea.text=dataModel!!.time

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid
                    if(myUid.equals(writerUid)){
                        Toast.makeText(baseContext,"내가 글쓴이",Toast.LENGTH_LONG).show()
                        binding.boardSettingIcon.isVisible = true
                    }else{
                        Toast.makeText(baseContext,"내가 글쓴이 아님",Toast.LENGTH_LONG).show()
                    }

                }catch(e:Exception){
                    Log.d(TAG, "캐치문")

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // .child를 붙여주어 key를 타고 안으로 들어가게 함
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }


}