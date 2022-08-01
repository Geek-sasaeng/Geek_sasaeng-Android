package com.example.geeksasaeng.Home.Delivery

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Home.CreateParty.CreatePartyActivity
import com.example.geeksasaeng.Home.Delivery.Adapter.BannerVPAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryBannerView
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryFilterView
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryService
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryView
import com.example.geeksasaeng.Home.Party.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Naver.StepNaverOneFragment
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding
import java.text.SimpleDateFormat
import java.util.*

class DeliveryFragment: BaseFragment<FragmentDeliveryBinding>(FragmentDeliveryBinding::inflate), DeliveryView, DeliveryBannerView, DeliveryFilterView {
    private var deliveryArray = ArrayList<DeliveryPartiesVoList?>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter
    private lateinit var deliveryService: DeliveryService //서비스 객체
    private lateinit var deliveryBannerAdapter : BannerVPAdapter
    private var flag: Int = 1
    private var currentPosition = Int.MAX_VALUE / 2
    private val thread = Thread(PagerRunnable())
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

    //핸들러 설정
    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun initAfterBinding() {
        // 모든 fragment stack 제거
        clearBackStack()

        deliveryService = DeliveryService() //서비스 객체 생성
        deliveryService.setDeliveryView(this)
        deliveryService.setDeliveryBannerView(this)
        binding.deliveryProgressCover.visibility = View.GONE
        binding.deliveryBottomView.visibility = View.VISIBLE

        initBanner() //배너작업
        initSpinner() //필터(spinner) 작업
        initRadioBtn() //필터(radiobutton) 작업
        initTopScrollListener() // 상단 스크롤 작업
        initAdapter()

        binding.deliveryFloatingBtn.setOnClickListener {
            val intent = Intent(context, CreatePartyActivity::class.java)
            startActivity(intent)
        }

        if (totalCursor == 0)
            initLoadPosts()

        initScrollListener()
    }

    // 오늘 날짜 계산
    private fun calculateToday(): String {
        nowTime = System.currentTimeMillis();
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
        Log.d("calculateTime-HOUR", todayHours.toString())
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
        Log.d("remainTime", remainTime.toString())

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
        isLoading = false
        finalPage = false
        if (filterCheckFlag) getDeliveryFilterList(dormitoryId, totalCursor, orderTimeCategory, maxMatching)
        else getDeliveryAllList(dormitoryId, totalCursor)
    }

