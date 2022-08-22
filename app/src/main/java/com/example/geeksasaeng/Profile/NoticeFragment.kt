package com.example.geeksasaeng.Profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Adapter.NoticeRVAdapter
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementView
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentNoticeBinding
import com.example.geeksasaeng.databinding.FragmentProfileBinding


class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate), ProfileAnnouncementView {
    private var announcementArray = ArrayList<ProfileAnnouncementResult>()
    private lateinit var noticeAdapter: NoticeRVAdapter
    private lateinit var profileService: ProfileDataService

    override fun initAfterBinding() {
        initAdapter()
        initClickListener()
    }

    override fun onStart() {
        super.onStart()
        profileService = ProfileDataService() //서비스 객체 생성
        profileService.setProfileAnnouncementView(this)
        profileService.profileAnnouncementSender() //★ 공지사항 목록 불러오기 api 호출
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
            override fun onItemClick(announcement: ProfileAnnouncementResult, pos: Int) {
                //아이템 클릭할 때 수행할 행동
                var noticeItemId = noticeAdapter.getNoticeItemId(pos)!!

                val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putInt("noticeItemId", noticeItemId)
                Log.d("lookNotice- NoticeFrag", noticeItemId.toString())

                val lookNoticeFragment = LookNoticeFragment()
                lookNoticeFragment.arguments = bundle

                transaction.addToBackStack("lookNotice").replace(R.id.main_frm, lookNoticeFragment)
                transaction.commit()
            }
        })
    }

    override fun onProfileAnnouncementSuccess(result: ArrayList<ProfileAnnouncementResult>) {

        announcementArray.clear()
        Log.d("announcement", result.toString())
        for (i in result) {
            announcementArray.add(ProfileAnnouncementResult(i.content, i.createdAt, i.id, i.imgUrl, i.title, i.createdAt))
        }
        noticeAdapter.notifyDataSetChanged()
    }

    override fun onProfileAnnouncementFailure(message: String) {

    }

}