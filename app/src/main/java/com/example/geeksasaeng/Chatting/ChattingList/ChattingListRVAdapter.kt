package com.example.geeksasaeng.Chatting.ChattingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.databinding.ItemChattingListBinding
import kotlin.collections.ArrayList

class ChattingListRVAdapter(private var chattingList: ArrayList<ChattingListData>) : RecyclerView.Adapter<ChattingListRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(chattingList: ChattingListData, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChattingListBinding = ItemChattingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(chattingList[position], position)
        }

        holder.bind(chattingList[position]!!)
    }

    override fun getItemCount(): Int = chattingList.size

    inner class ViewHolder(val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chattingList: ChattingListData) {
            binding.itemChattingListTitle.text = chattingList.roomName
            binding.itemChattingListLastChatting.text = chattingList.lastChat
            binding.itemChattingListTimeTv.text = chattingList.lastTime
            binding.itemChattingListChattingNumberTv.text = chattingList.newMsg
        }
    }
}