package com.example.geeksasaeng.Home.Party.ReportParty

import android.content.Intent
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyReportBinding

class PartyReportActivity : BaseActivity<FragmentDeliveryPartyReportBinding>(FragmentDeliveryPartyReportBinding::inflate) {
    var reportedDeliveryPartyId: Int = 0
    var reportedMemberId: Int = 0

    override fun initAfterBinding() {
        initClickListener()
    }

    fun initClickListener() {
        // 뒤로가기
        binding.reportPartyBackBtn.setOnClickListener {
            finish()
        }

        reportedDeliveryPartyId = intent.getIntExtra("reportedDeliveryPartyId", 0)
        reportedMemberId = intent.getIntExtra("reportedMemberId", 0)

        // 파티 게시글 신고
        binding.reportPartyPost1.setOnClickListener { changeActivity( binding.reportPartyPost1Tv.text.toString(), 1) }
        binding.reportPartyPost2.setOnClickListener { changeActivity( binding.reportPartyPost2Tv.text.toString(), 2) }
        binding.reportPartyPost3.setOnClickListener { changeActivity( binding.reportPartyPost3Tv.text.toString(), 3) }
        binding.reportPartyPost4.setOnClickListener { changeActivity( binding.reportPartyPost4Tv.text.toString(), 4) }

        // 사용자 신고
        binding.reportPartyUser1.setOnClickListener { changeActivity( binding.reportPartyUser1Tv.text.toString(), 5) }
        binding.reportPartyUser2.setOnClickListener { changeActivity( binding.reportPartyUser2Tv.text.toString(), 6) }
        binding.reportPartyUser3.setOnClickListener { changeActivity( binding.reportPartyUser3Tv.text.toString(), 7) }
        binding.reportPartyUser4.setOnClickListener { changeActivity( binding.reportPartyUser4Tv.text.toString(), 8) }
        binding.reportPartyUser5.setOnClickListener { changeActivity( binding.reportPartyUser5Tv.text.toString(), 9) }
        binding.reportPartyUser6.setOnClickListener { changeActivity( binding.reportPartyUser6Tv.text.toString(), 10) }
    }

    private fun changeActivity(reportName: String, reportCategoryId: Int) {
        val intent = Intent(this, PartyReportOptionActivity::class.java)
        intent.putExtra("reportName", reportName)
        intent.putExtra("reportCategoryId", reportCategoryId)
        intent.putExtra("reportedDeliveryPartyId", reportedDeliveryPartyId)
        intent.putExtra("reportedMemberId", reportedMemberId)
        startActivity(intent)
    }
}