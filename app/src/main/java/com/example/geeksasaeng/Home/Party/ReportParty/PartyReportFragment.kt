package com.example.geeksasaeng.Home.Party.ReportParty

import android.content.Intent
import com.example.geeksasaeng.Home.Party.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyReportBinding

class PartyReportFragment: BaseFragment<FragmentDeliveryPartyReportBinding>(FragmentDeliveryPartyReportBinding::inflate) {

    var reportedDeliveryPartyId: Int = 0
    var reportedMemberId: Int = 0

    override fun initAfterBinding() {
        initClickListener()
    }

    fun initClickListener() {
        // 뒤로가기
        binding.reportPartyBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, LookPartyFragment()).commit()
        }

        reportedDeliveryPartyId = requireArguments().getInt("reportedDeliveryPartyId")
        reportedMemberId = requireArguments().getInt("reportedMemberId")

        // 파티 게시글 신고
        binding.reportPartyPost1.setOnClickListener { changeActivity(0, binding.reportPartyPost1Tv.text.toString(), 1) }
        binding.reportPartyPost2.setOnClickListener { changeActivity(0, binding.reportPartyPost2Tv.text.toString(), 2) }
        binding.reportPartyPost3.setOnClickListener { changeActivity(0, binding.reportPartyPost3Tv.text.toString(), 3) }
        binding.reportPartyPost4.setOnClickListener { changeActivity(1, binding.reportPartyPost4Tv.text.toString(), 4) }

        // 사용자 신고
        binding.reportPartyUser1.setOnClickListener { changeActivity(0, binding.reportPartyUser1Tv.text.toString(), 5) }
        binding.reportPartyUser2.setOnClickListener { changeActivity(0, binding.reportPartyUser2Tv.text.toString(), 6) }
        binding.reportPartyUser3.setOnClickListener { changeActivity(0, binding.reportPartyUser3Tv.text.toString(), 7) }
        binding.reportPartyUser4.setOnClickListener { changeActivity(0, binding.reportPartyUser4Tv.text.toString(), 8) }
        binding.reportPartyUser5.setOnClickListener { changeActivity(0, binding.reportPartyUser5Tv.text.toString(), 9) }
        binding.reportPartyUser6.setOnClickListener { changeActivity(1, binding.reportPartyUser6Tv.text.toString(), 10) }
    }

    private fun changeActivity(option: Int, reportName: String, reportCategoryId: Int) {
        when(option) {
            0 -> {
                val intent = Intent(activity, PartyReportOptionActivity::class.java)
                intent.putExtra("reportName", reportName)
                intent.putExtra("reportCategoryId", reportCategoryId)
                intent.putExtra("reportedDeliveryPartyId", reportedDeliveryPartyId)
                intent.putExtra("reportedMemberId", reportedMemberId)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(activity, PartyReportNoOptionActivity::class.java)
                intent.putExtra("reportName", reportName)
                intent.putExtra("reportCategoryId", reportCategoryId)
                intent.putExtra("reportedDeliveryPartyId", reportedDeliveryPartyId)
                intent.putExtra("reportedMemberId", reportedMemberId)
                startActivity(intent)
            }
        }
    }
}