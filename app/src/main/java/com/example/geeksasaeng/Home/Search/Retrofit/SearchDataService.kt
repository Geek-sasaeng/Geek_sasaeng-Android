package com.example.geeksasaeng.Home.Search.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDataService {

    private lateinit var searchPartyView: SearchView

    fun setSearchPartyView(searchPartyView: SearchView) {
        this.searchPartyView = searchPartyView
    }

    fun getSearchPartyList(dormitoryId: Int, cursor: Int, keyword: String) {
        val searchPartyService = NetworkModule.getInstance()?.create(SearchRetrofitInterface::class.java)
        searchPartyService?.getSearchPartyList(dormitoryId, cursor, keyword)?.enqueue(object: Callback<SearchResponse> {
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
}