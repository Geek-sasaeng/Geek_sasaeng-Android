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
        binding.profileNoticeBtn.setOnClickListener {
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val noticeFragment = NoticeFragment()
            transaction.addToBackStack("profile_announcement").replace(R.id.main_frm, noticeFragment)
            transaction.commit()
        }

        binding.profileInquiryBtn.setOnClickListener {
            val urlintent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_Sxolhxj"))
            startActivity(urlintent)
        }

        binding.profileTosBtn.setOnClickListener {
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.putExtra("status","profile")
            startActivity(intent)
        }
//        binding.profileLogoutBtn.setOnClickListener {
//            (context as MainActivity).finish()
//            removeAutoLogin()
//            startActivity(Intent(activity, LoginActivity::class.java))
//        }
    }
}