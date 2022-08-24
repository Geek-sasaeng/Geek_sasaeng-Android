package com.example.geeksasaeng.Profile.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Profile.Retrofit.EndedDeliveryPartiesVoList
import com.example.geeksasaeng.Profile.Retrofit.ProfileMyPreActivityResult
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemProfilePreActivityBinding
import com.google.android.material.internal.EdgeToEdgeUtils

class MyPreActivityRVAdapter: RecyclerView.Adapter<MyPreActivityRVAdapter.ViewHolder>() {

    private var activityList = ArrayList<EndedDeliveryPartiesVoList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPreActivityRVAdapter.ViewHolder {
        val binding: ItemProfilePreActivityBinding = ItemProfilePreActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPreActivityRVAdapter.ViewHolder, position: Int) {
        holder.bind(activityList[position])
    }

    override fun getItemCount(): Int = activityList.size

    inner class ViewHolder(val binding: ItemProfilePreActivityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(profileMyPreActivityResult: EndedDeliveryPartiesVoList) {
            binding.itemProfilePreActivityLayoutSectionIv.setImageResource(R.drawable.ic_delivery_party_ic)
            binding.itemProfilePreActivityTv.text = profileMyPreActivityResult.title
            binding.itemProfilePreActivityMemberNumberTv.text = profileMyPreActivityResult.maxMatching.toString()
            binding.itemProfilePreActivityCategoryTv.text = profileMyPreActivityResult.foodCategory
            binding.itemProfilePreActivityDateTv.text = "${profileMyPreActivityResult.updatedAt.substring(0, 4)}.${profileMyPreActivityResult.updatedAt.substring(5, 7)}.${profileMyPreActivityResult.updatedAt.substring(8, 10)}"
        }
    }

    fun addItem(item: EndedDeliveryPartiesVoList) {
        activityList.add(item)
        this.notifyDataSetChanged()
    }

    fun addAllItems(items: ArrayList<EndedDeliveryPartiesVoList>) {
        activityList.clear()
        activityList.addAll(items)
        this.notifyDataSetChanged()
    }
}