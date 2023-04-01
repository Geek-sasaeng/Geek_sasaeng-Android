package com.example.geeksasaeng.Chatting.ChattingRoom.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.MemberData
import com.example.geeksasaeng.databinding.ItemForcedExitDialogBinding

class DialogForcedExitRVAdapter (var memberList: MutableList<MemberData>) : RecyclerView.Adapter<DialogForcedExitRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemForcedExitDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memberList[position])
    }

    override fun getItemCount(): Int = memberList.size

    inner class ViewHolder(val binding: ItemForcedExitDialogBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(member : MemberData){
            Glide.with(binding.root.context).load(member.userProfileImgUrl).into(binding.itemForcedExitDialogImageIv)
            binding.itemForcedExitDialogTv.text = member.userName
        }
    }

}