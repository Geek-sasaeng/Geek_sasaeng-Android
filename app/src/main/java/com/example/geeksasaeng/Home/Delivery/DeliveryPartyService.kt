package com.example.geeksasaeng.Home.Delivery

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyInterface
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyListResponse
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryPartyView
import com.example.geeksasaeng.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryPartyService {
    private lateinit var deliveryPartyView: DeliveryPartyView

    fun setDeliveryListView(deliveryListView: DeliveryPartyView){
        this.deliveryPartyView = deliveryListView;
    }

    // 배달 리스트 목록들 불러오기
    fun getDeliveryPartyList(domitory: Int){
        val deliveryPartyService = NetworkModule.getInstance()?.create(DeliveryPartyInterface::class.java)
        deliveryPartyService?.getAllDeliveryList(domitory)?.enqueue(object:
            Callback<DeliveryPartyListResponse>{
            override fun onResponse(
                call: Call<DeliveryPartyListResponse>,
                listResponse: Response<DeliveryPartyListResponse>
            ){
                Log.d("deliveryParty", "통신 성공")
                deliveryPartyView.deliveryPartyListSuccess(listResponse.body()!!)
            }
            override fun onFailure(call: Call<DeliveryPartyListResponse>, t: Throwable){
                Log.d("deliveryParty", "통신 실패")
                deliveryPartyView.deliveryPartyListFailure("${t}")
            }
        })
    }
}