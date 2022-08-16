package com.example.geeksasaeng.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Profile.Adapter.NoticeRVAdapter
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementResult
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentNoticeBinding
import com.example.geeksasaeng.databinding.FragmentProfileBinding


class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    private var announcementArray = ArrayList<ProfileAnnouncementResult>() //TODO: 이건 상세조회....고 목록 조회나오면 그거 Result로 바꿔야겠다.
    private lateinit var noticeAdapter: NoticeRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        initClickListener()
        //공지사항 더미데이터
        announcementArray.add(ProfileAnnouncementResult(1, "공지사항제목", "공지사항 내용", "이미지 유알앨", "생성시간", "업데이트 시간", "ACTIVE"))
        announcementArray.add(ProfileAnnouncementResult(2, "공지사항제목2", "공지사항 내용2", "이미지 유알앨", "생성시간", "업데이트 시간", "ACTIVE"))
    }

    private fun initClickListener(){
        binding.noticeBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
            activity?.supportFragmentManager?.popBackStack()
        }
    }
    private fun initAdapter(){
        noticeAdapter = NoticeRVAdapter(announcementArray)
        binding.noticeRv.adapter = noticeAdapter
        binding.noticeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        noticeAdapter.setOnItemClickListener(object : NoticeRVAdapter.OnItemClickListener{
            override fun onItemClick(announcement: ProfileAnnouncementResult) {
                //아이템 클릭할 때 수행할 행동
            }

        })
    }

}