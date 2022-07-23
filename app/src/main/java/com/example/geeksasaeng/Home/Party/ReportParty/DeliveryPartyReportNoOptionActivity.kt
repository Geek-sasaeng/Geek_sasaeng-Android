package com.example.geeksasaeng.Home.Party.ReportParty

import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityDeliveryPartyReportNoOptionBinding

class DeliveryPartyReportNoOptionActivity: BaseActivity<ActivityDeliveryPartyReportNoOptionBinding>(ActivityDeliveryPartyReportNoOptionBinding::inflate) {
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