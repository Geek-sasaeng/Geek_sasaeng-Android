package com.example.geeksasaeng.Home.Search

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentSearchBasicBinding

class SearchBasicFragment: BaseFragment<FragmentSearchBasicBinding>(FragmentSearchBasicBinding::inflate) {

    private lateinit var searchRecentAdapter: SearchRecentRVAdapter
    private lateinit var searchRankAdapter: SearchRankRVAdapter
    var searchRecentArray = ArrayList<DeliverySearchRecent>()
    var searchRankArray = ArrayList<DeliveryRank>()

    override fun initAfterBinding() {
        initAdapter()
    }

    private fun initAdapter() {
        // View가 생성될 때마다 array에 추가되는 것을 막기 위함!
        if (searchRecentArray.size == 0) {
            searchRecentArray.apply {
                add(DeliverySearchRecent("햄버거"))
                add(DeliverySearchRecent("양식"))
                add(DeliverySearchRecent("피자"))
                add(DeliverySearchRecent("야식"))
                add(DeliverySearchRecent("한식"))
                add(DeliverySearchRecent("족발"))
                add(DeliverySearchRecent("치킨"))
            }
        }

        searchRecentAdapter = SearchRecentRVAdapter(searchRecentArray)
        binding.searchBasicRecentItemRv.adapter = searchRecentAdapter
        binding.searchBasicRecentItemRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (searchRankArray.size != 10) {
            searchRankArray.apply {
                add(DeliveryRank(1, "치킨"))
                add(DeliveryRank(2, "피자"))
                add(DeliveryRank(3, "마라탕"))
                add(DeliveryRank(4, "족발"))
                add(DeliveryRank(5, "보쌈"))
                add(DeliveryRank(6, "빙수"))
                add(DeliveryRank(7, "돈까스"))
                add(DeliveryRank(8, "김치찜"))
                add(DeliveryRank(9, "냉면"))
                add(DeliveryRank(10, "부대찌개"))
            }
        }

        searchRankAdapter = SearchRankRVAdapter(searchRankArray)
        binding.searchBasicRankRv.adapter = searchRankAdapter
        binding.searchBasicRankRv.layoutManager = GridLayoutManager(context, 2)
    }
}