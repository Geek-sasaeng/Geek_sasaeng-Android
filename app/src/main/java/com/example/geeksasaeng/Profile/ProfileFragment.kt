package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.CustomerService.ProfileCustomerServiceActivity
import com.example.geeksasaeng.Profile.MyInfoUpdate.ProfileMyInfoUpdateActivity
import com.example.geeksasaeng.Profile.ProfileCard.DialogProfileDetail
import com.example.geeksasaeng.Profile.ProfileCard.ProfileLevelIntroductionActivity
import com.example.geeksasaeng.Profile.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.Utils.getProfileImgUrl
import com.example.geeksasaeng.databinding.FragmentProfileBinding
import java.util.*

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate)
    , ProfileMyPreActivityView, ProfileMyInfoView {

    lateinit var profiledataService: ProfileDataService
    //private var profileMyOngoingActivityList = ArrayList<ProfileMyOngoingActivityResult>()
    private var myPreActivityList = java.util.ArrayList<EndedDeliveryPartiesVoList>()

    // 나의정보 상세보기 다이얼로그에 넘겨줄 정보들
    private var nickName = String()
    private var universityName = String()
    private var dormitoryName = String()
    private var loginId = String()
    private var emailAddress = String()
    private var formattingPhoneNumber = String()
    private var fomattingSignUpDate = String()
    private var userId: Int = 0

    override fun initAfterBinding() {
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

        binding.profileCardLayoutBlueTop.setOnClickListener { //프로필 카드 상단 파란영역 클릭시 레벨 설명
            val intent = Intent(activity, ProfileLevelIntroductionActivity::class.java)
            startActivity(intent)
        }

        binding.profileCardLayoutWhile.setOnClickListener {
            val profileDetailDialog = DialogProfileDetail()
            val bundle = Bundle()
            bundle.putString("nickName", nickName)
            bundle.putString("universityName", universityName)
            bundle.putString("dormitoryName", dormitoryName)
            bundle.putString("loginId", loginId)
            bundle.putString("emailAddress", emailAddress)
            bundle.putString("phoneNumber", formattingPhoneNumber)
            bundle.putString("signUpDate", fomattingSignUpDate)
            profileDetailDialog.arguments = bundle
            profileDetailDialog.show(parentFragmentManager, "profileDetailDialog")
        }

        binding.profileMyActivity.setOnClickListener { //나의 활동 보기
            startActivity(Intent(activity, ProfileMyPreActivityActivity::class.java))
        }

        binding.profileMyInfo.setOnClickListener { //나의 정보 수정
            val intent = Intent(activity, ProfileMyInfoUpdateActivity::class.java)
            intent.putExtra("loginId", loginId)
            startActivity(intent)
        }

        binding.profileCustomerService.setOnClickListener { //고객 센터
            val intent = Intent(activity, ProfileCustomerServiceActivity::class.java)
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
        profiledataService.setMyPreActivityView(this) //★
        profiledataService.profileMyPreActivitySender(0)//cursor =0부터 시작!
        profiledataService.setMyInfoView(this) //★
        profiledataService.profileMyInfoSender()
    }

    private fun recentActivityBind(activity: EndedDeliveryPartiesVoList, index: Int) {
        when (index) {
            0 -> {
                binding.profileMyActivity1TypeTv.text = "배달파티"
                binding.profileMyActivity1Cv.visibility = View.VISIBLE
                binding.profileMyActivity1Iv.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profileMyActivity1TitleTv.text = activity.title
                val fomattingDate = activity.updatedAt.substring(0,4)+"."+activity.updatedAt.substring(5,7)+"."+activity.updatedAt.substring(8,10)
                binding.profileMyActivity1DateTv.text = fomattingDate
            } 1 -> {
                binding.profileMyActivity2TypeTv.text = "배달파티"
                binding.profileMyActivity2Cv.visibility = View.VISIBLE
                binding.profileMyActivity2Iv.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profileMyActivity2TitleTv.text = activity.title
                val fomattingDate = activity.updatedAt.substring(0,4)+"."+activity.updatedAt.substring(5,7)+"."+activity.updatedAt.substring(8,10)
                binding.profileMyActivity2DateTv.text = fomattingDate
            } 2 -> {
                binding.profileMyActivity3TypeTv.text = "배달파티"
                binding.profileMyActivity3Cv.visibility = View.VISIBLE
                binding.profileMyActivity3Iv.setImageResource(R.drawable.ic_delivery_party_ic)
                binding.profileMyActivity3TitleTv.text = activity.title
            val fomattingDate = activity.updatedAt.substring(0,4)+"."+activity.updatedAt.substring(5,7)+"."+activity.updatedAt.substring(8,10)
                binding.profileMyActivity3DateTv.text = fomattingDate
            }
        }
    }

    private fun initRecentActivityClickListener() {
        binding.profileMyActivity1Cv.setOnClickListener {
            val activityId = myPreActivityList[0].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            // transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment).commit()
            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment).commit()
        }

        binding.profileMyActivity2Cv.setOnClickListener {
            val activityId = myPreActivityList[1].id

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", activityId.toString())

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
            transaction.commit()
        }

        binding.profileMyActivity3Cv.setOnClickListener {
            val activityId = myPreActivityList[2].id

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
        Log.d("profile","나의 정보 조회 성공")
        userId = result.id
        nickName = result.nickname
        universityName = result.universityName
        dormitoryName = result.dormitoryName
        loginId = result.loginId
        emailAddress = result.emailAddress
        var phoneNumber = result.phoneNumber
        formattingPhoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country) //01012345678 => 010-1234-5678로 포맷팅
        val signUpDate = result.createdAt
        fomattingSignUpDate = signUpDate.substring(0,4)+"."+signUpDate.substring(5,7)+"."+signUpDate.substring(8,10)

        binding.profileSignOutTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //정보 설정
        binding.profileCardNickNameTv.text = getNickname()
        binding.profileCardUnivTv.text = universityName
        binding.profileCardDormitoryNameTv.text = dormitoryName
        Glide.with(requireContext())
            .load(getProfileImgUrl())
            .into(binding.profileCardImgIv)
        //등급
        binding.profileCardLayoutBlueTopGrade.text = result.grade
        if(result.grade == "복학생"){
            binding.profileCardBarIv.setImageResource(R.drawable.ic_profile_card_heart_bar_level2)
        }else if(result.grade == "졸업생"){
            binding.profileCardBarIv.setImageResource(R.drawable.ic_profile_card_heart_bar_level3)
        }
        //승급까지 남은 학점
        binding.profileCardLayoutBlueTopLeftCredit.text = result.nextGradeAndRemainCredits
    }

    override fun onProfileMyInfoFailure(message: String) {
        Log.d("profile","나의 정보 조회 실패")
    }

    override fun onProfileMyPreActivityViewSuccess(result: ProfileMyPreActivityResult) {
        Log.d("profile", "진행한 활동 불러오기 성공")

        var result = result.endedDeliveryPartiesVoList

        for (i in 0 until result.size) {
            val party = result[i]
            val item = EndedDeliveryPartiesVoList(party.foodCategory, party.id, party.maxMatching, party.title, party.updatedAt)
            myPreActivityList.add(item)
        }

        if (myPreActivityList.size == 0){ //진행했던 활동이 하나도 없으면
            binding.profileMyActivity.visibility = View.GONE
            binding.profileMyActivityNoInfoLayout.visibility = View.VISIBLE
        }else{
            for (i in 0 until myPreActivityList.size) {
                recentActivityBind(myPreActivityList[i], i)
                Log.d("profile", i.toString())
            }
        }

    }

    override fun onProfileMyPreActivityViewFailure(message: String) {
        showToast(message)
    }


}