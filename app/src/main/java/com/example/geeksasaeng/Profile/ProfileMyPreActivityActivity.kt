package com.example.geeksasaeng.Profile


import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Adapter.MyPreActivityRVAdapter
import com.example.geeksasaeng.Profile.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding
import kotlin.collections.ArrayList


class ProfileMyPreActivityActivity: BaseActivity<ActivityProfileMyActivityBinding>(ActivityProfileMyActivityBinding::inflate), ProfileMyPreActivityView {

    private var myPreActivityList = ArrayList<EndedDeliveryPartiesVoList>()
    lateinit var myPreActivityRVAdapter: MyPreActivityRVAdapter
    lateinit var profileDataService: ProfileDataService

    private var finalPage: Boolean? = null
    private var totalCursor = 0
    var isLoading = false

    override fun initAfterBinding() {
        binding.profileMyInfoProgressCover.visibility = View.GONE
        initClickListener()
        initActivityListener()
        initAdapter()

        if (totalCursor == 0)
            initLoadPosts()

        initScrollListener()
    }

    private fun initClickListener() {
        
        binding.profileMyActivityRg.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.profile_my_activity_rb1 -> { //배달 파티
                    binding.profileMyActivityRv.visibility = View.VISIBLE
                    binding.profileMyInfoPreparingLayout.visibility = View.GONE
                }
                R.id.profile_my_activity_rb2 -> { //심부름
                    binding.profileMyActivityRv.visibility = View.INVISIBLE
                    binding.profileMyInfoPreparingLayout.visibility = View.VISIBLE
                }
                R.id.profile_my_activity_rb3 -> { //거래
                    binding.profileMyActivityRv.visibility = View.INVISIBLE
                    binding.profileMyInfoPreparingLayout.visibility = View.VISIBLE
                    //TODO: 나중에는 이렇게 할게 아니라 각각에 맞게 rv에 데이터를 넣어줘야함.
                }
                else-> {}
            }
        }

        binding.profileMyActivityBackBtn.setOnClickListener {
            finish()
        }

    }

    private fun initLoadPosts() {
        totalCursor = 0
        isLoading = false
        finalPage = false
        getMyPreActivityAllList(totalCursor)
    }

    private fun getMyPreActivityAllList(cursor: Int) {
        profileDataService.profileMyPreActivitySender(cursor) //★ api호출
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
        binding.profileMyActivityRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.profileMyActivityRv.layoutManager

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
        myPreActivityRVAdapter = MyPreActivityRVAdapter()
        binding.profileMyActivityRv.adapter = myPreActivityRVAdapter
        binding.profileMyActivityRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myPreActivityRVAdapter.setOnItemClickListener(object :
            MyPreActivityRVAdapter.OnItemClickListener { // 리사이클러 뷰 : 내가 진행한 활동 아이템 클릭하면, 해당 글로 화면 전환
            override fun onItemClick(data: EndedDeliveryPartiesVoList, pos: Int) {

                var activityItemId = data.id

                //액티비티에서 => 나의 해당 글의 lookParty로 이동하는 법 ☆
                val intent = Intent(this@ProfileMyPreActivityActivity, MainActivity::class.java)
                intent.putExtra("status", "myActivity")
                intent.putExtra("deliveryItemId", activityItemId.toString())
                startActivity(intent)
            }
        })
    }

    private fun initActivityListener() {
        profileDataService = ProfileDataService()
        profileDataService.setMyPreActivityView(this)
    }


    //나의 활동 불러오기 성공
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

    //나의 활동 불러오기 실패
    override fun onProfileMyPreActivityViewFailure(message: String) {
        showToast(message)
    }
}