package com.domino.mysolelife.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domino.mysolelife.R
import com.domino.mysolelife.utils.FBAuth
import com.domino.mysolelife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef : DatabaseReference

    val bookmarkIdList = mutableListOf<String>()

    lateinit var rvAdapter : ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()

        rvAdapter = ContentRVAdapter(baseContext, items,itemKeyList,bookmarkIdList)

        val database = Firebase.database

        val category = intent.getStringExtra("category")





        if(category == "category1"){
            myRef = database.getReference("contents")
        }else if(category == "category2") {
            myRef = database.getReference("contents2")
        }

        // db에서 컨텐츠들을 받아오는 코드
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    Log.d("ContentListActivity",dataModel.toString())
                    Log.d("ContentListActivity",dataModel.key.toString())

                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString()) // db에 있는 컨텐츠들의 키값을 넣어놓는다
                }
                rvAdapter.notifyDataSetChanged() // 어댑터 동기화
                Log.d("ContentListActivity",items.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)


        val rv : RecyclerView =findViewById(R.id.rv)



        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(this,2)

//        rvAdapter.itemClick = object : ContentRVAdapter.ItemClick{
//            override fun onClick(view: View, position: Int) {
//                Toast.makeText(baseContext,items[position].title, Toast.LENGTH_LONG).show()
//
//                val intent = Intent(this@ContentListActivity,ContentShowActivity::class.java)
//                intent.putExtra("url",items[position].webUrl)
//                startActivity(intent)
//            }
//
//        }

        getBookmarkData()


    }

    // 북마크 데이터 받아오기
    private fun getBookmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 북마크 동적삭제 버그 수정
                //북마크 부분의 데이터가 수정될때 이 함수가 실행되는데
                //데이터 클리어를 한번 해주어야 그전 데이터들이 안쌓인다
                bookmarkIdList.clear()

                for(dataModel in dataSnapshot.children){
                    //Log.d("getBookmark",dataModel.key.toString())
                    bookmarkIdList.add(dataModel.key.toString())
                    //Log.d("getBookmark",dataModel.toString())
                }
                Log.d("getBookmark",bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)


    }


}