package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Adapter
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Adapter.MyOngoingActivityRVAdapter
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding

class ProfileMyActivityActivity: BaseActivity<ActivityProfileMyActivityBinding>(ActivityProfileMyActivityBinding::inflate), ProfileMyOngoingActivityView {

    private var myOngoingActivityList = ArrayList<ProfileMyOngoingActivityResult>()
    lateinit var myOngoingActivityAdapter: MyOngoingActivityRVAdapter
    lateinit var profileDataService: ProfileDataService

    override fun initAfterBinding() {
        binding.profileMyActivityPreActivityTv.text = getNickname()+"님이 진행하셨던\n활동들을 한 눈에 확인해보세요"
        initClickListener()
        initAdapter()
        initActivityListener()
    }

    private fun initClickListener() {
        binding.profileMyActivityBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        myOngoingActivityAdapter = MyOngoingActivityRVAdapter(myOngoingActivityList)
        binding.profileMyActivityActivityRv.adapter = myOngoingActivityAdapter
        binding.profileMyActivityActivityRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        myOngoingActivityAdapter.setOnItemClickListener(object : MyOngoingActivityRVAdapter.OnItemClickListener{
            override fun onItemClick(data: ProfileMyOngoingActivityResult, pos : Int) {
                Log.d("PROFILE-RESPONSE", "ITEM-CLICK-CHECK")
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
        for (i in 0 until result!!.size) {
            // TODO: 여기에는 무슨 이미지를 넣는거지
            var item = ProfileMyOngoingActivityResult(result[i].id, "이미지 넣을 부분", result[i].title)
            Log.d("PROFILE-RESPONSE", item.toString())
            myOngoingActivityAdapter.addItem(item)
        }

        initOngoingActivityNumber(result!!.size)
    }

    override fun onProfileMyOngoingActivityFailure(message: String) {
        showToast(message)
    }

    private fun initOngoingActivityNumber(digitNumber: Int) {
        var str = SpannableString("현재 진행 중인 나의 활동은\n총 ${digitNumber}개 입니다.")
        var resultBuilder = SpannableStringBuilder(str)

        var begin = "현재 진행 중인 나의 홛동은\n총 ".length
        var end = begin + digitNumber.toString().length + 1

        resultBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#29ABE2")), begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.profileMyActivityNumberTv.text = resultBuilder
    }
}