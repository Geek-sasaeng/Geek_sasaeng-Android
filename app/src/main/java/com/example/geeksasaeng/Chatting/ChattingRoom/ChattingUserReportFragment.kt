package com.example.geeksasaeng.Chatting.ChattingRoom

import android.content.Intent
import android.util.Log
import com.example.geeksasaeng.Home.Party.ReportParty.PartyReportOptionActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentChattingUserReportBinding

class ChattingUserReportFragment: BaseFragment<FragmentChattingUserReportBinding>(FragmentChattingUserReportBinding::inflate) {

    private var reportedMemberId = 0

    override fun initAfterBinding() {
        reportedMemberId = arguments?.getString("memberId").toString().toInt()
        Log.d("CHATTING-SERVICE", "reportedMemberId = $reportedMemberId")
        initClickListener()
    }

    private fun initClickListener() {
        // 사용자 신고
        binding.reportChatUser1.setOnClickListener { changeActivity( binding.reportChatUser1Tv.text.toString(), 5) }
        binding.reportChatUser2.setOnClickListener { changeActivity( binding.reportChatUser2Tv.text.toString(), 6) }
        binding.reportChatUser3.setOnClickListener { changeActivity( binding.reportChatUser3Tv.text.toString(), 7) }
        binding.reportChatUser4.setOnClickListener { changeActivity( binding.reportChatUser4Tv.text.toString(), 8) }
        binding.reportChatUser5.setOnClickListener { changeActivity( binding.reportChatUser5Tv.text.toString(), 9) }
        binding.reportChatUser6.setOnClickListener { changeActivity( binding.reportChatUser6Tv.text.toString(), 10) }
    }

    private fun changeActivity(reportName: String, reportCategoryId: Int) {
        val intent = Intent(activity, PartyReportOptionActivity::class.java)
        intent.putExtra("reportName", reportName)
        intent.putExtra("reportCategoryId", reportCategoryId)
        intent.putExtra("reportedMemberId", reportedMemberId)
        startActivity(intent)
    }
}