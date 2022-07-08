package com.example.geeksasaeng.Home.Delivery

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Data.Delivery
import com.example.geeksasaeng.Home.CreateParty.CreatePartyFragment
import com.example.geeksasaeng.Home.Delivery.Adapter.BannerVPAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyListResponse
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding


class DeliveryFragment: BaseFragment<FragmentDeliveryBinding>(FragmentDeliveryBinding::inflate), DeliveryPartyView {
    private var deliveryArray = ArrayList<Delivery>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter
    private lateinit var deliveryService: DeliveryPartyService
    private lateinit var deliveryBannerAdapter : BannerVPAdapter
    private var flag: Int = 1
    private var currentPosition = Int.MAX_VALUE / 2
    private val thread = Thread(PagerRunnable())

    //핸들러 설정
    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun initAfterBinding() {

        //배너작업
        initBanner()
        //필터(spinner) 작업
        initSpinner()


        //루나 코드
        // 어댑터 설정
        deliveryAdapter = DeliveryRVAdapter(deliveryArray)
        binding.deliveryRv.adapter = deliveryAdapter
        // 어댑터 클릭 이벤트 설정
        deliveryAdapter.setOnItemClickListener(object : DeliveryRVAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Delivery, pos : Int) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_frm, LookPartyFragment())?.commit()
            }
        })

        binding.deliveryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        binding.deliveryFloatingBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frm, LookPartyFragment())?.commit()
        }

        deliveryArray.apply {
            add(Delivery("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("13시간 48분 남았어요", "중식 같이 먹어요 같이 먹자", true, true, 1, 2, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕", true, false, 3, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "나는 중식이 먹고 싶은데~", true, false, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "중식 같이 먹어요", false, true, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "중식 같이 먹어요", false, true, 1, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("13시간 48분 남았어요", "중식 같이 먹어요 같이 먹자", false, false, 3, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕", false, false, 4, 5, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "나는 중식이 먹고 싶은데~", true, true, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 1, 2, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 1, 6, R.drawable.ic_default_profile, "네오"))
            add(Delivery("13시간 48분 남았어요", "중식 같이 먹어요 같이 먹자", true, false, 4, 6, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕", true, true, 5, 6, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "나는 중식이 먹고 싶은데~", true, true, 1, 4, R.drawable.ic_default_profile, "네오"))
            add(Delivery("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 1, 2, R.drawable.ic_default_profile, "네오"))
        }

        // 배달 파티 리스트 받아오기
        deliveryService = DeliveryPartyService()
        deliveryService.setDeliveryListView(this)
        /*deliveryService.getDeliveryPartyList(1)*/
        binding.deliveryRv.adapter = deliveryAdapter
        binding.deliveryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        binding.deliveryFloatingBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frm, CreatePartyFragment())?.commit()
        }
    }

    // 파티 목록 받아오기
    override fun deliveryPartyListSuccess(listResponse: DeliveryPartyListResponse) {
        val result = listResponse.result
        for(i in result.indices) {
            val title = result[i].title
            val orderTime = result[i].orderTime
            val currentMatching = result[i].currentMatching
            val maxMatching = result[i].maxMatching
            deliveryArray.add(
                Delivery(
                    orderTime,
                    title,
                    true,
                    true,
                    currentMatching,
                    maxMatching,
                    R.drawable.ic_default_profile,
                    "test"
                )
            )
        }
        deliveryAdapter.notifyDataSetChanged()
    }



    override fun deliveryPartyListFailure(message: String) {
        Log.d("deliveryPartyList", "실패")
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
                            Log.d("banner", currentPosition.toString())
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
        /*val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            items
        )*/

        val spinnerAdapter = PeopleSpinnerAdapter(requireContext(), items)
        binding.deliveryPeopleSpinner.adapter = spinnerAdapter
        //이벤트 처리
        binding.deliveryPeopleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO:이게 맞나?
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE

                items[0]=items[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2)) //??꼭 fragment에선 requireContext가 먹더라..?
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun onResume() {
        super.onResume()
        flag=1 // 다른 페이지 갔다가 돌아오면 다시 스크롤 시작
    }

    override fun onPause() {
        super.onPause()
        flag=0 // 다른 페이지로 떠나있는 동안 스크롤 동작 필요없음. 멈추기
    }

    override fun onDestroy() {
        super.onDestroy()
        thread.interrupt() //쓰레드 중지
    }
}