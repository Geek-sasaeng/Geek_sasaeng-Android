package com.example.geeksasaeng.Home.Delivery

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Data.Delivery
import com.example.geeksasaeng.Home.CreateParty.CreatePartyActivity
import com.example.geeksasaeng.Home.CreateParty.CreatePartyFragment
import com.example.geeksasaeng.Home.Delivery.Adapter.BannerVPAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding

class DeliveryFragment: BaseFragment<FragmentDeliveryBinding>(FragmentDeliveryBinding::inflate), DeliveryView {
    private var deliveryArray = ArrayList<DeliveryResult?>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter
    private lateinit var deliveryService: DeliveryService
    private lateinit var deliveryBannerAdapter : BannerVPAdapter
    private var flag: Int = 1
    private var currentPosition = Int.MAX_VALUE / 2
    private val thread = Thread(PagerRunnable())
    var isLoading = false
    var dormitoryId: Int = 1
    var totalCursor: Int = 0
    var totalPost: Int = 0

    //핸들러 설정
    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun initAfterBinding() {
        initBanner() //배너작업
        initSpinner() //필터(spinner) 작업
        initRadioBtn() //필터(radiobutton) 작업
        initTopScrollListener() // 상단 스크롤 작업

        // 어댑터 설정
//        deliveryAdapter = DeliveryRVAdapter(deliveryArray)
//        binding.deliveryRv.adapter = deliveryAdapter

//        // 어댑터 클릭 이벤트 설정
//        DeliveryRVAdapter_new.setOnItemClickListener(object : DeliveryRVAdapter_ver1.OnItemClickListener{
//            override fun onItemClick(v: View, data: Delivery, pos : Int) {
//                activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.main_frm, LookPartyFragment())?.commit()
//            }
//        })

//        binding.deliveryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)


        // 배달 파티 리스트 받아오기
        deliveryService = DeliveryService()
        deliveryService.setDeliveryView(this)

        binding.deliveryFloatingBtn.setOnClickListener {
            //fragment->fragment로의 전환
            /*activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frm, CreatePartyFragment())?.commit()*/

            //fragment->activity로의 전환
            val intent = Intent(context, CreatePartyActivity::class.java)
            startActivity(intent)
        }

        // 테스트
        // Log.d("DELIVERY-RESPONSE", "initAfterBinding1-TOTAL-CURSOR = $totalCursor")
        Log.d("DELIVERY-RESPONSE", "initAfterBinding : getDeliveryAllList0")

        if (totalCursor == 0)
            getDeliveryAllList(dormitoryId, totalCursor)

        if ((totalCursor + 1) * 10 == totalPost) {

        }

        Log.d("DELIVERY-RESPONSE", "TOTAL-CURSOR : $totalCursor  /  TOTAL-POST : $totalPost")

        // Log.d("DELIVERY-RESPONSE", "initAfterBinding2-TOTAL-CURSOR = $totalCursor")
        // initScrollListener()
    }

    // 상단 스크롤 관련
    private fun initTopScrollListener() {
        binding.deliverySwipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { /* swipe 시 진행할 동작 */
            val handler = Handler()
            handler.postDelayed({
            }, 2000)
            binding.deliverySwipe.isRefreshing = false
        })
    }

    // 무한 스크롤 관련
    private fun initScrollListener() {
        binding.deliveryRv!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == deliveryArray.size - 1) {
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        deliveryArray.add(null)
        deliveryAdapter!!.notifyItemInserted(deliveryArray.size - 1)
        val handler = Handler()
        handler.postDelayed({
            deliveryArray.removeAt(deliveryArray.size - 1)
            val scrollPosition = deliveryArray.size
            deliveryAdapter!!.notifyItemRemoved(scrollPosition)
//            var currentSize = scrollPosition
//            val nextLimit = currentSize + 10
//            while (currentSize - 1 < nextLimit) {
//                // deliveryArray.add(DeliveryResult("3시간 48분 남았어요", currentSize.toString(), true, true, 1, 2))
//            }
            Log.d("DELIVERY-RESPONSE", "initAfterBinding : loadMore")
            getDeliveryAllList(dormitoryId, totalCursor)
            deliveryAdapter!!.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }

    // 배달 목록 가져오기
    private fun getDeliveryAllList(dormitoryId: Int, cursor: Int) {
        Log.d("DELIVERY-RESPONSE", "getDeliveryAllList")
        // 테스트
        // var dormitoryId = 0
        val deliveryDataService = DeliveryService()
        deliveryDataService.getDeliveryAllList(dormitoryId, cursor)
        deliveryDataService.setDeliveryView(this)
        totalCursor = totalCursor + 1
        totalPost = totalPost + 10
    }

    override fun deliverySuccess(response: DeliveryResponse) {
        Log.d("DELIVERY-REPSONSE", "SUCCESS")
        val result = response.result
        val size = result!!.size
        Log.d("DELIVERY-RESPONSE", "size = $size")

        for (i in 0 until result!!.size) {
            var chief = result?.get(i)?.chief
            var content = result?.get(i)?.content
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var location = result?.get(i)?.location
            var matchingStatus = result?.get(i)?.matchingStatus
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hashTags

            deliveryArray.add(
                DeliveryResult(chief, content, currentMatching, foodCategory, id, location, matchingStatus, maxMatching, orderTime, title, hashTags!!)
            )

            deliveryAdapter = DeliveryRVAdapter(deliveryArray)
            binding.deliveryRv.adapter = deliveryAdapter
            binding.deliveryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun deliveryFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FRAGMENT-FAILURE")
        totalCursor--
    }

    private fun initRadioBtn(){
        binding.deliveryTimeRg.setOnCheckedChangeListener { _:RadioGroup, checkedId:Int ->
            binding.deliveryTimeRg.check(checkedId)
            Log.d("radio",checkedId.toString() +" 선택됨")
            when(checkedId){
                R.id.delivery_rb1->Log.d("radio","아침 선택됨")
                R.id.delivery_rb2->Log.d("radio","점심 선택됨")
                R.id.delivery_rb3->Log.d("radio","저녁 선택됨")
                R.id.delivery_rb4->Log.d("radio","야식 선택됨")
                else->{

                }
            }
        }
    }

    //배너 작업
    private fun initBanner(){
        deliveryBannerAdapter = BannerVPAdapter(this)
        //일단은 더미데이터 넣어둠
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.ic_chat))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))

        binding.deliveryBannerVp.adapter= deliveryBannerAdapter
        binding.deliveryBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        binding.deliveryBannerVp.setCurrentItem(currentPosition, false) // 시작위치 지정

        //뷰페이저 넘기는 쓰레드
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
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO:스피너
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE
                items[0]=items[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
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
}