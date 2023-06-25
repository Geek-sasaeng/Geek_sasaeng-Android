package com.example.geeksasaeng.Chatting.ChattingList

import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geeksasaeng.ChatSetting.ChatResponse
import com.example.geeksasaeng.ChatSetting.*
import com.example.geeksasaeng.databinding.*
import java.text.DecimalFormat

class ChattingRoomRVAdapter(var chattingList: ArrayList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                val binding = ItemChattingImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun bind(chatting: Chat) {
            binding.itemMyChattingChattingTv.text = chatting.content

            if (chatting.isLeader) { //리더라면 프로필 테두리 파랗게
                binding.itemMyChattingProfileCv.strokeColor = Color.parseColor("#3266EB")
            }

            if (chatting.unreadMemberCnt?.toString() == "0")
                binding.itemMyChattingNotReadTv.visibility = View.INVISIBLE
            else binding.itemMyChattingNotReadTv.text = chatting.unreadMemberCnt?.toString()

            binding.itemMyChattingNicknameTv.text = chatting.nickName
            binding.itemMyChattingTimeTv.text = setTime(chatting.createdAt)
            Glide.with(itemView.context)
                .load(chatting.profileImgUrl)
                .into(binding.itemMyChattingProfileIv)
            binding.itemMyChattingProfileIv.setOnClickListener {
                Log.d("CHATTING-SERVICE", "nickname = ${chatting.nickName} / profileImgUrl = ${chatting.profileImgUrl}")
                mCilckListener.onUserProfileClicked(chatting.profileImgUrl, chatting.readMembers[0])
            }
        }
    }

    inner class YourChattingViewHolder(val binding: ItemChattingYourChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chat) {
            binding.itemYourChattingChattingTv.text = chatting.content

            if (chatting.isLeader) { //리더라면 프로필 테두리 파랗게
                binding.itemYourChattingProfileCv.strokeColor = Color.parseColor("#3266EB")
            }

            if (chatting.unreadMemberCnt?.toString() == "0")
                binding.itemYourChattingNotReadTv.visibility = View.INVISIBLE
            else binding.itemYourChattingNotReadTv.text = chatting.unreadMemberCnt?.toString()

            binding.itemYourChattingNicknameTv.text = chatting.nickName
            binding.itemYourChattingProfileIv.setOnClickListener {
                Log.d("CHATTING-SERVICE", "nickname = ${chatting.nickName} / profileImgUrl = ${chatting.profileImgUrl}")
                mCilckListener.onUserProfileClicked(chatting.profileImgUrl, chatting.readMembers[0])
            }

            binding.itemYourChattingTimeTv.text = setTime(chatting.createdAt)
            Glide.with(itemView.context)
                .load(chatting.profileImgUrl)
                .into(binding.itemYourChattingProfileIv)
            Log.d("CHATTING-SYSTEM-TEST", "IMAGE URL = ${chatting.profileImgUrl}")
        }
    }

    interface OnUserProfileClickListener{
        fun onUserProfileClicked(profileImgUrl: String?, memberId: String)
    }

    fun setOnUserProfileClickListener(clickListener: OnUserProfileClickListener){
        mCilckListener = clickListener
    }

    inner class SystemChattingViewHolder(val binding: ItemChattingSystemChattingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chat) {
            binding.itemChattingSystemChattingTv.text = chatting.content
        }
    }

    // 이 부분 대신 사진 전송 부분으로 수정하기!!!
    // 이모티콘 부분 삭제됨!
    inner class ImageChattingViewHolder(val binding: ItemChattingImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: Chat) {
            // 사진 전송 관련 View 작업
            Log.d("API-TEST", "chatting = $chatting")
            // binding.itemImageChattingIn//

            Glide.with(itemView.context).load(chatting.content).into(binding.itemChattingImageIv)

            if (chatting.isLeader) { //리더라면 프로필 테두리 파랗게
                binding.itemImageChattingProfileCv.strokeColor = Color.parseColor("#3266EB")
            }

            if (chatting.unreadMemberCnt.toString() == "0")
                binding.itemImageChattingNotReadTv.visibility = View.INVISIBLE
            else binding.itemImageChattingNotReadTv.text = chatting.unreadMemberCnt.toString()

            binding.itemImageChattingNicknameTv.text = chatting.nickName
            binding.itemImageChattingTimeTv.text = setTime(chatting.createdAt)
            Glide.with(itemView.context)
                .load(chatting.profileImgUrl)
                .into(binding.itemImageChattingProfileIv)

            binding.itemImageChattingProfileIv.setOnClickListener {
                mCilckListener.onUserProfileClicked(chatting.profileImgUrl, chatting.readMembers[0])
            }
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

    fun addItem(item: Chat) {
        chattingList.add(item)
        this.notifyDataSetChanged()
    }

    fun addAllItems(items: ArrayList<Chat>) {
        chattingList.clear()
        chattingList.addAll(items)
        this.notifyDataSetChanged()
    }

    fun getAllItems(): ArrayList<Chat> {
        return chattingList
    }

    fun returnPosition(): Int {
        return chattingList.size
    }
}