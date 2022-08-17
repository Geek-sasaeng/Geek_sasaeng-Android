package com.example.geeksasaeng.Chatting.ChattingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.databinding.ItemChattingListBinding
import kotlin.collections.ArrayList

class ChattingListRVAdapter(private var chatting: ArrayList<ChattingData>) : RecyclerView.Adapter<ChattingListRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(chatting: ChattingData, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChattingListBinding = ItemChattingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(chatting[position]!!, position)
        }

        holder.bind(chatting[position]!!)
    }

    fun setChattingData(position: Int, chattingData: ChattingData) {
        chatting.set(position, chattingData)
        this.notifyItemChanged(position)
    }

    fun addAllItems(items: MutableList<ChattingData>) {
        chatting.clear()
        chatting.addAll(items)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = chatting.size

    inner class ViewHolder(val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChattingData) {
            binding.itemChattingListTitle.text = chatting.roomData.roomName
            binding.itemChattingListLastChatting.text = chatting.lastChat
            binding.itemChattingListTimeTv.text = chatting.lastMsgTime
            binding.itemChattingListChattingNumberTv.text = chatting.newMsg
        }
    }
}