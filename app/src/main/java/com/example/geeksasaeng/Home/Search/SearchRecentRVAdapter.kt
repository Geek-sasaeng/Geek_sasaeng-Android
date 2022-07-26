package com.example.geeksasaeng.Home.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.databinding.ItemRecentSearchBinding

class SearchRecentRVAdapter(private val searchRecentList: ArrayList<DeliverySearchRecent>) : RecyclerView.Adapter<SearchRecentRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecentRVAdapter.ViewHolder {
        val binding: ItemRecentSearchBinding = ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRecentRVAdapter.ViewHolder, position: Int) {
        holder.bind(searchRecentList[position]!!)
    }

    override fun getItemCount(): Int = searchRecentList.size

    inner class ViewHolder(val binding: ItemRecentSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (searchRecent: DeliverySearchRecent) {
            binding.itemRecentSearchCategory.text = searchRecent.searchRecentWord.toString()
        }
    }
}

data class DeliverySearchRecent (
    var searchRecentWord: String? = ""
)