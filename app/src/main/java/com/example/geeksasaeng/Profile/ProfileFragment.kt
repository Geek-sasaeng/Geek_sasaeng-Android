package com.example.geeksasaeng.Profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileRecentActivityResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileRecentActivityView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding
import com.example.geeksasaeng.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate), ProfileRecentActivityView {
    override fun initAfterBinding() {
        clearBackStack()
        binding.profileNoticeBelowTv.isSelected = true //물흐르는 애니메이션
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileNoticeBtn.setOnClickListener {
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val noticeFragment = NoticeFragment()
            transaction.addToBackStack("profile_announcement").replace(R.id.main_frm, noticeFragment)
            transaction.commit()
        }

        binding.profileMyActivity.setOnClickListener {
            startActivity(Intent(activity, ProfileMyActivityActivity::class.java))
        }

        binding.profileMyAccount.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("profile_my_info").replace(R.id.main_frm, ProfileMyInfoFragment()).commit()
        }
    }

    private fun clearBackStack() {
        val fragmentManager: FragmentManager = (context as MainActivity).supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onProfileRecentActivitySuccess(result: ProfileRecentActivityResult) {
        // 정보 바인딩
    }

    override fun onProfileAnnouncementFailure(message: String) {
        showToast(message)
    }
}