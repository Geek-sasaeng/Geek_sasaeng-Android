package com.example.geeksasaeng.Chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.databinding.ItemChattingListBinding

// TODO: Chatting ArrayList로 바꿔주기
class ChattingListRVAdapter(private var chattingList: ArrayList<DeliveryPartiesVoList?>) : RecyclerView.Adapter<ChattingListRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingListRVAdapter.ViewHolder {
        val binding: ItemChattingListBinding = ItemChattingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chattingList[position]!!)
    }

    override fun getItemCount(): Int = chattingList.size

    // TODO: Chatting ArrayList로 바꿔주기
    inner class ViewHolder(val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chattingList: DeliveryPartiesVoList) {

        }
    }
}