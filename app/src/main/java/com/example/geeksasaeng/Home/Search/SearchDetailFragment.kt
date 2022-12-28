package com.example.geeksasaeng.Home.Search

import android.animation.Animator
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
import com.airbnb.lottie.LottieAnimationView
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Delivery.Timer.DeliveryTimer
import com.example.geeksasaeng.Home.Delivery.Timer.TimerData
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
import java.util.concurrent.CopyOnWriteArrayList

class SearchDetailFragment: BaseFragment<FragmentSearchDetailBinding>(FragmentSearchDetailBinding::inflate), SearchView, SearchFilterView {

    lateinit var loadingAnimationView: LottieAnimationView
    private var searchArray = ArrayList<DeliveryPartiesVoList?>()
    private lateinit var searchAdapter: DeliveryRVAdapter
    private lateinit var searchService: SearchDataService
    private lateinit var timerTask: DeliveryTimer
    var isLoading = false
    var dormitoryId: Int = 1
    var totalCursor: Int = 0
    var orderTimeCategory: String? = null
    var maxMatching: Int? = null
    var nowTime: Long = 0
    var date: Date? = null
    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var finalPage: Boolean? = false
    var filterCheckFlag: Boolean = false
    var filter1CheckFlag: Boolean = false //인원수
    var filter2CheckFlag: Boolean = false //카테고리
    lateinit var keyword: String
    private var lastCheckedBox = -1
    private var currentCheckedBox = -1

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        loadingStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun initAfterBinding() {
        keyword = requireArguments().getString("keyword").toString()

        searchService = SearchDataService()
        searchService.setSearchPartyView(this)
        binding.searchProgressCover.visibility = View.GONE
        loadingStart()

        binding.searchBottomView.visibility = View.VISIBLE // 하단 불투명 뷰

        initSpinner() // 필터(spinner) 작업
        initCheckBox() // 필터(checkBox) 작업
        initAdapter() // 어댑터 작업
        initTopScrollListener() // 상단 스크롤 작업

        if (totalCursor == 0)
            initLoadPosts()

        initScrollListener()
    }

    private fun checkDoubleCheck(i: Int){ // 이 함수는 더블체크로 아이템을 해제하는 경우 수행할 행동을 정해두었다.
        if (lastCheckedBox == i) { // 만약 제일 마지막으로 체크된 애를 다시 선택하는 경우라면
            lastCheckedBox = -1 // 초기화
            orderTimeCategory = null
            filter2CheckFlag = false
            refreshing()
        }
    }

