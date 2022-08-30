package com.example.geeksasaeng.Home.Delivery.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResponse
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryService {
    //뷰 객체 생성
    private lateinit var deliveryBannerView: DeliveryBannerView
    private lateinit var deliveryView: DeliveryView
    private lateinit var deliveryFilterView: DeliveryFilterView
    private val deliveryDataService = retrofit.create(DeliveryRetrofitInterfaces::class.java)

    //setView
    fun setDeliveryBannerView(deliveryBannerView: DeliveryBannerView){
        this.deliveryBannerView = deliveryBannerView
    }
    fun setDeliveryView(deliveryView: DeliveryView){
        this.deliveryView = deliveryView
    }
    fun setDeliveryFilterView(deliveryFilterView: DeliveryFilterView) {
        this.deliveryFilterView = deliveryFilterView
    }

    fun getDeliveryBanner(){
        deliveryDataService.getCommercials().enqueue(object : Callback<DeliveryBannerResponse> {
            override fun onResponse(
                call: Call<DeliveryBannerResponse>,
                response: Response<DeliveryBannerResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val deliveryBannerResponse = response.body()!!

                    when (deliveryBannerResponse.code) {
                        1000 -> deliveryBannerView.ondeliveryBannerSuccess(deliveryBannerResponse.result!!) //광고불러오기
                        else -> deliveryBannerView.ondeliveryBannerFailure(deliveryBannerResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<DeliveryBannerResponse>, t: Throwable) {
                Log.d("commerical", "실패")
            }

        })
    }

    // 배달 리스트 목록들 불러오기
    fun getDeliveryAllList(dormitoryId: Int, cursor: Int){
        val deliveryPartyService = NetworkModule.getInstance()?.create(DeliveryRetrofitInterfaces::class.java)
        deliveryPartyService?.getAllDeliveryList("Bearer " + getJwt(), dormitoryId, cursor)?.enqueue(object:
            Callback<DeliveryResponse> {
            override fun onResponse(call: Call<DeliveryResponse>, response: Response<DeliveryResponse>) {
                Log.d("DELIVERY-RESPONSE", "response.code = ${response.code()} / response.body = ${response.body()}")
                if (response.isSuccessful && response.code() == 200) {
                    val deliveryResponse: DeliveryResponse = response.body()!!
                    Log.d("DELIVERY-RESPONSE", "response.code = ${deliveryResponse.code} / response.body = ${deliveryResponse.message}")
                    Log.d("why-filter",deliveryResponse.toString())
                    when (deliveryResponse.code) {
                        1000 -> deliveryView.deliverySuccess(deliveryResponse.result)
                        else -> deliveryView.deliveryFailure(deliveryResponse.code, deliveryResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
                Log.d("DELIVERY-RESPONSE", "DeliveryService-onFailure : DeliveryFailed", t)
            }
        })
    }

    // 배달 리스트 필터 적용 후 목록들 불러오기
    fun getDeliveryFilterList(dormitoryId: Int, cursor: Int, orderTimeCategory: String?, maxMatching: Int?){
        val deliveryPartyService = NetworkModule.getInstance()?.create(DeliveryRetrofitInterfaces::class.java)
        deliveryPartyService?.getFilterDeliveryList("Bearer " + getJwt(), dormitoryId, cursor, orderTimeCategory, maxMatching)?.enqueue(object: Callback<DeliveryResponse> {
            override fun onResponse(call: Call<DeliveryResponse>, response: Response<DeliveryResponse>) {
                // Log.d("DELIVERY-FILTER", "response.code = ${response.code()} / response.body = ${response.body()}")
                Log.d("DELIVERY-FILTER", "response.code = ${response.code()}")
                if (response.isSuccessful && response.code() == 200) {
                    val deliveryResponse: DeliveryResponse = response.body()!!
                    // Log.d("DELIVERY-FILTER", "response.code = ${deliveryResponse.code} / response.body = ${deliveryResponse.message}")
                    when (deliveryResponse.code) {
                        1000 -> deliveryFilterView.deliveryFilterSuccess(response.body()!!.result)
                        else -> deliveryFilterView.deliveryFilterFailure(deliveryResponse.code, deliveryResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
                Log.d("DELIVERY-RESPONSE", "DeliveryService-onFailure : DeliveryFailed", t)
            }
        })
    }

}