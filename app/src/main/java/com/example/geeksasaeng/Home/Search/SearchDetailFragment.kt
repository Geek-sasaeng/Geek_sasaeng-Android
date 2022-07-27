package com.example.geeksasaeng.Home.Search

import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Search.Retrofit.SearchDataService
import com.example.geeksasaeng.Home.Search.Retrofit.SearchFilterView
import com.example.geeksasaeng.Home.Search.Retrofit.SearchResult
import com.example.geeksasaeng.Home.Search.Retrofit.SearchView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentSearchDetailBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchDetailFragment: BaseFragment<FragmentSearchDetailBinding>(FragmentSearchDetailBinding::inflate), SearchView, SearchFilterView {
    private var searchArray = ArrayList<DeliveryPartiesVoList?>()
    private lateinit var searchAdapter: DeliveryRVAdapter
    private lateinit var searchService: SearchDataService
    var isLoading = false
    var dormitoryId: Int = 1
    var totalCursor: Int = 0
    var orderTimeCategory: String? = null
    var maxMatching: Int? = null
    var nowTime: Long = 0
    var date: Date? = null
    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    var finalPage: Boolean? = false
    var filterCheckFlag: Boolean = false
    lateinit var keyword: String

    override fun initAfterBinding() {
        keyword = requireArguments().getString("keyword").toString()

        binding.searchProgressCover.visibility = View.GONE
        binding.searchBottomView.visibility = View.VISIBLE

        initSpinner() //필터(spinner) 작업
        initRadioBtn() //필터(radiobutton) 작업
        initAdapter()
        initTopScrollListener() // 상단 스크롤 작업

        searchService = SearchDataService()
        searchService.setSearchPartyView(this)

        if (totalCursor == 0)
            initLoadPosts()

        initScrollListener()
    }

    // 상단 스크롤 관련
    private fun initTopScrollListener() {
        binding.searchDetailSwipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { /* swipe 시 진행할 동작 */
            searchArray.clear()
            Log.d("SEARCH-FILTER", "initTopScrollListener 1")
            initLoadPosts()
            Log.d("SEARCH-FILTER", "initTopScrollListener 2")
            initAdapter()
            Log.d("SEARCH-FILTER", "initTopScrollListener 3")
            binding.searchDetailSwipe.isRefreshing = false
            Log.d("SEARCH-FILTER", "initTopScrollListener 4")
        })
    }

    private fun initRadioBtn(){
        binding.searchDetailTimeRg.setOnCheckedChangeListener { _: RadioGroup, checkedId:Int ->
            binding.searchDetailTimeRg.check(checkedId)
            when(checkedId){
                R.id.delivery_rb1 -> {
                    orderTimeCategory = "BREAKFAST"
                    filterCheckFlag = true
                    isLoading = false
                }
                R.id.delivery_rb2 -> {
                    orderTimeCategory = "LUNCH"
                    filterCheckFlag = true
                    isLoading = false
                }
                R.id.delivery_rb3 -> {
                    orderTimeCategory = "DINNER"
                    filterCheckFlag = true
                    isLoading = false
                }
                R.id.delivery_rb4 -> {
                    orderTimeCategory = "MIDNIGHT_SNACKS"
                    filterCheckFlag = true
                    isLoading = false
                }
                else -> {
                    filterCheckFlag = false
                    isLoading = false
                }
            }
        }
    }

    // 오늘 날짜 계산
    private fun calculateToday(): String {
        nowTime = System.currentTimeMillis()
        date = Date(nowTime)
        return dateFormat.format(date)
    }

    // 남은 시간 계산
    // TODO: 흠... 실시간으로 해야하는데 흠...
    private fun calculateTime(orderTime: String): String {
        var orderYear = Integer.parseInt(orderTime.substring(0, 4))
        var orderMonth = Integer.parseInt(orderTime.substring(5, 7))
        var orderDay = Integer.parseInt(orderTime.substring(8, 10))
        var orderHours = Integer.parseInt(orderTime.substring(11, 13))
        var orderMinutes = Integer.parseInt(orderTime.substring(14, 16))

        var currentTime = calculateToday()
        var todayYear = Integer.parseInt(currentTime.substring(0, 4))
        var todayMonth = Integer.parseInt(currentTime.substring(5, 7))
        var todayDay = Integer.parseInt(currentTime.substring(8, 10))
        var todayHours = Integer.parseInt(currentTime.substring(11, 13))
        var todayMinutes = Integer.parseInt(currentTime.substring(14, 16))

        var today = Calendar.getInstance().apply {
            set(Calendar.YEAR, todayYear)
            set(Calendar.MONTH, todayMonth)
            set(Calendar.DAY_OF_MONTH, todayDay)
        }.timeInMillis + (60000 * 60 * todayHours) + (60000 * todayMinutes)

        var order = Calendar.getInstance().apply {
            set(Calendar.YEAR, orderYear)
            set(Calendar.MONTH, orderMonth)
            set(Calendar.DAY_OF_MONTH, orderDay)
        }.timeInMillis + (60000 * 60 * orderHours) + (60000 * orderMinutes)

        var remainTime = order - today

        if (remainTime <= 0) {
            return "끝끝"
        }

        var day = remainTime / (24*60*60*1000)
        var sec = (remainTime % (24*60*60*1000)) / 1000
        var hour = sec / 3600
        var minute = (sec % 3600) / 60

        return if (day > 0)
            "${day}일 ${hour}시간 ${minute}분 남았어요"
        else if (hour > 0)
            "${hour}시간 ${minute}분 남았어요"
        else
            "${minute}분 남았어요"
    }

    // 리사이클러뷰에 최초로 넣어줄 데이터를 로드하는 경우
    private fun initLoadPosts() {
        totalCursor = 0
        if (filterCheckFlag) getSearchFilterList(dormitoryId, totalCursor, orderTimeCategory!!, maxMatching!!)
        else getSearchPartyList(dormitoryId, totalCursor, keyword)
    }

    // 리사이클러뷰에 더 보여줄 데이터를 로드하는 경우
    // TODO: 로딩 중에 스크롤 막기
    // TODO: 새로고침 했을 때 제일 밑으로 가게 만들기
    private fun initMoreLoadPosts() {
        binding.searchProgressCover.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            if (filterCheckFlag) getSearchFilterList(dormitoryId, totalCursor, orderTimeCategory!!, maxMatching!!)
            else getSearchPartyList(dormitoryId, totalCursor, keyword)
            isLoading = false
            binding.searchProgressCover.visibility = View.GONE
        }, 1200)
    }

    // 하단 스크롤 관련
    // TODO: 하단 스크롤 디자인 관련 수정 필요해보임! (지금은 오류 해결하려고 일단 디자인 이렇게 했어!)
    private fun initScrollListener() {
        binding.searchDetailPartyRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.searchDetailPartyRv.layoutManager

                if (finalPage == true) {
                    if ((layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= searchArray.size - 2)
                        binding.searchBottomView.visibility = View.INVISIBLE
                    else
                        binding.searchBottomView.visibility = View.VISIBLE
                }

                if (!isLoading) {
                    if (layoutManager != null && (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == searchArray.size - 1) {
                        if (finalPage == false)
                            initMoreLoadPosts()
                        else
                            binding.searchBottomView.visibility = View.INVISIBLE

                        isLoading = true
                    }
                }
            }
        })
    }

    // 배달 목록 가져오기
    private fun getSearchPartyList(dormitoryId: Int, cursor: Int, keyword: String) {
        val searchDataService = SearchDataService()
        searchDataService.getSearchPartyList(dormitoryId, cursor, keyword)
        searchDataService.setSearchPartyView(this)
        totalCursor += 1
    }

    override fun onSearchSuccess(result: SearchResult) {
        Log.d("SEARCH-RESPSONSE", "SUCCESS")

        finalPage = result.finalPage
        val result = result.searchPartiesVoList

        for (i in 0 until result!!.size) {
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hasHashTag

            searchArray.add(
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, calculateTime(orderTime!!), title, hashTags)
            )

            searchAdapter.notifyDataSetChanged()
        }
    }

    override fun onSearchFailure(code: Int, message: String) {
        Log.d("SEARCH-RESPONSE", "SEARCH-DETAIL-FRAGMENT-FAILURE")
        totalCursor--
    }

    // Adapter 설정
    private fun initAdapter() {
        Log.d("SEARCH-FILTER", "initAdapter")
        searchAdapter = DeliveryRVAdapter(searchArray)
        binding.searchDetailPartyRv.adapter = searchAdapter
        binding.searchDetailPartyRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        Log.d("SEARCH-FILTER", "initAdapter 2")

        searchAdapter.setOnItemClickListener(object : DeliveryRVAdapter.OnItemClickListener{
            override fun onItemClick(data: DeliveryPartiesVoList, pos : Int) {
                var deliveryItemId = searchAdapter.getDeliveryItemId(pos).toString()

                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("deliveryItemId", deliveryItemId)
                intent.putExtra("status", "search")
                startActivity(intent)
            }
        })
    }

    //스피너 관련 작업
    private fun initSpinner(){
        val items = resources.getStringArray(R.array.home_dropdown1) // spinner아이템 배열
        //어댑터
        val spinnerAdapter = PeopleSpinnerAdapter(requireContext(), items)
        binding.searchDetailPeopleSpinner.adapter = spinnerAdapter
        binding.searchDetailPeopleSpinner.setSelection(items.size - 1) //마지막아이템을 스피너 초기값으로 설정해준다.

        //이벤트 처리
        binding.searchDetailPeopleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //TODO:스피너
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE
                items[0] = items[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_2))

                if (position in 1..5)
                    filterCheckFlag = true

                maxMatching = position * 2
                finalPage = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }
    // 배달 목록 필터 적용 후 가져오기
    private fun getSearchFilterList(dormitoryId: Int, cursor: Int, orderTimeCategory: String?, maxMatching: Int?) {
        val searchDataService = SearchDataService()
        searchDataService.getFilterFilterList(dormitoryId, cursor, orderTimeCategory!!, maxMatching!!)
        searchDataService.setSearchFilterView(this)
        totalCursor += 1
    }

    override fun searchFilterSuccess(result: SearchResult) {
        Log.d("SEARCH-FILTER", "SUCCESS")

        finalPage = result.finalPage
        val result = result.searchPartiesVoList

        for (i in 0 until result!!.size) {
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hasHashTag

            searchArray.add(
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, calculateTime(orderTime!!), title, hashTags)
            )

            searchAdapter.notifyDataSetChanged()
        }
    }

    override fun searchFilterFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FRAGMENT-FAILURE")
        totalCursor--
    }
}