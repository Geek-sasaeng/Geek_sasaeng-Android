package com.example.geeksasaeng.Home.Party.ReportParty

import android.os.Bundle
import com.example.geeksasaeng.Home.Party.Retrofit.*
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityPartyReportNoOptionBinding

class PartyReportNoOptionActivity: BaseActivity<ActivityPartyReportNoOptionBinding>(ActivityPartyReportNoOptionBinding::inflate), PartyReportView, UserReportView {

    var block: Boolean = false
    var reportCategoryId: Int = 0
    lateinit var reportContent: String
    var reportedDeliveryPartyId: Int = 0
    var reportedMemberId: Int = 0

    override fun initAfterBinding() {
        initClickListener()

        binding.reportNoOptionTv.text = intent.getStringExtra("reportName")

        reportCategoryId = intent.getIntExtra("reportCategoryId", 0)
        reportContent = binding.reportNoOptionEt.text.toString()
        reportedDeliveryPartyId = intent.getIntExtra("reportedDeliveryPartyId", 0)
        reportedMemberId = intent.getIntExtra("reportedMemberId", 0)
    }

    fun initClickListener() {
        binding.reportPartyNoOptionBackBtn.setOnClickListener {
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

    //파티 신고 성공
    override fun partyReportViewSuccess(code: Int) {
        val dialog = DialogReportParty()
        val bundle = Bundle()
        bundle.putString("msg", "고객님께서 요청하신 사항에\n따른 신고가 정상적으로\n처리되었습니다.")
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    //파티 신고 실패
    override fun partyReportViewFailure(message: String) {
        val dialog = DialogReportParty()
        val bundle = Bundle()
        bundle.putString("msg", message)
        dialog.arguments = bundle
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

    //사용자 신고 성공
    override fun userReportViewSuccess(code: Int) {

    }

    //사용자 신고 실패
    override fun userReportViewFailure(message: String) {

    }
}