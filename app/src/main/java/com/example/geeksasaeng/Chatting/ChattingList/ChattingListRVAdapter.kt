package com.example.geeksasaeng.Chatting.ChattingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Chatting.ChattingList.Retrofit.ChattingList
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemChattingListBinding

class ChattingListRVAdapter(private var chattingList: java.util.ArrayList<ChattingList?>) : RecyclerView.Adapter<ChattingListRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(chatting: ChattingList, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChattingListBinding = ItemChattingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(chattingList[position]!!, position)
        }
        holder.bind(chattingList[position]!!)
    }

//    fun setChattingData(position: Int, chattingList: ChattingList) {
//        chatting.set(position, chattingList)
//        this.notifyItemChanged(position)
//    }

    fun addAllItems(items: java.util.ArrayList<ChattingList?>) {
        chattingList.clear()
        chattingList.addAll(items)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = chattingList.size

    inner class ViewHolder(val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChattingList) {
            binding.itemChattingListTitle.text = chatting.roomTitle
            // 배달파티인지, 거래인지 등을 구분하기 위함
            binding.itemChattingListSectionIv.setImageResource(R.drawable.ic_delivery_party_ic)

            // TODO: Firebase를 통해 적용했던 부분이라서 수정 필요
//            binding.itemChattingListLastChatting.text = chatting.lastChat
//            binding.itemChattingListTimeTv.text = chatting.lastMsgTime
//            binding.itemChattingListChattingNumberTv.text = chatting.newMsg
//            if(chatting.newMsg==""){
//                binding.itemChattingListLastChatting.setTextColor(Color.parseColor("#636363"))
//            }
        }
    }
}