package com.example.geeksasaeng.Chatting.ChattingList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.R
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

    override fun getItemCount(): Int = chatting.size

    fun setChattingData(position: Int, chattingData: ChattingData) {
        chatting.set(position, chattingData)
        this.notifyItemChanged(position)
    }

    fun addAllItems(items: MutableList<ChattingData>) {
        chatting.clear()
        chatting.addAll(items)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChattingData) {
            binding.itemChattingListSectionIv.setImageResource(R.drawable.ic_delivery_party_ic)
            binding.itemChattingListTitle.text = chatting.roomData.roomName
            binding.itemChattingListLastChatting.text = chatting.lastChat
            binding.itemChattingListTimeTv.text = chatting.lastMsgTime
            binding.itemChattingListChattingNumberTv.text = chatting.newMsg
            if(chatting.newMsg==""){
                binding.itemChattingListLastChatting.setTextColor(Color.parseColor("#636363"))
            }
        }
    }
}