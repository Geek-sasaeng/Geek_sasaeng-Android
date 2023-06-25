package com.example.geeksasaeng.Home.Search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.deleteRecentSearch
import com.example.geeksasaeng.databinding.FragmentSearchBasicBinding
import com.example.geeksasaeng.Utils.getRecentSearches

class SearchBasicFragment: BaseFragment<FragmentSearchBasicBinding>(FragmentSearchBasicBinding::inflate) {

    private lateinit var searchRecentAdapter: SearchRecentRVAdapter
    private lateinit var searchRankAdapter: SearchRankRVAdapter
    private var stringSearchRecentArray : MutableList<String> = ArrayList()
    var searchRecentArray = ArrayList<DeliverySearchRecent>()
    var searchRankArray = ArrayList<DeliveryRank>()

    override fun initAfterBinding() {
        Log.d("searchFlag", "실행됨")
        stringSearchRecentArray = getRecentSearches() //sharedPreference에서 최근 검색어 불러오기
        initAdapter()
    }

    private fun initAdapter() {

        if (stringSearchRecentArray.isEmpty()){
            binding.searchBasicRecentItemRv.visibility = View.INVISIBLE
            binding.searchBasicNoRecentTv.visibility = View.VISIBLE
        }else{
/*            // View가 생성될 때마다 array에 추가되는 것을 막기 위함!
            if (searchRecentArray.size == 0) {
                searchRecentArray.apply {
                    for (search in stringSearchRecentArray){
                        add(DeliverySearchRecent(search)) // 리사이클러뷰 어댑터에 넣어줄 리스트에 추가
                }
              }
            }*/
            searchRecentArray.clear()
            searchRecentArray.apply {
                for (search in stringSearchRecentArray){
                    add(DeliverySearchRecent(search)) // 리사이클러뷰 어댑터에 넣어줄 리스트에 추가
                }
            }

            searchRecentAdapter = SearchRecentRVAdapter(searchRecentArray)
            binding.searchBasicRecentItemRv.adapter = searchRecentAdapter
            binding.searchBasicRecentItemRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            searchRecentAdapter.setOnItemClickListener(object : SearchRecentRVAdapter.OnItemClickListener{

                override fun onItemClick(keyword: DeliverySearchRecent, pos: Int) {
                    val parentActivity = activity as SearchActivity
                    parentActivity.doSearch(keyword.searchRecentWord.toString())
                }

                override fun onItemDeleteClick(keyword: DeliverySearchRecent, pos: Int) {
                    deleteRecentSearch(keyword.searchRecentWord.toString())
                    searchRecentAdapter.removeItem(pos)
                    searchRecentAdapter.notifyItemRemoved(pos)
                    Log.d("searchFlag", (stringSearchRecentArray.size - pos).toString())
                    searchRecentAdapter.notifyItemRangeChanged(pos, stringSearchRecentArray.size - pos)
                    stringSearchRecentArray.removeAt(pos)
                    if(stringSearchRecentArray.isEmpty()){
                        binding.searchBasicRecentItemRv.visibility = View.INVISIBLE
                        binding.searchBasicNoRecentTv.visibility = View.VISIBLE
                    }
                }
            })
        }

        //랭킹 시스템
//        if (searchRankArray.size != 10) {
//            searchRankArray.apply {
//                add(DeliveryRank(1, "치킨"))
//                add(DeliveryRank(2, "피자"))
//                add(DeliveryRank(3, "마라탕"))
//                add(DeliveryRank(4, "족발"))
//                add(DeliveryRank(5, "보쌈"))
//                add(DeliveryRank(6, "빙수"))
//                add(DeliveryRank(7, "돈까스"))
//                add(DeliveryRank(8, "김치찜"))
//                add(DeliveryRank(9, "냉면"))
//                add(DeliveryRank(10, "부대찌개"))
//            }
//        }
//
//        searchRankAdapter = SearchRankRVAdapter(searchRankArray)
//        binding.searchBasicRankRv.adapter = searchRankAdapter
//        binding.searchBasicRankRv.layoutManager = GridLayoutManager(context, 2)
    }
}