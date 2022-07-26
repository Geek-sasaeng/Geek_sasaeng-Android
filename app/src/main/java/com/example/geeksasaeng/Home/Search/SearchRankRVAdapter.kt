package com.example.geeksasaeng.Home.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.databinding.ItemRankingSearchBinding

class SearchRankRVAdapter(private val deliveryRankList: ArrayList<DeliveryRank>) : RecyclerView.Adapter<SearchRankRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRankRVAdapter.ViewHolder {
        val binding: ItemRankingSearchBinding = ItemRankingSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRankRVAdapter.ViewHolder, position: Int) {
        holder.bind(deliveryRankList[position]!!)
    }

    override fun getItemCount(): Int = deliveryRankList.size

    inner class ViewHolder(val binding: ItemRankingSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (deliveryRank: DeliveryRank) {
            binding.itemRankingSearchRank.text = deliveryRank.rank.toString()
            binding.itemRankingSearchFoodTv.text = deliveryRank.food
        }
    }
}

data class DeliveryRank (
    var rank: Int = 0,
    var food: String = ""
)