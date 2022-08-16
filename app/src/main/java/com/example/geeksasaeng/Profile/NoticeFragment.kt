package com.example.geeksasaeng.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Profile.Adapter.Announcement
import com.example.geeksasaeng.Profile.Adapter.NoticeRVAdapter
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentNoticeBinding
import com.example.geeksasaeng.databinding.FragmentProfileBinding


class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    private var announcementArray = ArrayList<Announcement>()
    private lateinit var noticeAdapter: NoticeRVAdapter

    override fun initAfterBinding() {
        initAdapter()
    }

    private fun initAdapter(){
        noticeAdapter = NoticeRVAdapter(announcementArray)
        binding.noticeRv.adapter = noticeAdapter
        binding.noticeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        noticeAdapter.setOnItemClickListener(object : NoticeRVAdapter.OnItemClickListener{
            override fun onItemClick(announcement: Announcement) {
                //아이템 클릭할 때 수행할 행동
            }

        })
    }

}