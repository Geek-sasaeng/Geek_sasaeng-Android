package com.example.geeksasaeng.Home.Party.ReportParty

import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyReportRequest
import com.example.geeksasaeng.Home.Party.Retrofit.PartyReportView
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityPartyReportNoOptionBinding

class PartyReportNoOptionActivity: BaseActivity<ActivityPartyReportNoOptionBinding>(ActivityPartyReportNoOptionBinding::inflate), PartyReportView {

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
    }

    override fun partyReportViewFailure(message: String) {
        showToast(message)
    }
}

//                게시물 신고
//                {
//                    "block": true,
//                    "reportCategoryId": 9,
//                    "reportContent": "욕설로 신고합니다.",
//                    "reportedDeliveryPartyId": 231,
//                    "reportedMemberId": 34
//                }
//                사용자 신고
//                {
//                    "block": true,
//                    "reportCategoryId": 9,
//                    "reportContent": "욕설로 신고합니다.",
//                    "reportedMemberId": 34
//                }