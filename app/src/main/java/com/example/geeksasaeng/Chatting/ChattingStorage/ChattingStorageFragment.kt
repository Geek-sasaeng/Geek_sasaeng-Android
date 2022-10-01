package com.example.geeksasaeng.Chatting.ChattingStorage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentChattingStorageBinding

class ChattingStorageFragment : BaseFragment<FragmentChattingStorageBinding>(FragmentChattingStorageBinding::inflate) {

    lateinit var chattingStorageRVAdapter: ChattingStorageRVAdapter
    private var chattingStorageList = ArrayList<ChattingStorageData>()
    private var checkBinding: Boolean = false // 바인딩 오류나는거 잡기 위해서

    override fun onResume() {
        super.onResume()
        initChattingStorageList()
        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkBinding = false
    }

    override fun initAfterBinding() {
        checkBinding = true
        initClickListener()
        initScrollListener()
    }

    private fun initChattingStorageList() {
        //TODO: 채팅 스토리지 목록 불러오기
    }

    private fun initAdapter() {
        if (checkBinding) {
            //어댑터 연결
            chattingStorageRVAdapter= ChattingStorageRVAdapter(chattingStorageList)
            binding.chattingStorageListRv.adapter = chattingStorageRVAdapter
            binding.chattingStorageListRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            chattingStorageRVAdapter.setOnItemClickListener(object :
                ChattingStorageRVAdapter.OnItemClickListener {
                override fun onItemClick(chatting: ChattingStorageData, position: Int) {
                    //TODO: 채팅방(보관함)입장하는 코드 작성
                }
            })
        }
    }

    private fun initClickListener() {
        binding.chattingStorageSectionRb1.setOnClickListener {
            binding.chattingStorageListPreparingIv.visibility = View.GONE
            binding.chattingStorageListRv.visibility = View.VISIBLE
        }
        binding.chattingStorageSectionRb2.setOnClickListener {
            binding.chattingStorageListRv.visibility = View.GONE
            binding.chattingStorageListPreparingIv.visibility = View.VISIBLE
        }
        binding.chattingStorageSectionRb3.setOnClickListener {
            binding.chattingStorageListRv.visibility = View.GONE
            binding.chattingStorageListPreparingIv.visibility = View.VISIBLE
        }

        binding.chattingStorageBackBtn.setOnClickListener { //뒤로가기
           /* parentFragmentManager.beginTransaction()
                .remove(this).commit()*/
            //이거말고 뒤로가기랑 똑같은 효과주는 거 사용하기
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun initScrollListener() {
        //fade효과 관련
        binding.chattingStorageListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 마지막 스크롤된 항목 위치
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 항목 전체 개수
                if (lastVisibleItemPosition >= itemTotalCount - 1) { //마지막 아이템 전 아이템이 되면
                    binding.chattingStorageListBottomView.visibility = View.INVISIBLE
                } else binding.chattingStorageListBottomView.visibility = View.VISIBLE
            }
        })
    }

    //TODO: 코드 다 완성되면, 로딩 애니메이션 넣기
}