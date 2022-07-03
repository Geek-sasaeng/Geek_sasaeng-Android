package com.example.geeksasaeng.Home.Delivery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.CreatePartyFragment
import com.example.geeksasaeng.Data.DeliveryData
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyListResponse
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyView
import com.example.geeksasaeng.Home.Delivery.Service.DeliveryPartyService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding


class DeliveryFragment: Fragment(), DeliveryPartyView {
    lateinit var binding : FragmentDeliveryBinding
    private var deliveryArray = ArrayList<DeliveryData>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter
    private lateinit var deliveryService: DeliveryPartyService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDeliveryBinding.inflate(inflater, container, false)
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

        return binding.root
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
}