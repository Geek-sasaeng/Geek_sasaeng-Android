package com.example.geeksasaeng.Chatting.ChattingList

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.ChatSetting.ChatResponse
import com.example.geeksasaeng.ChatSetting.*
import com.example.geeksasaeng.databinding.*
import java.text.DecimalFormat

class ChattingRoomRVAdapter(var chattingList: ArrayList<ChatResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mCilckListener : OnUserProfileClickListener

    override fun getItemViewType(position: Int): Int {
         return chattingList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
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
            imageChatting -> {
                val binding = ItemChattingEmoticonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ImageChattingViewHolder(binding)
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
            imageChatting -> {
                (holder as ImageChattingViewHolder).bind(chattingList[position])
                holder.setIsRecyclable(false)
            }
            else -> {
                (holder as SystemChattingViewHolder).bind(chattingList[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class MyChattingViewHolder(val binding: ItemChattingMyChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChatResponse) {
            binding.itemMyChattingChattingTv.text = chatting.content

            if (chatting.isLeader) { //리더라면 프로필 테두리 파랗게
                binding.itemMyChattingProfileCv.strokeColor = Color.parseColor("#3266EB")
            }

            if (chatting.unreadMemberCnt?.toString() == "0")
                binding.itemMyChattingNotReadTv.visibility = View.INVISIBLE
            else binding.itemMyChattingNotReadTv.text = chatting.unreadMemberCnt?.toString()

            binding.itemMyChattingNicknameTv.text = chatting.nickName
            binding.itemMyChattingTimeTv.text = setTime(chatting.createdAt)
            binding.itemMyChattingProfileIv.setImageURI(Uri.parse(chatting.profileImgUrl))
        }
    }

    inner class YourChattingViewHolder(val binding: ItemChattingYourChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChatResponse) {
            binding.itemYourChattingChattingTv.text = chatting.content

            if (chatting.isLeader) { //리더라면 프로필 테두리 파랗게
                binding.itemYourChattingProfileCv.strokeColor = Color.parseColor("#3266EB")
            }

            if (chatting.unreadMemberCnt?.toString() == "0")
                binding.itemYourChattingNotReadTv.visibility = View.INVISIBLE
            else binding.itemYourChattingNotReadTv.text = chatting.unreadMemberCnt?.toString()

            binding.itemYourChattingNicknameTv.text = chatting.nickName
            binding.itemYourChattingProfileIv.setOnClickListener {
                mCilckListener.onUserProfileClicked()
            }

            binding.itemYourChattingTimeTv.text = setTime(chatting.createdAt)
            binding.itemYourChattingProfileIv.setImageURI(Uri.parse(chatting.profileImgUrl))
        }
    }

    interface OnUserProfileClickListener{
        fun onUserProfileClicked()
    }

    fun setOnUserProfileClickListener(clickListener: OnUserProfileClickListener){
        mCilckListener = clickListener
    }

    inner class SystemChattingViewHolder(val binding: ItemChattingSystemChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChatResponse) {
            binding.itemChattingSystemChattingTv.text = chatting.content
        }
    }

    // 이 부분 대신 사진 전송 부분으로 수정하기!!!
    // 이모티콘 부분 삭제됨!
    // ItemChattingEmoticonBinding 부분 수정하기!
    inner class ImageChattingViewHolder(val binding: ItemChattingEmoticonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChatResponse) {
            // 이모티콘으로 인사해보세요 관련 binding
        }
    }

    override fun getItemCount(): Int = chattingList.size

    fun setTime(time: String): String {
        val hour = Integer.parseInt(time.substring(11, 13))
        val minute = DecimalFormat("00").format(Integer.parseInt(time.substring(14, 16)))
        return if (hour == 0)
            "오전 12:$minute"
        else if (hour in 1..11)
            "오전 $hour:$minute"
        else "오후 ${hour - 12}:$minute"
    }

    fun addItem(item: ChatResponse) {
        chattingList.add(item)
        this.notifyDataSetChanged()
    }

    fun addAllItems(items: ArrayList<ChatResponse>) {
        chattingList.clear()
        chattingList.addAll(items)
        this.notifyDataSetChanged()
    }

    fun returnPosition(): Int {
        return chattingList.size
    }
}