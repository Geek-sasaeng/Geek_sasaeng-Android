package com.example.geeksasaeng.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileRecentActivityResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileRecentActivityView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Retrofit.SocialSignUpRequest
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding
import com.example.geeksasaeng.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate), ProfileRecentActivityView {

    lateinit var profileRecentActivityService: ProfileDataService

    override fun initAfterBinding() {
        binding.profileNoticeBelowTv.isSelected = true //물흐르는 애니메이션

        clearBackStack()
        initClickListener()
        initRecentActivityService()
    }

    private fun clearBackStack() {
        val fragmentManager: FragmentManager = (context as MainActivity).supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

    private fun initRecentActivityService() {
        profileRecentActivityService = ProfileDataService() // 서비스 객체 생성
        profileRecentActivityService.setProfileRecentActivityView(this)
        profileRecentActivityService.profileRecentActivitySender()
    }

    override fun onProfileRecentActivitySuccess(result: ArrayList<ProfileRecentActivityResult>?) {
        Log.d("PROFILE-RESPONSE", "SUCCESS")
        for (i in 0 until result!!.size) {
            recentActivityBind(result[i], i)
        }
    }

    override fun onProfileAnnouncementFailure(message: String) {
        Log.d("PROFILE-RESPONSE", "FAILED")
        showToast(message)
    }

    private fun recentActivityBind(activity: ProfileRecentActivityResult, index: Int) {
        if (index == 0) {
            binding.profilePartyLayoutCv1.visibility = View.VISIBLE
            // TODO: 임시로 넣은 부분
            binding.profilePartyLayoutSectionIv1.setImageResource(R.drawable.ic_delivery)
            binding.profilePartyTitleTv1.text = activity.title
        } else if (index == 1) {
            binding.profilePartyLayoutCv2.visibility = View.VISIBLE
            // TODO: 임시로 넣은 부분
            binding.profilePartyLayoutSectionIv2.setImageResource(R.drawable.ic_delivery)
            binding.profilePartyTitleTv2.text = activity.title
        } else if (index == 2) {
            binding.profilePartyLayoutCv3.visibility = View.VISIBLE
            // TODO: 임시로 넣은 부분
            binding.profilePartyLayoutSectionIv3.setImageResource(R.drawable.ic_delivery)
            binding.profilePartyTitleTv3.text = activity.title
        }
    }
}