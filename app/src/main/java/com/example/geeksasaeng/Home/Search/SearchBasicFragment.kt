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

        searchRecentArray.apply {
            add(DeliverySearchRecent("햄버거"))
            add(DeliverySearchRecent("양식"))
            add(DeliverySearchRecent("피자"))
            add(DeliverySearchRecent("야식"))
            add(DeliverySearchRecent("한식"))
            add(DeliverySearchRecent("족발"))
            add(DeliverySearchRecent("치킨"))
        }

        searchRankArray.apply {
            add(DeliveryRank(1, "1위"))
            add(DeliveryRank(2, "2위"))
            add(DeliveryRank(3, "3위"))
            add(DeliveryRank(4, "4위"))
            add(DeliveryRank(5, "5위"))
            add(DeliveryRank(6, "6위"))
            add(DeliveryRank(7, "7위"))
            add(DeliveryRank(8, "8위"))
            add(DeliveryRank(9, "9위"))
            add(DeliveryRank(10, "10위"))
        }
    }

    private fun initAdapter() {
        searchRecentAdapter = SearchRecentRVAdapter(searchRecentArray)
        binding.searchBasicRecentItemRv.adapter = searchRecentAdapter
        binding.searchBasicRecentItemRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        searchRankAdapter = SearchRankRVAdapter(searchRankArray)
        binding.searchBasicRankRv.adapter = searchRankAdapter
        binding.searchBasicRankRv.layoutManager = GridLayoutManager(context, 2)
    }
}