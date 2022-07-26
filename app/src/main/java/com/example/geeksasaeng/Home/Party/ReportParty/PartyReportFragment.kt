package com.example.geeksasaeng.Home.Party.ReportParty

import android.content.Intent
import com.example.geeksasaeng.Home.Party.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyReportBinding

class PartyReportFragment: BaseFragment<FragmentDeliveryPartyReportBinding>(FragmentDeliveryPartyReportBinding::inflate) {

    var reportedDeliveryPartyId: Int = 0
    // var reportedMemberId: Int = 0

    override fun initAfterBinding() {
        initClickListener()
    }

    fun initClickListener() {
        // 뒤로가기
        binding.reportPartyBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, LookPartyFragment()).commit()
        }

        reportedDeliveryPartyId = requireArguments().getInt("reportedDeliveryPartyId")
        // reportedMemberId = requireArguments().getInt("reportedMemberId")

        // 파티 게시글 신고
        binding.reportPartyPost1.setOnClickListener { changeActivity(0, 1) }
        binding.reportPartyPost2.setOnClickListener { changeActivity(0, 2) }
        binding.reportPartyPost3.setOnClickListener { changeActivity(0, 3) }
        binding.reportPartyPost4.setOnClickListener { changeActivity(1, 4) }

        // 사용자 신고
        binding.reportPartyUser1.setOnClickListener { changeActivity(0, 5) }
        binding.reportPartyUser2.setOnClickListener { changeActivity(0, 6) }
        binding.reportPartyUser3.setOnClickListener { changeActivity(0, 7) }
        binding.reportPartyUser4.setOnClickListener { changeActivity(0, 8) }
        binding.reportPartyUser5.setOnClickListener { changeActivity(0, 9) }
        binding.reportPartyUser6.setOnClickListener { changeActivity(1, 10) }
    }

    private fun changeActivity(option: Int, reportCategoryId: Int) {
        when(option) {
            0 -> {
                val intent = Intent(activity, PartyReportOptionActivity::class.java)
                intent.putExtra("reportCategoryId", reportCategoryId)
                intent.putExtra("reportedDeliveryPartyId", reportedDeliveryPartyId)
                // intent.putExtra("reportedMemberId", reportedMemberId)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(activity, PartyReportNoOptionActivity::class.java)
                intent.putExtra("reportCategoryId", reportCategoryId)
                intent.putExtra("reportedDeliveryPartyId", reportedDeliveryPartyId)
                // intent.putExtra("reportedMemberId", reportedMemberId)
                startActivity(intent)
            }
        }
    }
}