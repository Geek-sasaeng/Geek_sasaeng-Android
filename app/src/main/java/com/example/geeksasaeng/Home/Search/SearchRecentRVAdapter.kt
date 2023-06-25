package com.example.geeksasaeng.Home.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.databinding.ItemRecentSearchBinding

class SearchRecentRVAdapter(private val searchRecentList: ArrayList<DeliverySearchRecent>) : RecyclerView.Adapter<SearchRecentRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecentRVAdapter.ViewHolder {
        val binding: ItemRecentSearchBinding = ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 클릭 리스너 구현 위한 인터페이스
    interface OnItemClickListener {
        fun onItemClick(keyword: DeliverySearchRecent, pos: Int)
        fun onItemDeleteClick(keyword: DeliverySearchRecent, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onBindViewHolder(holder: SearchRecentRVAdapter.ViewHolder, position: Int) {
        holder.bind(searchRecentList[position]!!)
        holder.binding.itemRecentSearchKeywordTv.setOnClickListener {
            mItemClickListener.onItemClick(searchRecentList[position]!!, position)
        }
        holder.binding.itemRecentSearchDeleteBtn.setOnClickListener {
            mItemClickListener.onItemDeleteClick(searchRecentList[position]!!, position)
        }
    }

    override fun getItemCount(): Int = searchRecentList.size

    inner class ViewHolder(val binding: ItemRecentSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (searchRecent: DeliverySearchRecent) {
            binding.itemRecentSearchKeywordTv.text = searchRecent.searchRecentWord.toString()
        }
    }

    fun removeItem(position: Int) {
        searchRecentList.removeAt(position)
    }
}

data class DeliverySearchRecent (
    var searchRecentWord: String? = ""
)