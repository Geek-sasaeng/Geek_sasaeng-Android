package com.example.geeksasaeng.Profile

import android.net.Uri
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyInfoResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyInfoView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentProfileMyInfoBinding

class ProfileMyInfoFragment: BaseFragment<FragmentProfileMyInfoBinding>(FragmentProfileMyInfoBinding::inflate), ProfileMyInfoView {

    lateinit var profileMyInfoService: ProfileDataService

    override fun initAfterBinding() {
        initClickListener()
        initProfileDataService()
    }

    private fun initClickListener() {
        binding.profileMyInfoBackBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        binding.profilePencilIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("profile_my_info_update").replace(R.id.main_frm, ProfileMyInfoUpdateFragment()).commit()
        }

        binding.profileUserImgCv.setOnClickListener {
            // TODO: 프로필 사진 수정 부분 앨범과 연결해 넣어주기
        }

        binding.profileMyInfoLogoutCv.setOnClickListener {
            val dialog = DialogProfileLogout()
            dialog.show(parentFragmentManager, "ProfileLogoutDialog")
        }
        
        binding.profileMyInfoWithdrawalTv.setOnClickListener {
            val dialog = DialogProfileWithdrawal()
            dialog.show(parentFragmentManager, "ProfileWithdrawalDialog")
        }
    }

    private fun initProfileDataService() {
        profileMyInfoService = ProfileDataService()
        profileMyInfoService.setMyInfoView(this)
        profileMyInfoService.profileMyInfoSender()
    }

    override fun onProfileMyInfoSuccess(result: ProfileMyInfoResult) {
        binding.profileMyInfoUserImgIv.setImageURI(Uri.parse(result.profileImgUrl))
        binding.profileMyInfoDormitoryTv.text = result.dormitoryName
        binding.profileMyInfoNicknameTv.text = result.nickname
        binding.profileMyInfoIdTv.text = result.loginId
        binding.profileMyInfoEmailTv.text = result.emailAddress
        binding.profileMyInfoPhoneNumberTv.text = result.phoneNumber
    }

    override fun onProfileMyInfoFailure(message: String) {
        showToast(message)
    }
}