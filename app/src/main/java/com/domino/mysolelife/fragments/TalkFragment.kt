package com.domino.mysolelife.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.domino.mysolelife.R
import com.domino.mysolelife.board.BoardInsideActivity
import com.domino.mysolelife.board.BoardListLVAdapter
import com.domino.mysolelife.board.BoardModel
import com.domino.mysolelife.board.BoardWriteActivity
import com.domino.mysolelife.contentsList.ContentModel
import com.domino.mysolelife.databinding.FragmentTalkBinding
import com.domino.mysolelife.databinding.FragmentTipBinding
import com.domino.mysolelife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private val TAG = TalkFragment::class.java.simpleName

    private lateinit var boardRVAdapter : BoardListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)


        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListview.adapter = boardRVAdapter



        // 게시글 클릭 시 그 게시글 안으로 들어가는 것에 대한 방법

        // 첫번째 방법으로는 listView에 있는 데이터(title 등등)들을 모두 다 다른액티비티로 전달해줘서 만들기
//        binding.boardListview.setOnItemClickListener { parent, view, position, id ->
//
//            val intent = Intent(context, BoardInsideActivity::class.java)
//            intent.putExtra("title",boardDataList[position].title)
//            intent.putExtra("content",boardDataList[position].content)
//            intent.putExtra("time",boardDataList[position].time)
//            startActivity(intent)
//        }
        // 두번째 방법으로, Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
        // 데이터의 key값을 이용
        binding.boardListview.setOnItemClickListener { parent, view, position, id ->


            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context,BoardWriteActivity::class.java)
            startActivity(intent)

        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)

        }
        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)

        }
        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)

        }

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData() {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for(dataModel in dataSnapshot.children){

                    Log.d(TAG,dataModel.toString())
                    //dataModel.key

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }
                boardKeyList.reverse()
                boardDataList.reverse() // 데이터들을 최신순으로 보이도록 뒤집어준다
                boardRVAdapter.notifyDataSetChanged()

            }


            override fun onCancelled(databaseError: DatabaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }


}