    // 리사이클러뷰에 더 보여줄 데이터를 로드하는 경우
    // TODO: 로딩 중에 스크롤 막기
    // TODO: 새로고침 했을 때 제일 밑으로 가게 만들기
    private fun initMoreLoadPosts() {
        binding.deliveryProgressCover.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            if (filterCheckFlag) getDeliveryFilterList(dormitoryId, totalCursor, orderTimeCategory, maxMatching)
            else getDeliveryAllList(dormitoryId, totalCursor)
            isLoading = false
            binding.deliveryProgressCover.visibility = View.GONE
        }, 1200)
    }

    // 상단 스크롤 관련
    private fun initTopScrollListener() {
        binding.deliverySwipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { /* swipe 시 진행할 동작 */
            deliveryArray.clear()
            initLoadPosts()
            initAdapter()
            binding.deliverySwipe.isRefreshing = false
        })
    }

    // 하단 스크롤 관련
    // TODO: 하단 스크롤 디자인 관련 수정 필요해보임! (지금은 오류 해결하려고 일단 디자인 이렇게 했어!)
    private fun initScrollListener() {
        binding.deliveryRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.deliveryRv.layoutManager

                if (finalPage == true) {
                    if ((layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= deliveryArray.size - 2)
                        binding.deliveryBottomView.visibility = View.INVISIBLE
                    else binding.deliveryBottomView.visibility = View.VISIBLE
                }

                if (!isLoading) {
                    if (layoutManager != null && (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == deliveryArray.size - 1) {
                        if (finalPage == false)
                            initMoreLoadPosts()
                        else binding.deliveryBottomView.visibility = View.INVISIBLE

                        isLoading = true
                    }
                }
            }
        })
    }

    // 배달 목록 가져오기
    private fun getDeliveryAllList(dormitoryId: Int, cursor: Int) {
        val deliveryDataService = DeliveryService()
        deliveryDataService.setDeliveryView(this)
        deliveryDataService.getDeliveryAllList(dormitoryId, cursor)
        totalCursor += 1
    }

    // Adapter 설정
    private fun initAdapter() {
        deliveryAdapter = DeliveryRVAdapter(deliveryArray)
        binding.deliveryRv.adapter = deliveryAdapter
        binding.deliveryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        deliveryAdapter.setOnItemClickListener(object : DeliveryRVAdapter.OnItemClickListener{
            override fun onItemClick(data: DeliveryPartiesVoList, pos : Int) {
                var deliveryItemId = deliveryAdapter.getDeliveryItemId(pos).toString()

                val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putString("deliveryItemId", deliveryItemId)

                val lookPartyFragment = LookPartyFragment()
                lookPartyFragment.arguments = bundle

                transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
                transaction.commit()
            }
        })
    }

    override fun deliverySuccess(result: DeliveryResult) {
        Log.d("DELIVERY-REPSONSE", "SUCCESS")

        finalPage = result.finalPage
        val result = result.deliveryPartiesVoList

        for (i in 0 until result!!.size) {
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hasHashTag

            deliveryArray.add(
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, calculateTime(orderTime!!), title, hashTags)
            )

            deliveryAdapter.notifyDataSetChanged()
        }
    }

    override fun deliveryFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FRAGMENT-FAILURE")
        totalCursor--
    }

    private fun initRadioBtn(){ //라디오 버튼
        binding.deliveryTimeRg.setOnCheckedChangeListener { _:RadioGroup, checkedId:Int ->
            binding.deliveryTimeRg.check(checkedId)
            filterCheckFlag = true

            when(checkedId){
                R.id.delivery_rb1 -> orderTimeCategory = "BREAKFAST"
                R.id.delivery_rb2 -> orderTimeCategory = "LUNCH"
                R.id.delivery_rb3 -> orderTimeCategory = "DINNER"
                R.id.delivery_rb4 -> orderTimeCategory = "MIDNIGHT_SNACKS"
                else -> filterCheckFlag = false
            }
        }
    }

    //배너 작업
    private fun initBanner(){
        deliveryService.getDeliveryBanner() //광고 불러오기
    }

    //페이지 변경하기
    fun setPage(){
        if(currentPosition == deliveryBannerAdapter.itemCount) //currentPosition이 마지막 페이지 다음페이지면
            currentPosition = 0
        binding.deliveryBannerVp.setCurrentItem(currentPosition, true)
        currentPosition+=1
    }

    //스피너 관련 작업
    private fun initSpinner(){
        val items = resources.getStringArray(R.array.home_dropdown1) // spinner아이템 배열
        //어댑터
        val spinnerAdapter = PeopleSpinnerAdapter(requireContext(), items)
        binding.deliveryPeopleSpinner.adapter = spinnerAdapter
        binding.deliveryPeopleSpinner.setSelection(items.size-1) //마지막아이템을 스피너 초기값으로 설정해준다.

        //이벤트 처리
        binding.deliveryPeopleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //TODO:스피너
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE
                items[0] = items[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))

                if (position in 1..5)
                    filterCheckFlag = true

                maxMatching = position * 2
                finalPage = false
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    override fun onResume() {
        super.onResume()
        flag = 1 // 다른 페이지 갔다가 돌아오면 다시 스크롤 시작
    }

    override fun onPause() {
        super.onPause()
        flag = 0 // 다른 페이지로 떠나있는 동안 스크롤 동작 필요없음. 멈추기
    }

    override fun onDestroy() {
        super.onDestroy()
        thread.interrupt() //쓰레드 중지
    }

    fun clearBackStack() {
        val fragmentManager: FragmentManager = (context as MainActivity).supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    //배너 작업
    override fun ondeliveryBannerSuccess(results: Array<DeliveryBannerResult>) {
        deliveryBannerAdapter = BannerVPAdapter(this)
        //더미 img url
        for(j in 1..5){ //fragment already added 고치기 위함
            for (i in results){
                deliveryBannerAdapter.addFragment(i.imgUrl)
            }
        }
        binding.deliveryBannerVp.adapter= deliveryBannerAdapter
        binding.deliveryBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        binding.deliveryBannerVp.setCurrentItem(currentPosition, false) // 시작위치 지정

        //뷰페이저 넘기는 쓰레드
        if (thread.state == Thread.State.NEW)
            thread.start() //스레드 시작

        binding.deliveryBannerVp.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when(state){
                        //뷰페이저가 멈춰져있을때
                        //SCROLL_STATE_IDLE 상태는 현재 스크롤을 하지 않는 상태
                        ViewPager2.SCROLL_STATE_IDLE ->{
                            flag=1
                            currentPosition = binding.deliveryBannerVp.currentItem+1
                        }
                        //뷰페이저 움직이는 중
                        ViewPager2.SCROLL_STATE_DRAGGING -> flag=0
                    }
                }
            })
        }

    }

    //3초마다 페이지 넘기는 기능
    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                try {
                    Thread.sleep(3000)
                    if(this@DeliveryFragment.flag==1) {
                        handler.sendEmptyMessage(0)
                    }
                } catch (e : InterruptedException){
                    Log.d("interupt", "interupt발생")
                }
            }
        }
    }

    override fun ondeliveryBannerFailure(message: String) {
        Log.d("commercial", "광고 불러오기 실패~!")
    }

    // 배달 목록 필터 적용 후 가져오기
    private fun getDeliveryFilterList(dormitoryId: Int, cursor: Int, orderTimeCategory: String?, maxMatching: Int?) {
        val deliveryDataService = DeliveryService()
        deliveryDataService.setDeliveryFilterView(this)
        deliveryDataService.getDeliveryFilterList(dormitoryId, cursor, orderTimeCategory, maxMatching)
        totalCursor += 1
    }

    override fun deliveryFilterSuccess(result: DeliveryResult) {
        Log.d("DELIVERY-FILTER", "SUCCESS")

        finalPage = result.finalPage

        val result = result.deliveryPartiesVoList

        for (i in 0 until result!!.size) {
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hasHashTag

            deliveryArray.add(
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, calculateTime(orderTime!!), title, hashTags)
            )

            deliveryAdapter.notifyItemChanged(deliveryArray.size - 1)
        }

        if (finalPage == true) {
            Log.d("DELIVERY-FILTER", "getDeliveryFilterList-DeliveryArray-Size = ${deliveryArray.size}")
            if (deliveryArray.size >= 2) {
                if ((binding.deliveryRv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= deliveryArray.size - 2)
                    binding.deliveryBottomView.visibility = View.INVISIBLE
                else binding.deliveryBottomView.visibility = View.VISIBLE
            } else binding.deliveryBottomView.visibility = View.VISIBLE
        }
    }

    override fun deliveryFilterFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FILTER-FRAGMENT-FAILURE")
        totalCursor--
    }
}