package com.example.geeksasaeng.Profile.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Chatting.ChattingList.ChattingData
import com.example.geeksasaeng.Chatting.ChattingList.ChattingListRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityResult
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemProfileMyActivityActivityBinding

class MyOngoingActivityRVAdapter(private var activityList: ArrayList<ProfileMyOngoingActivityResult>): RecyclerView.Adapter<MyOngoingActivityRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: MyOngoingActivityRVAdapter.OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(data: ProfileMyOngoingActivityResult, pos: Int)
    }

    fun setOnItemClickListener(listener: MyOngoingActivityRVAdapter.OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOngoingActivityRVAdapter.ViewHolder {
        val binding: ItemProfileMyActivityActivityBinding = ItemProfileMyActivityActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOngoingActivityRVAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(activityList[position]!!, position)
        }
        holder.bind(activityList[position])
    }

    override fun getItemCount(): Int = activityList.size

    inner class ViewHolder(val binding: ItemProfileMyActivityActivityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(profileMyOngoingActivity: ProfileMyOngoingActivityResult) {
            // binding.itemProfileMyActivityActivityLayoutSectionIv.setImageURI(Uri.parse(profileMyOngoingActivity.storeUrl))
            binding.itemProfileMyActivityActivityLayoutSectionIv.setImageResource(R.drawable.ic_default_profile2)
            binding.itemProfileMyActivityActivityTv.text = profileMyOngoingActivity.title
        }
    }

    fun addItem(item: ProfileMyOngoingActivityResult) {
        activityList.add(item)
        this.notifyDataSetChanged()
    }
}