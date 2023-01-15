package com.example.geeksasaeng.Profile.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyOngoingActivityResult
import com.example.geeksasaeng.Profile.Retrofit.myOngoingActivity
import com.example.geeksasaeng.Profile.Retrofit.myOngoingActivityDate
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemProfileMyActivityActivityBinding
import com.example.geeksasaeng.databinding.ItemProfileMyActivityTimeBinding

//이거는 지금 나의 활동 보기에서 현재 진행중 활동이 빠지면서 현재는 안쓰는 중

class MyOngoingActivityRVAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var activityList = ArrayList<ProfileMyOngoingActivityResult>()
    private lateinit var mItemClickListener: MyOngoingActivityRVAdapter.OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(data: ProfileMyOngoingActivityResult, pos: Int)
    }

    fun setOnItemClickListener(listener: MyOngoingActivityRVAdapter.OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return activityList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            myOngoingActivity -> {
                val binding = ItemProfileMyActivityActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ActivityViewHolder(binding)
            }
            else -> {
                val binding = ItemProfileMyActivityTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(activityList[position]!!, position)
        }

        when (activityList[position].viewType) {
            myOngoingActivity -> {
                (holder as MyOngoingActivityRVAdapter.ActivityViewHolder).bind(activityList[position])
                holder.setIsRecyclable(false)
            }
            myOngoingActivityDate -> {
                (holder as MyOngoingActivityRVAdapter.DateViewHolder).bind(activityList[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    override fun getItemCount(): Int = activityList.size

    inner class ActivityViewHolder(val binding: ItemProfileMyActivityActivityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(profileMyOngoingActivity: ProfileMyOngoingActivityResult) {
            binding.itemProfileMyActivityActivityLayoutSectionIv.setImageResource(R.drawable.ic_delivery_party_ic)
            binding.itemProfileMyActivityActivityTv.text = profileMyOngoingActivity.title
        }
    }

    inner class DateViewHolder(val binding: ItemProfileMyActivityTimeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(profileMyOngoingActivity: ProfileMyOngoingActivityResult) {
            binding.itemProfileMyActivityTimeTv.text = profileMyOngoingActivity.createdAt
        }
    }

    fun addItem(item: ProfileMyOngoingActivityResult) {
        activityList.add(item)
        this.notifyDataSetChanged()
    }

    fun addAllItems(items: ArrayList<ProfileMyOngoingActivityResult>) {
        activityList.addAll(items)
        this.notifyDataSetChanged()
    }

    fun clearItemList() {
        activityList.clear()
        this.notifyDataSetChanged()
    }

    fun getPartyId(position: Int): Int? {
        return activityList[position].id
    }
}