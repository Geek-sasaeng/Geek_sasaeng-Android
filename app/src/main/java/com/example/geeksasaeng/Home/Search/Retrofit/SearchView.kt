package com.example.geeksasaeng.Home.Search.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryResult

interface SearchView {
    fun onSearchSuccess(result: SearchResult)
    fun onSearchFailure(code: Int, message: String)
}