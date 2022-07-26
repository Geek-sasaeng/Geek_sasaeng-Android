package com.example.geeksasaeng.Home.Party.ReportParty

import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityPartyReportNoOptionBinding

class PartyReportNoOptionActivity: BaseActivity<ActivityPartyReportNoOptionBinding>(ActivityPartyReportNoOptionBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()

        binding.reportNoOptionTv.text = intent.getStringExtra("reportContent")
    }

    fun initClickListener() {
        binding.reportPartyNoOptionBackBtn.setOnClickListener {
            finish()
        }
    }
}