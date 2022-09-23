package com.example.geeksasaeng.Home.Party.Retrofit

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CreatePartyService {
    //뷰 객체 생성
    private lateinit var createPartyDefaultLocView: CreatePartyDefaultLocView //기숙사 deafult위치 불러오기
    private lateinit var createPartyView: CreatePartyView // 파티 생성하기 등록

    private val createPartyDataService = retrofit.create(CreatePartyRetrofitInterfaces::class.java)

    //setView
    fun setCreatePartyDefaultLocView(createPartyDefaultLocView: CreatePartyDefaultLocView){
        this.createPartyDefaultLocView = createPartyDefaultLocView
    }

    fun setCreatePartyView(createPartyView: CreatePartyView){
        this.createPartyView = createPartyView
    }

    //기숙사 위치 정보
    fun getDefaultLoc( dormitoryId : Int) {
        createPartyDataService.getDeliveryPartyDefaultLocation(dormitoryId).enqueue(object :
            Callback<CreatePartyDefaultLocResponse> {
            override fun onResponse(
                call: Call<CreatePartyDefaultLocResponse>,
                response: Response<CreatePartyDefaultLocResponse>
            ) {
                Log.d("kakaodefault", response.toString())
                Log.d("kakaodefault", response.body()?.message.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp: CreatePartyDefaultLocResponse = response.body()!!
                    Log.d("kakaodefault", resp.toString())
                    createPartyDefaultLocView.onDefaultLocSuccess(resp.result!!)
                }
            }

            override fun onFailure(call: Call<CreatePartyDefaultLocResponse>, t: Throwable) {
                Log.d("kakaodefault", "onFailure 호출됨")
            }
        })
    }

    //파티 생성하기
    fun createPartySender(dormitoryId: Int, createPartyRequest: CreatePartyRequest){
        createPartyDataService.createParty("Bearer " + getJwt(), dormitoryId, createPartyRequest).enqueue(object:
        Callback<CreatePartyResponse>{
            override fun onResponse(
                call: Call<CreatePartyResponse>,
                response: Response<CreatePartyResponse>
            ) {
                Log.d("jjang", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("jjang-response", response.toString())
                    val resp: CreatePartyResponse = response.body()!!
                    Log.d("jjang-resp", resp.toString())
                    when (resp.code) {
                        1000 -> createPartyView.onCreatePartySuccess(resp.result!!)
                        else -> createPartyView.onCreatePartyFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<CreatePartyResponse>, t: Throwable) {
                Log.d("jjang", "파티 생성하기 onFailure"+t.toString())
            }

        })
    }

}

