package com.example.geeksasaeng.Home.Party.ReportParty

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.example.geeksasaeng.Home.Party.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpPhoneSkip
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityPartyReportOptionBinding

class PartyReportOptionActivity: BaseActivity<ActivityPartyReportOptionBinding>(ActivityPartyReportOptionBinding::inflate), PartyReportView, UserReportView {

    var block: Boolean = false
    var reportCategoryId: Int = 0
    lateinit var reportContent: String
    var reportedDeliveryPartyId: Int = 0
    var reportedMemberId: Int = 0

    override fun initAfterBinding() {
        initClickListener()

        if (binding.reportOptionCb.isChecked)
            block = true

        binding.reportOptionTv.text = intent.getStringExtra("reportName") + " (선택 옵션)"

        reportCategoryId = intent.getIntExtra("reportCategoryId", 0)
        reportContent = binding.reportOptionEt.text.toString()
        reportedDeliveryPartyId = intent.getIntExtra("reportedDeliveryPartyId", 0)
        reportedMemberId = intent.getIntExtra("reportedMemberId", 0)
    }

    fun initClickListener() {
        binding.reportPartyOptionBackBtn.setOnClickListener {
            finish()
        }

        binding.reportReportBtn.setOnClickListener {
            if (reportCategoryId <= 4)
                sendReportParty()
            else
                sendReportUser()
        }
    }

    private fun getReportParty(): PartyReportRequest {
        return PartyReportRequest(block, reportCategoryId, reportContent, reportedDeliveryPartyId, reportedMemberId)
    }

    private fun sendReportParty() {
        val reportPartyService = PartyDataService()
        reportPartyService.setPartyReportView(this)
        reportPartyService.partyReportSender(getReportParty())
    }

    override fun partyReportViewSuccess(code: Int) {

        showToast("신고 완료")
        val dialog = DialogReportPartySuccess()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    override fun partyReportViewFailure(message: String) {
        showToast(message)
        val dialog = DialogReportPartyFail()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    private fun getReportUser(): UserReportRequest {
        return UserReportRequest(block, reportCategoryId, reportContent, reportedMemberId)
    }

    private fun sendReportUser() {
        val reportUserService = PartyDataService()
        reportUserService.setUserReportView(this)
        reportUserService.userReportSender(getReportUser())
    }

    override fun userReportViewSuccess(code: Int) {
        showToast("신고 완료")
        val dialog = DialogReportPartySuccess()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    override fun userReportViewFailure(message: String) {
        showToast(message)
        val dialog = DialogReportPartyFail()
        dialog.show(supportFragmentManager, "CustomDialog")
    }
}