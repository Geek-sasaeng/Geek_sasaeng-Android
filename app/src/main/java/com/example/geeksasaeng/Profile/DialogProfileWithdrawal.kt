package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileWithdrawalView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.DialogWithdralBinding

class DialogProfileWithdrawal: DialogFragment(), ProfileWithdrawalView {

    lateinit var binding: DialogWithdralBinding
    lateinit var profileWithdrawalService: ProfileDataService
    private var userId: Int = 0
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogWithdralBinding.inflate(inflater, container, false)
        userId = requireArguments().getInt("userId")
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        initService()

        binding.withdrawalOkBtn.setOnClickListener {
            profileWithdrawalService.profileWithdrawalSender()
        }

        binding.withdrawalCancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    private fun initService() {
        profileWithdrawalService = ProfileDataService()
        profileWithdrawalService.setProfileWithdrawalView(this)
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_width)
        val height = resources.getDimensionPixelSize(R.dimen.chatting_exit_popup_height)
        dialog?.window?.setLayout(width,height)
    }

    override fun onProfileWithdrawalSuccess() {
        (context as MainActivity).finish()
        removeAutoLogin()
        CustomToastMsg.createToast(requireContext(), "탈퇴가 완료되었습니다", "#8029ABE2", 53)?.show()
        dismiss()
        startActivity(Intent(activity, LoginActivity::class.java))
    }

    override fun onProfileWithdrawalFailure(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}