package com.domino.mysolelife.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.domino.mysolelife.R
import com.domino.mysolelife.utils.FBAuth
import com.domino.mysolelife.utils.FBRef

class BookmarkRVAdapter (val context : Context,
                        val items : ArrayList<ContentModel>,
                        val keyList : ArrayList<String>,
                        val bookmarkIdList : MutableList<String> )
    : RecyclerView.Adapter<BookmarkRVAdapter.Viewholder>() {

//    // 리사이클러뷰는 클릭리스너를 따로 만들어줘야함
//    interface ItemClick{
//        fun onClick(view : View,position : Int)
//    }
//    var itemClick : ItemClick? = null
//    // 여기까지 클릭 관련코드

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRVAdapter.Viewholder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item,parent,false)

        Log.d("BookmarkRVAdapter",keyList.toString())
        Log.d("BookmarkRVAdapter",bookmarkIdList.toString())

        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRVAdapter.Viewholder, position: Int) {

//        // 리사이클러뷰는 클릭리스너를 따로 만들어줘야함
//        if(itemClick != null){
//           holder.itemView.setOnClickListener { v->
//               itemClick?.onClick(v,position)
//           }
//       }
//        // 여기까지 클릭 관련코드
//        // 클릭코드 작성하고나서 ContentListActivity에서 rvAdapter.itemClick 이부분 작성한거임
        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(item : ContentModel, key : String) {

            // 아이템클릭 리스너 달아주는 두번째 방법 ( 첫번째는 저 위에 주석 참고 )
            itemView.setOnClickListener {
                Toast.makeText(context,item.title, Toast.LENGTH_LONG).show()
                val intent = Intent(context,ContentShowActivity::class.java)
                intent.putExtra("url",item.webUrl)
                itemView.context.startActivity(Intent(intent))

            }


            // 아래 두줄 설명
            // itemView는 content_rv_item에서 받아온 뷰이다
            // item은 ContentModel에서 받아온 하나하나의 리스트아이템이다
            // 그러니까 content_rv_item에서 뷰를 받아오는데
            // 거기서 textArea의 영역을 받아오고 그곳을 ContentModel에서 받아온 item들에 들어있는 title로
            // 변경한다
            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if(bookmarkIdList.contains(key)){
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            }else{
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }



            contentTitle.text=item.title

            // 글라이드를 쓰려면 context를 받아와야해서 어댑터 매개변수에 val context 추가했음
            //imageUrl에 있는걸 imageViewArea에 넣겠다 라는 뜻임
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)


        }
    }
}