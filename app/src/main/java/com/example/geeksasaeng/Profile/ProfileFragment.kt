package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Home.HomeFragment
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate), ProfileMyOngoingActivityView {

    lateinit var profileMyOngoingActivityService: ProfileDataService
    private var profileMyOngoingActivityList = ArrayList<ProfileMyOngoingActivityResult>()

    override fun initAfterBinding() {
        binding.profileCardNickNameTv.text = getNickname()
        binding.profileSignOutTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        // clearBackStack()
        initClickListener()
        initRecentActivityService()
        initRecentActivityClickListener()
    }

    private fun initClickListener() {
        /*binding.profileNotice.setOnClickListener { //공지사항
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val noticeFragment = NoticeFragment()
            transaction.addToBackStack("profile_announcement").replace(R.id.main_frm, noticeFragment)
            transaction.commit()
        }*/

        binding.profileInquiry.setOnClickListener { //문의하기
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_Sxolhxj")) //긱사생 카카오톡 플러스 친구 링크
            startActivity(urlIntent)
        }
        binding.profileMyActivity.setOnClickListener { //나의 활동 보기
            startActivity(Intent(activity, ProfileMyActivityActivity::class.java))
        }

        binding.profileMyInfo.setOnClickListener { //나의 정보 수정
            (context as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("profile_my_info").replace(R.id.main_frm, ProfileMyInfoFragment()).commit()
        }

        binding.profileTos.setOnClickListener { //서비스 이용 약관 보기
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.putExtra("status","profile")
            startActivity(intent)
        }
    }

    private fun initRecentActivityService() {
        profileMyOngoingActivityService = ProfileDataService() // 서비스 객체 생성
        profileMyOngoingActivityService.setProfileMyOngoingActivityView(this)
        profileMyOngoingActivityService.profileMyOngoingActivitySender()
    }

    override fun onProfileMyOngoingActivitySuccess(result: ArrayList<ProfileMyOngoingActivityResult>?) {
     /*   profileMyOngoingActivityList = result!!
        for (i in 0 until result.size) {
            recentActivityBind(result[i], i)
        }*/
    }

    override fun onProfileMyOngoingActivityFailure(message: String) {
        showToast(message)
    }

/*    private fun recentActivityBind(activity: ProfileMyOngoingActivityResult, index: Int) {
        when (index) {
            0 -> {
                binding.profilePartyLayoutCv1.visibility = View.VISIBLE
                binding.profilePartyLayoutSectionIv1.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profilePartyTitleTv1.text = activity.title
            } 1 -> {
                binding.profilePartyLayoutCv2.visibility = View.VISIBLE
                binding.profilePartyLayoutSectionIv2.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profilePartyTitleTv2.text = activity.title
            } 2 -> {
                binding.profilePartyLayoutCv3.visibility = View.VISIBLE
                binding.profilePartyLayoutSectionIv3.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profilePartyTitleTv3.text = activity.title
            }
        }
    }*/

    private fun initRecentActivityClickListener() {
     /*   binding.profilePartyLayoutCv1.setOnClickListener {
            val activityId = profileMyOngoingActivityList[0].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            // transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment).commit()
            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment).commit()
        }

        binding.profilePartyLayoutCv2.setOnClickListener {
            val activityId = profileMyOngoingActivityList[1].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
            transaction.commit()
        }

        binding.profilePartyLayoutCv3.setOnClickListener {
            val activityId = profileMyOngoingActivityList[2].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            // transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
            transaction.replace(R.id.main_frm, lookPartyFragment)
            transaction.commit()
        }*/
    }
}