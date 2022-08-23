package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Adapter.MyOngoingActivityRVAdapter
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityView
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding
import java.util.*
import kotlin.collections.ArrayList

class ProfileMyActivityActivity: BaseActivity<ActivityProfileMyActivityBinding>(ActivityProfileMyActivityBinding::inflate), ProfileMyOngoingActivityView {

    private var myOngoingActivityList = ArrayList<ProfileMyOngoingActivityResult>()
    lateinit var myOngoingActivityAdapter: MyOngoingActivityRVAdapter
    lateinit var profileDataService: ProfileDataService

    override fun initAfterBinding() {
        initClickListener()
        initActivityListener()
        initAdapter()
    }

    private fun initClickListener() {
        binding.profileMyActivityBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        myOngoingActivityAdapter = MyOngoingActivityRVAdapter()
        binding.profileMyActivityActivityRv.adapter = myOngoingActivityAdapter
        binding.profileMyActivityActivityRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        myOngoingActivityAdapter.setOnItemClickListener(object : MyOngoingActivityRVAdapter.OnItemClickListener{
            override fun onItemClick(data: ProfileMyOngoingActivityResult, pos : Int) {
                var activityItemId = myOngoingActivityAdapter.getPartyId(pos)

                val intent = Intent(this@ProfileMyActivityActivity, MainActivity::class.java)
                intent.putExtra("status", "myActivity")
                intent.putExtra("deliveryItemId", activityItemId.toString())
                startActivity(intent)
            }
        })
    }

    private fun initActivityListener() {
        profileDataService = ProfileDataService()
        profileDataService.setProfileMyOngoingActivityView(this)
        profileDataService.profileMyOngoingActivitySender()
    }

    override fun onProfileMyOngoingActivitySuccess(result: ArrayList<ProfileMyOngoingActivityResult>?) {
        var preActivityDate = ""

        for (i in 0 until result!!.size) {
            if (preActivityDate != result[i].createdAt.substring(0, 10)) {
                if (i != 0)
                    myOngoingActivityList.add(ProfileMyOngoingActivityResult(null, null, "${preActivityDate.substring(0, 4)}.${preActivityDate.substring(5, 7)}.${preActivityDate.substring(8, 10)}", 2))
                preActivityDate = result[i].createdAt.substring(0, 10)
            }

            var item = ProfileMyOngoingActivityResult(result[i].id, result[i].title, result[i].createdAt, 1)
            myOngoingActivityList.add(item)

            if (i == result.size - 1)
                myOngoingActivityList.add(ProfileMyOngoingActivityResult(null, null, "${preActivityDate.substring(0, 4)}.${preActivityDate.substring(5, 7)}.${preActivityDate.substring(8, 10)}", 2))
        }

        myOngoingActivityList.reverse()
        myOngoingActivityAdapter.addAllItems(myOngoingActivityList)
        initOngoingActivityNumber(result.size)
    }

    override fun onProfileMyOngoingActivityFailure(message: String) {
        showToast(message)
    }

    private fun initOngoingActivityNumber(digitNumber: Int) {
        val str = SpannableString("현재 진행 중인 나의 활동은\n총 ${digitNumber}개 입니다.")
        val resultBuilder = SpannableStringBuilder(str)

        val begin = "현재 진행 중인 나의 홛동은\n총 ".length
        val end = begin + digitNumber.toString().length + 1

        resultBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#29ABE2")), begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.profileMyActivityNumberTv.text = resultBuilder
    }
}