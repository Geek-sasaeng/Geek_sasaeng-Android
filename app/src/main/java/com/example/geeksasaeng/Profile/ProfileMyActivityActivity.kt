package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Adapter.MyOngoingActivityRVAdapter
import com.example.geeksasaeng.Profile.Adapter.MyPreActivityRVAdapter
import com.example.geeksasaeng.Profile.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class ProfileMyActivityActivity: BaseActivity<ActivityProfileMyActivityBinding>(ActivityProfileMyActivityBinding::inflate), ProfileMyOngoingActivityView, ProfileMyPreActivityView {

    private var myOngoingActivityList = ArrayList<ProfileMyOngoingActivityResult>()
    lateinit var myOngoingActivityRVAdapter: MyOngoingActivityRVAdapter
    private var myPreActivityList = ArrayList<EndedDeliveryPartiesVoList>()
    lateinit var myPreActivityRVAdapter: MyPreActivityRVAdapter
    lateinit var profileDataService: ProfileDataService

    private var finalPage: Boolean? = null
    private var totalCursor = 0
    var isLoading = false

    override fun initAfterBinding() {
        binding.profileMyInfoProgressCover.visibility = View.GONE
        binding.profileMyActivityPreActivityTv.text = getNickname()+"님이 진행하셨던\n활동들을 한 눈에 확인해보세요"
        initClickListener()
        initActivityListener()
        initAdapter()
        initSpinner()

        if (totalCursor == 0)
            initLoadPosts()

        initScrollListener()
    }

    private fun initClickListener() {
        binding.profileMyActivityBackBtn.setOnClickListener {
            finish()
        }

        binding.profileMyActivityTab1.setOnClickListener {
            binding.profileMyActivityTab1.setBackgroundResource(R.drawable.profile_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab1.visibility = View.INVISIBLE
            binding.profileMyActivityTab2.setBackgroundResource(R.drawable.profile_not_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab2.visibility = View.VISIBLE
            binding.profileMyActivityTab3.setBackgroundResource(R.drawable.profile_not_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab3.visibility = View.VISIBLE
            binding.profileMyActivityPreActivityRv.visibility = View.VISIBLE
            binding.profileMyInfoPreparingLayout.visibility = View.GONE
        }

        binding.profileMyActivityTab2.setOnClickListener {
            binding.profileMyActivityTab1.setBackgroundResource(R.drawable.profile_not_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab1.visibility = View.VISIBLE
            binding.profileMyActivityTab2.setBackgroundResource(R.drawable.profile_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab2.visibility = View.INVISIBLE
            binding.profileMyActivityTab3.setBackgroundResource(R.drawable.profile_not_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab3.visibility = View.VISIBLE
            binding.profileMyActivityPreActivityRv.visibility = View.INVISIBLE
            binding.profileMyInfoPreparingLayout.visibility = View.VISIBLE
        }

        binding.profileMyActivityTab3.setOnClickListener {
            binding.profileMyActivityTab1.setBackgroundResource(R.drawable.profile_not_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab1.visibility = View.VISIBLE
            binding.profileMyActivityTab2.setBackgroundResource(R.drawable.profile_not_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab2.visibility = View.VISIBLE
            binding.profileMyActivityTab3.setBackgroundResource(R.drawable.profile_activity_tab)
            binding.profileMyActivityPreActivityWhiteTab3.visibility = View.INVISIBLE
            binding.profileMyActivityPreActivityRv.visibility = View.INVISIBLE
            binding.profileMyInfoPreparingLayout.visibility = View.VISIBLE
        }
    }

    private fun initLoadPosts() {
        totalCursor = 0
        isLoading = false
        finalPage = false
        getMyPreActivityAllList(totalCursor)
    }

    private fun getMyPreActivityAllList(cursor: Int) {
        profileDataService.profileMyPreActivitySender(cursor)
        totalCursor += 1
    }

    private fun initMoreLoadPosts() {
        binding.profileMyInfoProgressCover.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            getMyPreActivityAllList(totalCursor)
            isLoading = false
            binding.profileMyInfoProgressCover.visibility = View.GONE
        }, 1200)
    }

    private fun initScrollListener() {
        binding.profileMyActivityPreActivityRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.profileMyActivityPreActivityRv.layoutManager

                if (!isLoading) {
                    if (layoutManager != null && (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == myPreActivityList.size - 1) {
                        if (finalPage == false)
                            initMoreLoadPosts()

                        isLoading = true
                    }
                }
            }
        })
    }

    private fun initAdapter() {
        myOngoingActivityRVAdapter = MyOngoingActivityRVAdapter()
        binding.profileMyActivityActivityRv.adapter = myOngoingActivityRVAdapter
        binding.profileMyActivityActivityRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        myOngoingActivityRVAdapter.setOnItemClickListener(object : MyOngoingActivityRVAdapter.OnItemClickListener{
            override fun onItemClick(data: ProfileMyOngoingActivityResult, pos : Int) {
                var activityItemId = myOngoingActivityRVAdapter.getPartyId(pos)

                val intent = Intent(this@ProfileMyActivityActivity, MainActivity::class.java)
                intent.putExtra("status", "myActivity")
                intent.putExtra("deliveryItemId", activityItemId.toString())
                startActivity(intent)
            }
        })

        myPreActivityRVAdapter = MyPreActivityRVAdapter()
        binding.profileMyActivityPreActivityRv.adapter = myPreActivityRVAdapter
        binding.profileMyActivityPreActivityRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initSpinner() {
        val items = resources.getStringArray(R.array.profile_dropdown) // spinner아이템 배열

        //어댑터
        val spinnerAdapter = ProfileMyPreActivitySpinnerAdapter(this, items)
        binding.profileMyActivityPreActivitySpinner.adapter = spinnerAdapter
        binding.profileMyActivityPreActivitySpinner.setSelection(items.size - 1) //마지막아이템을 스피너 초기값으로 설정해준다.

        //이벤트 처리
        binding.profileMyActivityPreActivitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position==0) items[0]= items[items.size-1]
                else items[0] = items[position]

                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE

                val textName: TextView = view.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(this@ProfileMyActivityActivity, R.color.gray_2))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initActivityListener() {
        profileDataService = ProfileDataService()
        profileDataService.setProfileMyOngoingActivityView(this)
        profileDataService.profileMyOngoingActivitySender()
        profileDataService.setMyPreActivityView(this)
    }

    override fun onProfileMyOngoingActivitySuccess(result: ArrayList<ProfileMyOngoingActivityResult>?) {
        var preActivityDate = ""
        Collections.sort(result!!, DateComparator())

        for (i in 0 until result.size) {
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
        myOngoingActivityRVAdapter.addAllItems(myOngoingActivityList)
        initOngoingActivityNumber(result.size)
    }

    internal class DateComparator : Comparator<ProfileMyOngoingActivityResult?> {
        override fun compare(f1: ProfileMyOngoingActivityResult?, f2: ProfileMyOngoingActivityResult?): Int {
            if (f1!!.createdAt < f2!!.createdAt) {
                return 1
            } else if (f1.createdAt > f2.createdAt) {
                return -1
            }
            return 0
        }
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

    override fun onProfileMyPreActivityViewSuccess(result: ProfileMyPreActivityResult) {
        finalPage = result.finalPage
        var result = result.endedDeliveryPartiesVoList

        for (i in 0 until result.size) {
            val party = result[i]
            val item = EndedDeliveryPartiesVoList(party.foodCategory, party.id, party.maxMatching, party.title, party.updatedAt)
            myPreActivityList.add(item)
        }

        myPreActivityRVAdapter.addAllItems(myPreActivityList)
    }

    override fun onProfileMyPreActivityViewFailure(message: String) {
        showToast(message)
    }
}