    private fun initCheckBox() { //라디오 버튼
        binding.searchDetailCb1.setOnClickListener {
            checkDoubleCheck(R.id.search_detail_cb1)
        }

        binding.searchDetailCb2.setOnClickListener {
            checkDoubleCheck(R.id.search_detail_cb2)
        }

        binding.searchDetailCb3.setOnClickListener {
            checkDoubleCheck(R.id.search_detail_cb3)
        }

        binding.searchDetailCb4.setOnClickListener {
            checkDoubleCheck(R.id.search_detail_cb4)
        }

        binding.searchDetailCb1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.searchDetailCb2.isChecked = false
                binding.searchDetailCb3.isChecked = false
                binding.searchDetailCb4.isChecked = false
                orderTimeCategory = "BREAKFAST"
                filter2CheckFlag = true
                refreshing()
            } else { // 체크가 꺼지면
                lastCheckedBox = R.id.search_detail_cb1
            }
            Log.d("check",orderTimeCategory.toString())
        }

        binding.searchDetailCb2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.searchDetailCb1.isChecked = false
                binding.searchDetailCb3.isChecked = false
                binding.searchDetailCb4.isChecked = false
                orderTimeCategory = "LUNCH"
                filter2CheckFlag = true
                refreshing()
            } else { // 체크가 꺼지면
                lastCheckedBox = R.id.search_detail_cb2
            }
            Log.d("check",orderTimeCategory.toString())
        }

        binding.searchDetailCb3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.searchDetailCb1.isChecked = false
                binding.searchDetailCb2.isChecked = false
                binding.searchDetailCb4.isChecked = false
                orderTimeCategory = "DINNER"
                filter2CheckFlag = true
                refreshing()
            } else { // 체크가 꺼지면
                lastCheckedBox = R.id.search_detail_cb3
            }
            Log.d("check",orderTimeCategory.toString())
        }

        binding.searchDetailCb4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.searchDetailCb1.isChecked = false
                binding.searchDetailCb2.isChecked = false
                binding.searchDetailCb3.isChecked = false
                orderTimeCategory = "MIDNIGHT_SNACKS"
                filter2CheckFlag = true
                refreshing()
            } else { // 체크가 꺼지면
                lastCheckedBox = R.id.search_detail_cb4
            }
            Log.d("check",orderTimeCategory.toString())
        }
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
                if(position==0){ // 제일 상단 클릭(position==0)하면 초기화 해주기 위해
                    items[0]= items[items.size-1] // 인원선택(마지막값)을 현재선택값으로 넣어준다
                    maxMatching = 12 //maxMatching을 최댓값인 12로 설정해준다.
                } else {
                    items[0] = items[position] // items[0]에 선택한 아이템을 저장해준다. (*items[0]은 현재선택값 저장용)
                    maxMatching = position * 2
                }
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_2))

                filter1CheckFlag = position in 1..5 // 1~5사이 아이템을 선택하면 filterCheckFlag true. 아니면 false(false인 경우는 젤 상단 아이템 선택해서 스피너 선택해제하는 경우)
                finalPage = false
                refreshing()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    // 오늘 날짜 계산
    private fun calculateToday(): String {
        nowTime = System.currentTimeMillis()
        date = Date(nowTime)
        return dateFormat.format(date)
    }

    // 리사이클러뷰에 최초로 넣어줄 데이터를 로드하는 경우
    private fun initLoadPosts() {
        searchArray.clear() //리프레시나 최초로 넣어줄 애 로드니까 배열 비워주기
        totalCursor = 0
        isLoading = false
        finalPage = false
        keyword = requireArguments().getString("keyword").toString()
        filterCheckFlag = filter1CheckFlag||filter2CheckFlag
        if (filterCheckFlag) getSearchFilterList(dormitoryId, totalCursor, keyword, orderTimeCategory, maxMatching)
        else getSearchPartyList(dormitoryId, totalCursor, keyword)
    }

    // 리사이클러뷰에 더 보여줄 데이터를 로드하는 경우
    // TODO: 로딩 중에 스크롤 막기
    private fun initMoreLoadPosts() {
        binding.searchProgressCover.visibility = View.VISIBLE
        keyword = requireArguments().getString("keyword").toString()
        val handler = Handler()
        handler.postDelayed({
            filterCheckFlag = filter1CheckFlag||filter2CheckFlag
            if (filterCheckFlag) getSearchFilterList(dormitoryId, totalCursor, keyword, orderTimeCategory, maxMatching)
            else getSearchPartyList(dormitoryId, totalCursor, keyword)
            isLoading = false
            binding.searchProgressCover.visibility = View.GONE
            loadingStop()
        }, 1200)
    }

    // 상단 스크롤 관련
    private fun initTopScrollListener() {
        binding.searchDetailSwipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { /* swipe 시 진행할 동작 */
            initLoadPosts()
            initAdapter()
            binding.searchDetailSwipe.isRefreshing = false //TODO: 이건 무슨 코드일까..? 루나 보고있다면 정답을 알려줘~
        })
    }

    private fun refreshing(){ // 새로고침(initLoadPost랑 하는 일은 같은데,, 일단은 이 변수명 좀 썼으면 좋겠어여 refreshing이 직관적이고,, 코드의 변경가능성도 있어서욥!)
        initLoadPosts()
        initAdapter() //일단, 타이머바인딩때문에 넣어둠
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

                Log.d("why",isLoading.toString())
                if (!isLoading) {
                    if (layoutManager != null && (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == searchArray.size - 1) {
                        if (finalPage == false)
                            initMoreLoadPosts()
                        else binding.searchBottomView.visibility = View.INVISIBLE

                        isLoading = true
                    }
                }
            }
        })
    }

    // 배달 목록 가져오기
    private fun getSearchPartyList(dormitoryId: Int, cursor: Int, keyword: String) {
        val searchDataService = SearchDataService()
        searchDataService.setSearchPartyView(this)
        searchDataService.getSearchPartyList(dormitoryId, cursor, keyword)
        totalCursor += 1
    }

    override fun onSearchSuccess(result: SearchResult) {
        Log.d("SEARCH-RESPONSE", "SUCCESS")
        loadingStop()
        searchArray.clear()
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
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, orderTime!!, title, hashTags)
            )
        }
        searchAdapter.setArrayList(searchArray)
    }

    override fun onSearchFailure(code: Int, message: String) {
        Log.d("SEARCH-RESPONSE", "SEARCH-DETAIL-FRAGMENT-FAILURE")
        totalCursor--
    }

    // Adapter 설정
    private fun initAdapter() {
        Log.d("checkSearchArray", searchArray.toString())
        searchAdapter = DeliveryRVAdapter(searchArray)
        binding.searchDetailPartyRv.adapter = searchAdapter
        binding.searchDetailPartyRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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

    // 배달 목록 필터 적용 후 가져오기
    private fun getSearchFilterList(dormitoryId: Int, cursor: Int, keyword: String, orderTimeCategory: String?, maxMatching: Int?) {
        val searchDataService = SearchDataService()
        searchDataService.setSearchFilterView(this)
        searchDataService.getSearchFilterList(dormitoryId, cursor, keyword, orderTimeCategory, maxMatching)
        totalCursor += 1
    }

    override fun searchFilterSuccess(result: SearchResult) {
        Log.d("SEARCH-FILTER", "SUCCESS")
        loadingStop()

        finalPage = result.finalPage
        val result = result.searchPartiesVoList

        searchArray.clear()
        for (i in 0 until result!!.size) {
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hasHashTag

            searchArray.add(
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, orderTime!!, title, hashTags)
            )

            if (finalPage == true) {
                if ((binding.searchDetailPartyRv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= searchArray.size - 2)
                    binding.searchBottomView.visibility = View.INVISIBLE
                else binding.searchBottomView.visibility = View.VISIBLE
            }
        }

        searchAdapter.setArrayList(searchArray)
    }

    override fun searchFilterFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FRAGMENT-FAILURE")
        totalCursor--
    }

    private fun loadingStart() {
        loadingAnimationView = binding.animationView
        binding.animationViewLayout.visibility = View.VISIBLE
        loadingAnimationView.visibility = View.VISIBLE
        loadingAnimationView.playAnimation()
        loadingAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) { }
            override fun onAnimationEnd(animation: Animator?) { }
            override fun onAnimationCancel(p0: Animator?) { }
            override fun onAnimationRepeat(p0: Animator?) { }
        })
    }

    private fun loadingStop() {
        loadingAnimationView.cancelAnimation()
        binding.animationViewLayout.visibility = View.GONE
        loadingAnimationView.visibility = View.GONE
    }
}