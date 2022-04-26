package com.domino.mysolelife.comment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.domino.mysolelife.R
import com.domino.mysolelife.utils.FBAuth

class CommentLVAdapter(val commentList : MutableList<CommentModel>) : BaseAdapter(){
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView

        // view 가 재활용 되는 이슈로 if문 삭제했음
        // listview 중첩출력 현상 <- 구글링

        if(view ==null) {

            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)

        }
        val title = view?.findViewById<TextView>(R.id.titleArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)


        title!!.text = commentList[position].commentTitle
        time!!.text = commentList[position].commentCreatedTime


        return view!!
    }
}