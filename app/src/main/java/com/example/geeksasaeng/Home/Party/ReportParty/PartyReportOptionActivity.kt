package com.example.geeksasaeng.Home.Party.ReportParty

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityPartyReportOptionBinding

class PartyReportOptionActivity: BaseActivity<ActivityPartyReportOptionBinding>(ActivityPartyReportOptionBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()

        binding.reportOptionTv.text = intent.getStringExtra("reportContent")
    }

    fun initClickListener() {
        binding.reportPartyOptionBackBtn.setOnClickListener {
            finish()
        }
    }
}