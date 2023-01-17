package com.example.geeksasaeng.Profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileMemberInfoModifyResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileMemberInfoModifyView
import com.example.geeksasaeng.Utils.saveDormitory
import com.example.geeksasaeng.Utils.saveDormitoryId
import com.example.geeksasaeng.Utils.saveNickname
import com.example.geeksasaeng.Utils.saveProfileImgUrl
import com.example.geeksasaeng.databinding.DialogProfileUpdateBinding
import kotlin.properties.Delegates

class DialogProfileUpdate: DialogFragment(), ProfileMemberInfoModifyView {
    lateinit var binding: DialogProfileUpdateBinding
    lateinit var profiledataService: ProfileDataService

    private var dormitoryId :Int = 1
    private lateinit var loginId : String
    private lateinit var nickname : String
    private var profileImg : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileUpdateBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        // Dialog 사이즈 조절 하기
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        initView()
        initLClickListener()
        return binding.root
    }

    private fun initView() {
        profiledataService = ProfileDataService() // 서비스 객체 생성
        profiledataService.setProfileMemberInfoModifyView(this)
    }

    private fun initLClickListener() {
        binding.dialogProfileUpdateCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.dialogProfileUpdateOkBtn.setOnClickListener {
            //★ 나의 정보 수정 api 호출
            dormitoryId = arguments!!.getInt("dormitoryId")
            loginId = arguments!!.getString("loginId").toString()
            nickname  = arguments!!.getString("loginId").toString()
            profileImg = arguments?.getString("profileImg")
            //profiledataService.profileMemberInfoModifySender(null, dormitoryId, loginId, nickname, null, profileImg)

        }
    }

    override fun onProfileMemberInfoModifySuccess(result: ProfileMemberInfoModifyResult) {
        Log.d("PROFILE-MODIFY-RESPONSE", "성공")
        //정보 수정이 성공하면, 그 정보로 sharedPreference에 갱시시켜주는 작업
        //기숙사
        saveDormitoryId(result.dormitoryId)
        saveDormitory("제"+result.dormitoryName)
        //닉네임
        saveNickname(result.nickname)
        //프로필
        saveProfileImgUrl(result.profileImgUrl)
    }

    override fun onProfileMemberInfoModifyFailure(message: String) {
        Log.d("PROFILE-MODIFY-RESPONSE", "실패"+message)
    }
}