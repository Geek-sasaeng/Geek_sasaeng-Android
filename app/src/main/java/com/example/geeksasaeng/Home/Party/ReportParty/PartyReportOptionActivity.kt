package com.example.geeksasaeng.Home.Party.ReportParty

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyReportRequest
import com.example.geeksasaeng.Home.Party.Retrofit.PartyReportResponse
import com.example.geeksasaeng.Home.Party.Retrofit.PartyReportView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityPartyReportOptionBinding

class PartyReportOptionActivity: BaseActivity<ActivityPartyReportOptionBinding>(ActivityPartyReportOptionBinding::inflate), PartyReportView {

    var block: Boolean = false
    var reportCategoryId: Int = 0
    lateinit var reportContent: String
    var reportedDeliveryPartyId: Int = 0
    var reportedMemberId: Int = 0

    override fun initAfterBinding() {
        initClickListener()

        if (binding.reportOptionCb.isChecked)
            block = true

        reportCategoryId = intent.getIntExtra("reportCategoryId", 0)
        reportContent = binding.reportOptionEt.text.toString()
        reportedDeliveryPartyId = intent.getIntExtra("reportedDeliveryPartyId", 0)
        reportedMemberId = intent.getIntExtra("reportedMemberId", 0)

        binding.reportOptionTv.text = intent.getStringExtra("reportContent")
    }

    fun initClickListener() {
        binding.reportPartyOptionBackBtn.setOnClickListener {
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