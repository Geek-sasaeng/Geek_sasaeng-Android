package com.example.geeksasaeng.Home.Delivery

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.geeksasaeng.Home.CreateParty.CreatePartyActivity
import com.example.geeksasaeng.Home.Delivery.Adapter.BannerVPAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryBannerView
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryFilterView
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryService
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryView
import com.example.geeksasaeng.Home.Delivery.Timer.DeliveryTimer
import com.example.geeksasaeng.Home.Delivery.Timer.TimerData
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getDormitoryId
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class DeliveryFragment : BaseFragment<FragmentDeliveryBinding>(FragmentDeliveryBinding::inflate),
    DeliveryView, DeliveryFilterView, DeliveryBannerView {

    lateinit var loadingAnimationView: LottieAnimationView
    var loadingBannerFlag = false
    var loadingDeliveryListFlag = false
    private var deliveryArray = ArrayList<DeliveryPartiesVoList?>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter
    private lateinit var deliveryService: DeliveryService //서비스 객체
    private lateinit var deliveryBannerAdapter: BannerVPAdapter
    private lateinit var timerTask: DeliveryTimer
    private var flag: Int = 1
    private var currentPosition = Int.MAX_VALUE / 2
    private val thread = Thread(PagerRunnable())
    var isLoading = false
    var dormitoryId: Int = 0
    var totalCursor: Int = 0
    var orderTimeCategory: String? = null
    var maxMatching: Int? = null
    var finalPage: Boolean? = false
    var filterCheckFlag: Boolean = false
    var filter1CheckFlag: Boolean = false //인원수
    var filter2CheckFlag: Boolean = false //카테고리
    private var lastCheckedBox = -1
    private lateinit var handler: Handler
    private var checkBinding: Boolean = false;

    override fun onResume() {
        super.onResume()

        loadingDeliveryListFlag = false
        loadingBannerFlag = false

        loadingStart()

        //핸들러 설정
        handler = Handler(Looper.getMainLooper()) {
            setPage()
            true
        }

        refreshing() // 화면이 다시 시작될 때 파티 목록 리프레시
        initBanner() // 배너작업
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

    override fun initAfterBinding() {
        checkBinding = true
        dormitoryId = getDormitoryId()!!
        // 모든 fragment stack 제거
        clearBackStack()

        initDeliveryService()

        binding.deliveryProgressCover.visibility = View.GONE
        binding.deliveryBottomView.visibility = View.VISIBLE

        //initBanner() //배너작업
        initSpinner() //필터(spinner) 작업
        initCheckBox() //필터(checkBox) 작업
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

    // 리사이클러뷰에 최초로 넣어줄 데이터를 로드하는 경우
    private fun initLoadPosts() {
        Log.d("why-filter", "initLoadPosts 실행됨")
        deliveryArray.clear() //최초로, 리프레시용으로 데이터를 로드하기 때문에 기존 array에 있는건 지워준다.
        totalCursor = 0
        isLoading = false
        finalPage = false
        filterCheckFlag = filter1CheckFlag||filter2CheckFlag
        if (filterCheckFlag) getDeliveryFilterList(dormitoryId, totalCursor, orderTimeCategory, maxMatching)
        else getDeliveryAllList(dormitoryId, totalCursor)
    }

    // 리사이클러뷰에 더 보여줄 데이터를 로드하는 경우
    // TODO: 로딩 중에 스크롤 막기
    // TODO: 새로고침 했을 때 제일 밑으로 가게 만들기
    private fun initMoreLoadPosts() {
        Log.d("why-filter", "initMoreLoadePosts 실행됨")
        Log.d("why-filter", "initMore에서의 totalCursor"+totalCursor.toString())
        binding.deliveryProgressCover.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            filterCheckFlag = filter1CheckFlag||filter2CheckFlag
            if (filterCheckFlag) getDeliveryFilterList(dormitoryId, totalCursor, orderTimeCategory, maxMatching)
            else getDeliveryAllList(dormitoryId, totalCursor)
            isLoading = false
            binding.deliveryProgressCover.visibility = View.GONE
        }, 1200)
    }

    // 상단 스크롤 관련
    private fun initTopScrollListener() {
        binding.deliverySwipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { /* swipe 시 진행할 동작 */
            initAdapter()
            initLoadPosts()
            binding.deliverySwipe.isRefreshing = false
        })
    }

    private fun refreshing(){ // 파티목록 새로고침(initLoadPost랑 하는 일은 같은데,, 일단은 이 변수명 좀 썼으면 좋겠어여 refreshing이 직관적이고,, 코드의 변경가능성도 있어서욥!)
        initAdapter() //TODO: 리프레시할때 매번 InitAdapter안해도 될것 같은데 일단 타이머 바인드가 안돼서... 어댑터를 새로 만들어주는 중
        initLoadPosts()
        binding.deliverySwipe.isRefreshing = false
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

    // 하단 스크롤 관련
    // TODO: 하단 스크롤 디자인 관련 수정 필요해보임! (지금은 오류 해결하려고 일단 디자인 이렇게 했어!)
    private fun initScrollListener() {
        binding.deliveryRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.deliveryRv.layoutManager

                if (finalPage == true && totalCursor!=0) {
                    if ((layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= deliveryArray.size - 2)
                        binding.deliveryBottomView.visibility = View.INVISIBLE
                    else binding.deliveryBottomView.visibility = View.VISIBLE
                }

                Log.d("why",isLoading.toString())
                if (!isLoading) {
                    if (layoutManager != null && (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == deliveryArray.size - 1) {
                        if (finalPage == false  && totalCursor!=0)
                            initMoreLoadPosts()
                        else binding.deliveryBottomView.visibility = View.INVISIBLE

                        isLoading = true
                    }
                }
            }
        })
    }
    private fun initDeliveryService(){
        deliveryService = DeliveryService() //서비스 객체 생성
        deliveryService.setDeliveryView(this)
        deliveryService.setDeliveryBannerView(this)
    }

    // 배달 목록 가져오기
    private fun getDeliveryAllList(dormitoryId: Int, cursor: Int) {
        Log.d("why-filter", "getDeliveryAllList dormitoryId=$dormitoryId cursor=$cursor")
        deliveryService.getDeliveryAllList(dormitoryId, cursor)
        totalCursor += 1
    }

    // 배달파티 목록 조회 성공
    override fun deliverySuccess(result: DeliveryResult) {
        loadingDeliveryListFlag = true

        if (loadingDeliveryListFlag && loadingBannerFlag)
            loadingStop()

        finalPage = result.finalPage
        val result = result.deliveryPartiesVoList

        Log.d("why-filter", deliveryArray.toString())
        for (i in 0 until result!!.size) {
            var currentMatching = result?.get(i)?.currentMatching
            var foodCategory = result?.get(i)?.foodCategory
            var id = result?.get(i)?.id
            var maxMatching = result?.get(i)?.maxMatching
            var orderTime = result?.get(i)?.orderTime
            var title = result?.get(i)?.title
            var hashTags = result?.get(i)?.hasHashTag

            deliveryArray.add(
                // DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, calculateTime(orderTime!!), title, hashTags)
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, orderTime!!, title, hashTags)
            )

           /* deliveryAdapter.notifyDataSetChanged()*/
        }
        Log.d("why-filter", "필터 x"+deliveryArray.toString())
        deliveryAdapter.setArrayList(deliveryArray) //RVAdapter의 arraylist를 deliveryArray로 설정하고 notifyDataSetChanged를 해주는 함수이다.
    }

    override fun deliveryFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FRAGMENT-FAILURE")
        totalCursor--
    }

    private fun checkDoubleCheck(i: Int){ // 이 함수는 더블체크로 아이템을 해제하는 경우 수행할 행동을 정해두었다.
        Log.d("why-filter", "checkDoubleCheck 실행됨")
        Log.d("why-filter", lastCheckedBox.toString() +"      :      "+ i.toString())
        if(lastCheckedBox==i){ //만약 제일 마지막으로 체크된 애를 다시 선택해서 선택해제시키는 경우(더블체크)라면
            lastCheckedBox = -1 //초기화
            orderTimeCategory = null
            filter2CheckFlag = false
            refreshing()
        }
    }

    private fun initCheckBox() { //체크버튼 이벤트 (아침, 점심, 저녁)

        //setOnClick리스너를 통해 더블체크인지 여부 확인 & 그에 맞는 동작 수행
        binding.deliveryCb1.setOnClickListener {
            checkDoubleCheck(R.id.delivery_cb1)
        }

        binding.deliveryCb2.setOnClickListener {
            checkDoubleCheck(R.id.delivery_cb2)
        }

        binding.deliveryCb3.setOnClickListener {
            checkDoubleCheck(R.id.delivery_cb3)
        }

        binding.deliveryCb4.setOnClickListener {
            checkDoubleCheck(R.id.delivery_cb4)
        }

        binding.deliveryCb1.setOnCheckedChangeListener { buttonView, isChecked ->
            filterCheckFlag = true
            if(isChecked){
                binding.deliveryCb2.isChecked = false
                binding.deliveryCb3.isChecked = false
                binding.deliveryCb4.isChecked = false
                orderTimeCategory = "BREAKFAST"
                filter2CheckFlag = true
                refreshing()
            }else{ // 체크가 꺼지면
                lastCheckedBox = R.id.delivery_cb1
            }
            Log.d("check",orderTimeCategory.toString())
        }

        binding.deliveryCb2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.deliveryCb1.isChecked = false
                binding.deliveryCb3.isChecked = false
                binding.deliveryCb4.isChecked = false
                orderTimeCategory = "LUNCH"
                filter2CheckFlag = true
                refreshing()
            }else{ // 체크가 꺼지면
                lastCheckedBox = R.id.delivery_cb2
            }
            Log.d("check",orderTimeCategory.toString())
        }

        binding.deliveryCb3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.deliveryCb1.isChecked = false
                binding.deliveryCb2.isChecked = false
                binding.deliveryCb4.isChecked = false
                orderTimeCategory = "DINNER"
                filter2CheckFlag = true
                refreshing()
            }else{ // 체크가 꺼지면
                lastCheckedBox = R.id.delivery_cb3
            }
            Log.d("check",orderTimeCategory.toString())
        }

        binding.deliveryCb4.setOnCheckedChangeListener { buttonView, isChecked ->
            filterCheckFlag = true
            if(isChecked){
                binding.deliveryCb1.isChecked = false
                binding.deliveryCb2.isChecked = false
                binding.deliveryCb3.isChecked = false
                orderTimeCategory = "MIDNIGHT_SNACKS"
                filter2CheckFlag = true
                refreshing()
            }else{ // 체크가 꺼지면
                lastCheckedBox = R.id.delivery_cb4
            }
            Log.d("check",orderTimeCategory.toString())
        }

    }

    //스피너 관련 작업
    private fun initSpinner() {
        val items = resources.getStringArray(R.array.home_dropdown1) // spinner아이템 배열

        //어댑터
        val spinnerAdapter = PeopleSpinnerAdapter(requireContext(), items)
        binding.deliveryPeopleSpinner.adapter = spinnerAdapter
        binding.deliveryPeopleSpinner.setSelection(items.size - 1) //마지막아이템을 스피너 초기값으로 설정해준다.

        //이벤트 처리
        binding.deliveryPeopleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //TODO:스피너
                Log.d("peopleSpinner", "$position/")
                if(position==0){ // 제일 상단 클릭하면 초기화 해주기 위해
                    items[0]= items[items.size-1] // 인원선택(마지막값)을 현재선택값으로 넣어준다
                    maxMatching = 12 //maxMatching을 최댓값인 12로 설정해준다.
                }else{
                    items[0] = items[position] // items[0]은 현재 선택된 아이템 저장용
                    maxMatching = position * 2
                }
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))

                filter1CheckFlag = position in 1..5 // 1~5사이 아이템을 선택하면 filterCheckFlag true. 아니면 false(false인 경우는 젤 상단 아이템 선택해서 스피너 선택해제하는 경우)
                finalPage = false
                refreshing()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //배너 작업
    private fun initBanner() {
        deliveryService.getDeliveryBanner() //광고 불러오기
    }

    //페이지 변경하기
    fun setPage() {
        if (currentPosition == deliveryBannerAdapter.itemCount) //currentPosition이 마지막 페이지 다음페이지면
            currentPosition = 0
        binding.deliveryBannerVp.setCurrentItem(currentPosition, true)
        currentPosition += 1
    }


    private fun clearBackStack() {
        val fragmentManager: FragmentManager = (context as MainActivity).supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    //배너 작업
    override fun ondeliveryBannerSuccess(results: Array<DeliveryBannerResult>) {
        if (checkBinding) {
            loadingBannerFlag = true

            if (loadingDeliveryListFlag && loadingBannerFlag)
                loadingStop()

            deliveryBannerAdapter = BannerVPAdapter(this)

            //더미 img url
            for (j in 1..5) { //fragment already added 고치기 위함
                for (i in results) {
                    deliveryBannerAdapter.addFragment(i.imgUrl)
                }
            }

            binding.deliveryBannerVp.adapter = deliveryBannerAdapter
            binding.deliveryBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.deliveryBannerVp.setCurrentItem(currentPosition, false) // 시작위치 지정

            //뷰페이저 넘기는 쓰레드
            if (thread.state == Thread.State.NEW)
                thread.start() //스레드 시작

            binding.deliveryBannerVp.apply {
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        when (state) {
                            //뷰페이저가 멈춰져있을때
                            //SCROLL_STATE_IDLE 상태는 현재 스크롤을 하지 않는 상태
                            ViewPager2.SCROLL_STATE_IDLE -> {
                                flag = 1
                                currentPosition = binding.deliveryBannerVp.currentItem + 1
                            }
                            //뷰페이저 움직이는 중
                            ViewPager2.SCROLL_STATE_DRAGGING -> flag = 0
                        }
                    }
                })
            }
        }
    }

    //3초마다 페이지 넘기는 기능
    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                try {
                    Thread.sleep(3000)
                    if (this@DeliveryFragment.flag == 1) {
                        handler.sendEmptyMessage(0)
                    }
                } catch (e: InterruptedException) {
                    Log.d("interrupt", "interrupt 발생")
                }
            }
        }
    }

    override fun ondeliveryBannerFailure(message: String) {
        Log.d("commercial", "광고 불러오기 실패~!")
    }

    // 배달 목록 필터 적용 후 가져오기
    private fun getDeliveryFilterList(dormitoryId: Int, cursor: Int, orderTimeCategory: String?, maxMatching: Int?) {
        Log.d("why-filter", "getDeliveryFilterList dormitoryId=$dormitoryId cursor=$cursor / orderTimeCategory = $orderTimeCategory / maxMatching = $maxMatching")
        val deliveryDataService = DeliveryService()
        deliveryDataService.setDeliveryFilterView(this)
        deliveryDataService.getDeliveryFilterList(dormitoryId, cursor, orderTimeCategory, maxMatching)
        totalCursor += 1
    }

    // 배달 필터 성공
    override fun deliveryFilterSuccess(result: DeliveryResult) {
        loadingDeliveryListFlag = true

        if (loadingDeliveryListFlag && loadingBannerFlag)
            loadingStop()

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
                DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, orderTime!!, title, hashTags)
                // DeliveryPartiesVoList(currentMatching, foodCategory, id, maxMatching, calculateTime(orderTime!!), title, hashTags)
            )
            /*deliveryAdapter.notifyItemChanged(deliveryArray.size - 1)*/
        }

        if (finalPage == true) {
            Log.d("DELIVERY-FILTER", "getDeliveryFilterList-DeliveryArray-Size = ${deliveryArray.size}")
            if (deliveryArray.size >= 2) {
                if ((binding.deliveryRv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= deliveryArray.size - 2)
                    binding.deliveryBottomView.visibility = View.INVISIBLE
                else binding.deliveryBottomView.visibility = View.VISIBLE
            } else binding.deliveryBottomView.visibility = View.VISIBLE
        }

        Log.d("why-filter", "필터 o"+deliveryArray.toString())
        deliveryAdapter.setArrayList(deliveryArray) //어댑터에 바뀐 ArrayList를 알려주는 용
    }

    override fun deliveryFilterFailure(code: Int, message: String) {
        Log.d("DELIVERY-RESPONSE", "DELIVERY-FILTER-FRAGMENT-FAILURE")
        totalCursor--
    }

    private fun loadingStart() {
        if (checkBinding) {
            loadingAnimationView = binding.animationView
            binding.animationViewLayout.visibility = View.VISIBLE
            loadingAnimationView.visibility = View.VISIBLE
            loadingAnimationView.playAnimation()
            loadingAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }
                override fun onAnimationEnd(animation: Animator?) {
                    // initAfterBinding()
                }
                override fun onAnimationCancel(p0: Animator?) {
                }
                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
        }
    }

    private fun loadingStop() {
        if (checkBinding) {
            loadingAnimationView.cancelAnimation()
            binding.animationViewLayout.visibility = View.GONE
            loadingAnimationView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkBinding = false
    }
}