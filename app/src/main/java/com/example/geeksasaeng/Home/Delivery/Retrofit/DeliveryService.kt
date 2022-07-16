package com.example.geeksasaeng.Home.Delivery.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryService {
    private lateinit var deliveryView: DeliveryView

    fun setDeliveryView(deliveryView: DeliveryView){
        this.deliveryView = deliveryView;
    }

    // 배달 리스트 목록들 불러오기
    fun getDeliveryAllList(dormitory: Int, cursor: Int){
        val deliveryPartyService = NetworkModule.getInstance()?.create(DeliveryInterface::class.java)
        deliveryPartyService?.getAllDeliveryList(dormitory, cursor)?.enqueue(object:
            Callback<DeliveryResponse>{
            override fun onResponse(call: Call<DeliveryResponse>, response: Response<DeliveryResponse>) {
                Log.d("DELIVERY-RESPONSE", "DeliveryService-onResponse : response.code = " + response.code())
                Log.d("DELIVERY-RESPONSE", "DeliveryService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val deliveryResponse: DeliveryResponse = response.body()!!

                    Log.d("DELIVERY-RESPONSE", "BODY = \n ${response.body()!!}")
                    Log.d("DELIVERY-RESPONSE", "TYPE = \n ${response.body()!!.javaClass}")

                    when (deliveryResponse.code) {
                        1000 -> deliveryView.deliverySuccess(response.body()!!) //회원가입 성공
                        else -> {
                            //회원가입 실패할 경우
                            deliveryView.deliveryFailure(deliveryResponse.code, deliveryResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
                Log.d("DELIVERY-RESPONSE", "DeliveryService-onFailure : DeliveryFailed", t)
            }
        })
    }
}