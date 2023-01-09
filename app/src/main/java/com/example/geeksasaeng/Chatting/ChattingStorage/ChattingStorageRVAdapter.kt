package com.example.geeksasaeng.Chatting.ChattingStorage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemChattingStorageListBinding

class ChattingStorageRVAdapter(private var chattingStorage: ArrayList<ChattingStorageData>): RecyclerView.Adapter<ChattingStorageRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(chatting: ChattingStorageData, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChattingStorageListBinding = ItemChattingStorageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(chattingStorage[position]!!, position)
        }

        holder.bind(chattingStorage[position]!!)
    }

    override fun getItemCount(): Int = chattingStorage.size

    inner class ViewHolder(val binding: ItemChattingStorageListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChattingStorageData) {
            binding.itemChattingStorageListImageIv.setImageResource(R.drawable.ic_delivery_party_ic)
            binding.itemChattingStorageListTitle.text = chatting.title
            binding.itemChattingStorageListCategory.text = chatting.foodCategory
            binding.itemChattingStorageListTimeTv.text = chatting.date
        }
    }
}