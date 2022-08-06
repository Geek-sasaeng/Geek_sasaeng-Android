package com.example.geeksasaeng.Chatting.ChattingList

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Chatting.ChattingRoom.Chatting
import com.example.geeksasaeng.Chatting.ChattingRoom.myChatting
import com.example.geeksasaeng.Chatting.ChattingRoom.systemChatting
import com.example.geeksasaeng.Chatting.ChattingRoom.yourChatting
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemChattingListBinding
import com.example.geeksasaeng.databinding.ItemChattingMyChattingBinding
import com.example.geeksasaeng.databinding.ItemChattingSystemChattingBinding
import com.example.geeksasaeng.databinding.ItemChattingYourChattingBinding
import kotlin.collections.ArrayList

class ChattingRoomRVAdapter(private var chattingList: ArrayList<Chatting>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return chattingList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType){
            myChatting -> {
                val binding = ItemChattingMyChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyChattingViewHolder(binding)
            }
            yourChatting -> {
                val binding = ItemChattingYourChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return YourChattingViewHolder(binding)
            }
            systemChatting -> {
                val binding = ItemChattingSystemChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SystemChattingViewHolder(binding)
            }
            else -> {
                val binding = ItemChattingSystemChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SystemChattingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (chattingList[position].viewType) {
            myChatting -> {
                (holder as MyChattingViewHolder).bind(chattingList[position])
                holder.setIsRecyclable(false)
            }
            yourChatting -> {
                (holder as YourChattingViewHolder).bind(chattingList[position])
                holder.setIsRecyclable(false)
            }
            systemChatting -> {
                (holder as SystemChattingViewHolder).bind(chattingList[position])
                holder.setIsRecyclable(false)
            }
            else -> {
                (holder as SystemChattingViewHolder).bind(chattingList[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class MyChattingViewHolder(val binding: ItemChattingMyChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            binding.itemMyChattingChattingTv.text = chatting.message
            binding.itemMyChattingNotReadTv.text = chatting.notRead?.toString()
            // binding.itemMyChattingProfileIv.setImageURI(Uri.parse(chatting?.senderImgUrl))
            binding.itemMyChattingProfileIv.setImageResource(chatting.senderImgUrl!!)
        }
    }

    inner class YourChattingViewHolder(val binding: ItemChattingYourChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            binding.itemYourChattingChattingTv.text = chatting.message
            binding.itemYourChattingNotReadTv.text = chatting.notRead?.toString()
            // binding.itemYourChattingProfileIv.setImageURI(Uri.parse(chatting?.senderImgUrl))
            binding.itemYourChattingProfileIv.setImageResource(chatting.senderImgUrl!!)
        }
    }

    inner class SystemChattingViewHolder(val binding: ItemChattingSystemChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            // 00 님이 입장하셨습니다
            // 모든 파티원이 입장을 마쳤습니다! 안내에 따라 메뉴를 입력해주세요
            binding.itemChattingSystemChattingTv.text = chatting.message
        }
    }

    override fun getItemCount(): Int = chattingList.size
}