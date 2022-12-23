package com.example.geeksasaeng.Home.Search.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryRetrofitInterfaces
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDataService {

    private lateinit var searchPartyView: SearchView
    private lateinit var searchFilterView: SearchFilterView

    fun setSearchPartyView(searchPartyView: SearchView) {
        this.searchPartyView = searchPartyView
    }
    fun setSearchFilterView(searchFilterView: SearchFilterView) {
        this.searchFilterView = searchFilterView
    }

    val SearchPartyService = NetworkModule.getInstance()?.create(SearchRetrofitInterface::class.java)

    fun getSearchPartyList(dormitoryId: Int, cursor: Int, keyword: String) {
        SearchPartyService?.getSearchPartyList("Bearer " + getJwt(), dormitoryId, cursor, keyword)?.enqueue(object: Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                Log.d("SEARCH-RESPONSE", "dormitoryId = $dormitoryId / cursor = $cursor / keyword = $keyword")
                Log.d("SEARCH-RESPONSE", "response.code = ${response.code()} / response.body = ${response.body()}")
                if (response.isSuccessful && response.code() == 200) {
                    val searchResponse: SearchResponse = response.body()!!
                    Log.d("SEARCH-RESPONSE", "response.code = ${searchResponse.code} / response.body = ${searchResponse.message}")
                    when (searchResponse.code) {
                        1000 -> searchPartyView.onSearchSuccess(response.body()!!.result)
                        else -> searchPartyView.onSearchFailure(searchResponse.code, searchResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("SEARCH-RESPONSE", "SearchPartyService-onFailure : SearchFailed", t)
            }
        })
    }

    // 배달 리스트 필터 적용 후 목록들 불러오기
    fun getSearchFilterList(dormitoryId: Int, cursor: Int, keyword: String, orderTimeCategory: String?, maxMatching: Int?){
        SearchPartyService?.getFilterSearchList("Bearer " + getJwt(), dormitoryId, cursor, keyword, orderTimeCategory, maxMatching)?.enqueue(object: Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                // Log.d("DELIVERY-FILTER", "response.code = ${response.code()} / response.body = ${response.body()}")
                Log.d("SEARCH-FRAGMENT", "getSearchFilterList keyword = $keyword / orderTimeCategory = $orderTimeCategory / maxMatching = $maxMatching")
                Log.d("SEARCH-FRAGMENT", "response.code = ${response.code()}")
                if (response.isSuccessful && response.code() == 200) {
                    val searchResponse: SearchResponse = response.body()!!
                    Log.d("why", "성공"+searchResponse.toString())
                    Log.d("SEARCH-FRAGMENT", "response.code = ${searchResponse.code} / response.body = ${searchResponse.result}")
                    when (searchResponse.code) {
                        1000 -> searchFilterView.searchFilterSuccess(response.body()!!.result)
                        else -> searchFilterView.searchFilterFailure(searchResponse.code, searchResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("SEARCH-RESPONSE", "SEARCHService-onFailure : DeliveryFailed", t)
            }
        })
    }
}