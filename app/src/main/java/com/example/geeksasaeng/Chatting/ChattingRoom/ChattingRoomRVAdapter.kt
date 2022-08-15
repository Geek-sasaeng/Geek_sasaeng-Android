package com.example.geeksasaeng.Chatting.ChattingList

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Chatting.ChattingRoom.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.*
import okhttp3.internal.notify
import kotlin.collections.ArrayList

class ChattingRoomRVAdapter(private var chattingList: MutableList<Chatting>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            emoticonChatting -> {
                val binding = ItemChattingEmoticonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return EmoticonChattingViewHolder(binding)
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
            emoticonChatting -> {
                (holder as EmoticonChattingViewHolder).bind(chattingList[position])
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
            binding.itemMyChattingNicknameTv.text = chatting.nickname
            binding.itemMyChattingTimeTv.text = setTime(chatting.time)
            // binding.itemMyChattingProfileIv.setImageURI(Uri.parse(chatting?.senderImgUrl))
            binding.itemMyChattingProfileIv.setImageResource(chatting.senderImgUrl!!)
        }
    }

    inner class YourChattingViewHolder(val binding: ItemChattingYourChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            binding.itemYourChattingChattingTv.text = chatting.message
            binding.itemYourChattingNotReadTv.text = chatting.notRead?.toString()
            binding.itemYourChattingNicknameTv.text = chatting.nickname
            binding.itemYourChattingTimeTv.text = setTime(chatting.time)
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

    inner class EmoticonChattingViewHolder(val binding: ItemChattingEmoticonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chatting) {
            // 이모티콘으로 인사해보세요 관련 binding
        }
    }

    override fun getItemCount(): Int = chattingList.size

    fun setTime(time: String): String {
        val hour = Integer.parseInt(time.substring(11, 13))
        val minute = Integer.parseInt(time.substring(14, 16))
        return if (hour in 0..11)
            "오전 $hour:$minute"
        else "오후 $hour:$minute"
    }

    fun itemSort() {
        if (chattingList.isNotEmpty()){ //emptyList를 sort할 수 없으므로
            var items = chattingList.sortedBy { it.time } as MutableList<Chatting>
            addAllItems(items)
        }
    }

    fun addItem(item: Chatting) {
        chattingList.add(item)
        this.notifyDataSetChanged()
    }

    fun addAllItems(items: MutableList<Chatting>) {
        chattingList.clear()
        chattingList.addAll(items)
        this.notifyDataSetChanged()
    }

    fun returnPosition(): Int {
        return chattingList.size
    }
}