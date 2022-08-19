package com.example.geeksasaeng.Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()
        binding.profileNoticeBelowTv.setSingleLine(true)
        binding.profileNoticeBelowTv.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.profileNoticeBelowTv.isSelected = true //물흐르는 애니메이션
    }

    private fun initClickListener() {
        binding.profileNoticeBtn.setOnClickListener { //공지사항
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val noticeFragment = NoticeFragment()
            transaction.addToBackStack("profile_announcement").replace(R.id.main_frm, noticeFragment)
            transaction.commit()
        }

        binding.profileMyInfoBtn.setOnClickListener {  //나의 정보
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val fragment = ProfileMyInfoFragment()
            transaction.addToBackStack("profile_my_info").replace(R.id.main_frm, fragment)
            transaction.commit()
        }

        binding.profileInquiryBtn.setOnClickListener { //문의하기
            val urlintent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_Sxolhxj")) //긱사생 카카오톡 플러스 친구 링크
            startActivity(urlintent)
        }

        binding.profileTosBtn.setOnClickListener { //서비스 이용 약관 보기
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.putExtra("status","profile")
            startActivity(intent)
        }
    }
}