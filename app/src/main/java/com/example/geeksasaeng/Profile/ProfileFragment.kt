package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Chatting.ChattingRoom.DialogMatchingEnd
import com.example.geeksasaeng.Home.HomeFragment
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentProfileBinding
import java.util.*
import kotlin.collections.ArrayList

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate)
    ,ProfileMyOngoingActivityView, ProfileMyInfoView {

    lateinit var profiledataService: ProfileDataService
    private var profileMyOngoingActivityList = ArrayList<ProfileMyOngoingActivityResult>()

    // 나의정보 상세보기 다이얼로그에 넘겨줄 정보들
    private var nickName = String()
    private var universityName = String()
    private var dormitoryName = String()
    private var loginId = String()
    private var emailAddress = String()
    private var formattingPhoneNumber = String()
    private var fomattingsignUpDate = String()
    private var userId: Int = 0

    override fun initAfterBinding() {
        binding.profileCardNickNameTv.text = getNickname()
        binding.profileSignOutTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        // clearBackStack()
        initRetrofitService()
        initClickListener()
        initRecentActivityClickListener()
    }

    private fun initClickListener() {
        /*binding.profileNotice.setOnClickListener { //공지사항
            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val noticeFragment = NoticeFragment()
            transaction.addToBackStack("profile_announcement").replace(R.id.main_frm, noticeFragment)
            transaction.commit()
        }*/

        binding.profileCardLayoutWhile.setOnClickListener {
            val profileDetailDialog = DialogProfileDetail()
            val bundle = Bundle()
            bundle.putString("nickName", nickName)
            bundle.putString("universityName", universityName)
            bundle.putString("dormitoryName", dormitoryName)
            bundle.putString("loginId", loginId)
            bundle.putString("emailAddress", emailAddress)
            bundle.putString("phoneNumber", formattingPhoneNumber)
            bundle.putString("signUpDate", fomattingsignUpDate)
            profileDetailDialog.arguments = bundle
            profileDetailDialog.show(parentFragmentManager, "profileDetailDialog")
        }

        binding.profileMyActivity.setOnClickListener { //나의 활동 보기
            startActivity(Intent(activity, ProfileMyActivityActivity::class.java))
        }

        binding.profileMyInfo.setOnClickListener { //나의 정보 수정
            startActivity(Intent(activity, ProfileMyInfoUpdateActivity::class.java))
        }

        binding.profileInquiry.setOnClickListener { //문의하기
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_Sxolhxj")) //긱사생 카카오톡 플러스 친구 링크
            startActivity(urlIntent)
        }

        binding.profileTos.setOnClickListener { //서비스 이용 약관 보기
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.putExtra("status","profile")
            startActivity(intent)
        }

        binding.profileLogout.setOnClickListener { //로그아웃
            val dialog = DialogProfileLogout()
            dialog.show(parentFragmentManager, "ProfileLogoutDialog")
        }

        binding.profileSignOutTv.setOnClickListener { //회원탈퇴
            val dialog = DialogProfileWithdrawal()

            var bundle = Bundle()
            bundle.putInt("userId", userId)

            dialog.arguments = bundle
            dialog.show(parentFragmentManager, "ProfileWithdrawalDialog")
        }
    }

    private fun initRetrofitService() {
        profiledataService = ProfileDataService() // 서비스 객체 생성
        profiledataService.setProfileMyOngoingActivityView(this)
        profiledataService.profileMyOngoingActivitySender()
        profiledataService.setMyInfoView(this)
        profiledataService.profileMyInfoSender()
    }

    override fun onProfileMyOngoingActivitySuccess(result: ArrayList<ProfileMyOngoingActivityResult>?) {
        profileMyOngoingActivityList = result!!
        for (i in 0 until result.size) {
            recentActivityBind(result[i], i)
        }
    }

    override fun onProfileMyOngoingActivityFailure(message: String) {
        showToast(message)
    }

    private fun recentActivityBind(activity: ProfileMyOngoingActivityResult, index: Int) {
        when (index) {
            0 -> {
                binding.profileMyActivity1TypeTv.text = "배달파티"
                binding.profileMyActivity1Cv.visibility = View.VISIBLE
                binding.profileMyActivity1Iv.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profileMyActivity1TitleTv.text = activity.title
                val fomattingDate = activity.createdAt.substring(0,4)+"."+activity.createdAt.substring(5,7)+"."+activity.createdAt.substring(8,10)
                binding.profileMyActivity1DateTv.text = fomattingDate
            } 1 -> {
                binding.profileMyActivity2TypeTv.text = "배달파티"
                binding.profileMyActivity2Cv.visibility = View.VISIBLE
                binding.profileMyActivity2Iv.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profileMyActivity2TitleTv.text = activity.title
                val fomattingDate = activity.createdAt.substring(0,4)+"."+activity.createdAt.substring(5,7)+"."+activity.createdAt.substring(8,10)
                binding.profileMyActivity2DateTv.text = fomattingDate
            } 2 -> {
                binding.profileMyActivity3TypeTv.text = "배달파티"
                binding.profileMyActivity3Cv.visibility = View.VISIBLE
                binding.profileMyActivity3Iv.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profileMyActivity3TitleTv.text = activity.title
            val fomattingDate = activity.createdAt.substring(0,4)+"."+activity.createdAt.substring(5,7)+"."+activity.createdAt.substring(8,10)
                binding.profileMyActivity3DateTv.text = fomattingDate
            }
        }
    }

    private fun initRecentActivityClickListener() {
        binding.profileMyActivity1Cv.setOnClickListener {
            val activityId = profileMyOngoingActivityList[0].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            // transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment).commit()
            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment).commit()
        }

        binding.profileMyActivity2Cv.setOnClickListener {
            val activityId = profileMyOngoingActivityList[1].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
            transaction.commit()
        }

        binding.profileMyActivity3Cv.setOnClickListener {
            val activityId = profileMyOngoingActivityList[2].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            // transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
            transaction.replace(R.id.main_frm, lookPartyFragment)
            transaction.commit()
        }
    }

    override fun onProfileMyInfoSuccess(result: ProfileMyInfoResult) {
        userId = result.id
        nickName = result.nickname
        universityName = result.universityName
        dormitoryName = result.dormitoryName
        loginId = result.loginId
        emailAddress = result.emailAddress
        var phoneNumber = result.phoneNumber
        formattingPhoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country) //01012345678 => 010-1234-5678로 포맷팅
        val signUpDate = result.createdAt
        fomattingsignUpDate = signUpDate.substring(0,4)+"."+signUpDate.substring(5,7)+"."+signUpDate.substring(8,10)
        binding.profileCardUnivTv.text = result.universityName
        binding.profileCardDormitoryNameTv.text = result.dormitoryName
    }

    override fun onProfileMyInfoFailure(message: String) {
        Log.d("profile","나의 정보 조회 실패")
    }
}