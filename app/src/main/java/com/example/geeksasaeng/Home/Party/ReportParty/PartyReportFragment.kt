package com.example.geeksasaeng.Home.Party.ReportParty

import android.content.Intent
import com.example.geeksasaeng.Home.Party.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyReportBinding

class PartyReportFragment: BaseFragment<FragmentDeliveryPartyReportBinding>(FragmentDeliveryPartyReportBinding::inflate) {

    var reportContent: String = String()

    override fun initAfterBinding() {
        initClickListener()
    }

    fun initClickListener() {
        // 뒤로가기
        binding.reportPartyBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, LookPartyFragment()).commit()
        }

        // 파티 게시글 신고
        binding.reportPartyPost1.setOnClickListener { changeActivity(0, binding.reportPartyPost1Tv.text.toString()) }
        binding.reportPartyPost2.setOnClickListener { changeActivity(0, binding.reportPartyPost2Tv.text.toString()) }
        binding.reportPartyPost3.setOnClickListener { changeActivity(0, binding.reportPartyPost3Tv.text.toString()) }
        binding.reportPartyPost4.setOnClickListener { changeActivity(1, binding.reportPartyPost4Tv.text.toString()) }

        // 사용자 신고
        binding.reportPartyUser1.setOnClickListener { changeActivity(0, binding.reportPartyUser1Tv.text.toString()) }
        binding.reportPartyUser2.setOnClickListener { changeActivity(0, binding.reportPartyUser2Tv.text.toString()) }
        binding.reportPartyUser3.setOnClickListener { changeActivity(0, binding.reportPartyUser3Tv.text.toString()) }
        binding.reportPartyUser4.setOnClickListener { changeActivity(0, binding.reportPartyUser4Tv.text.toString()) }
        binding.reportPartyUser5.setOnClickListener { changeActivity(0, binding.reportPartyUser5Tv.text.toString()) }
        binding.reportPartyUser6.setOnClickListener { changeActivity(1, binding.reportPartyUser6Tv.text.toString()) }
    }

    private fun changeActivity(option: Int, reportContent: String) {
        when(option) {
            0 -> {
                val intent = Intent(activity, PartyReportOptionActivity::class.java)
                intent.putExtra("reportContent", reportContent)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(activity, PartyReportNoOptionActivity::class.java)
                intent.putExtra("reportContent", reportContent)
                startActivity(intent)
            }
        }
    }
}