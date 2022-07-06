package com.example.geeksasaeng.Home.Delivery

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.CreatePartyFragment
import com.example.geeksasaeng.Data.DeliveryData
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyListResponse
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyView
import com.example.geeksasaeng.Home.Delivery.Service.DeliveryPartyService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding


class DeliveryFragment: BaseFragment<FragmentDeliveryBinding>(FragmentDeliveryBinding::inflate), DeliveryPartyView {
    private var deliveryArray = ArrayList<DeliveryData>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter
    private lateinit var deliveryService: DeliveryPartyService
    private lateinit var deliveryBannerAdapter : BannerVPAdapter
    private var flag: Int = 1
    private var currentPosition = Int.MAX_VALUE / 2

    //핸들러 설정
    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun initAfterBinding() {

        //배너작업
        initBanner()

        // 어댑터 설정
        deliveryAdapter = DeliveryRVAdapter(deliveryArray)
        binding.deliveryRv.adapter = deliveryAdapter
        // 어댑터 클릭 이벤트 설정
        deliveryAdapter.setOnItemClickListener(object : DeliveryRVAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: DeliveryData, pos : Int) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_frm, LookPartyFragment())?.commit()
            }
        })

        binding.deliveryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        binding.deliveryFloatingBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frm, LookPartyFragment())?.commit()
        }
        /*deliveryArray.apply {
            add(DeliveryData("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("13시간 48분 남았어요", "중식 같이 먹어요 같이 먹자", true, true, 1, 2, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕", true, false, 3, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "나는 중식이 먹고 싶은데~", true, false, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "중식 같이 먹어요", false, true, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "중식 같이 먹어요", false, true, 1, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("13시간 48분 남았어요", "중식 같이 먹어요 같이 먹자", false, false, 3, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕", false, false, 4, 5, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "나는 중식이 먹고 싶은데~", true, true, 2, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 1, 2, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 1, 6, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("13시간 48분 남았어요", "중식 같이 먹어요 같이 먹자", true, false, 4, 6, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕왕", true, true, 5, 6, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "나는 중식이 먹고 싶은데~", true, true, 1, 4, R.drawable.ic_default_profile, "네오"))
            add(DeliveryData("3시간 48분 남았어요", "중식 같이 먹어요", true, true, 1, 2, R.drawable.ic_default_profile, "네오"))
        }*/

        // 배달 파티 리스트 받아오기
        deliveryService = DeliveryPartyService()
        deliveryService.setDeliveryListView(this)
        deliveryService.getDeliveryPartyList(1)
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
                DeliveryData(
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
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.ic_chatting))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))
        deliveryBannerAdapter.addFragment(BannerFragment(R.drawable.home_banner))


        binding.deliveryBannerVp.adapter= deliveryBannerAdapter
        binding.deliveryBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        binding.deliveryBannerVp.setCurrentItem(currentPosition, false) // 시작위치 지정

        //뷰페이저 넘기는 쓰레드
        val thread = Thread(PagerRunnable())
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




}