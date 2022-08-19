package com.example.geeksasaeng.Profile

import android.util.Log
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementDeatilResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementDetailRequest
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementDetailView
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentLookNoticeBinding

class LookNoticeFragment: BaseFragment<FragmentLookNoticeBinding>(FragmentLookNoticeBinding::inflate), ProfileAnnouncementDetailView{ //공지 상세보기화면

    private var noticeItemId: Int? = null
    private lateinit var profileService: ProfileDataService

    override fun initAfterBinding() {
        noticeItemId = requireArguments().getInt("noticeItemId")
        Log.d("lookNotice", noticeItemId.toString())
        profileService = ProfileDataService() //서비스 객체 생성
        profileService.setProfileAnnouncementDetailView(this)
        profileService.ProfileAnnouncementDetailSender(ProfileAnnouncementDetailRequest(noticeItemId!!)) //★ 공지 상세보기 api 호출
        initClickListener()
    }

    private fun initClickListener(){
        binding.lookNoticeBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    // 공지 상세보기 성공
    override fun onProfileAnnouncementDetailSuccess(result: ProfileAnnouncementDeatilResult) {
        Log.d("lookNotice","상세보기 api 성공")
        binding.lookNoticeTitleTv.text = result.title
        binding.lookNoticeContentTv.text = result.content
    }

    // 공지 상세보기 실패
    override fun onProfileAnnouncementDetailFailure(message: String) {
        Log.d("lookNotice","상세보기 api 실패 : "+ message.toString())
    }
}