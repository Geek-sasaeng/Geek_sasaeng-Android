package com.example.geeksasaeng.Home.Delivery

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Data.DeliveryData
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemDeliveryBinding

class DeliveryRVAdapter(private var deliveryList: ArrayList<DeliveryData>) : RecyclerView.Adapter<DeliveryRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeliveryRVAdapter.ViewHolder {
        val binding: ItemDeliveryBinding = ItemDeliveryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryRVAdapter.ViewHolder, position: Int) {
        holder.bind(deliveryList[position])
    }

    override fun getItemCount(): Int = deliveryList.size

    inner class ViewHolder(val binding: ItemDeliveryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (delivery: DeliveryData) {
            binding.deliveryItemTime.text = delivery.time
            binding.deliveryItemTitle.text = delivery.title

            if (delivery.option1) {
                binding.deliveryItemOption1.visibility = View.VISIBLE

                if (delivery.option2)
                    binding.deliveryItemOption2.visibility = View.VISIBLE
            } else {
                binding.deliveryItemOption1.text = ""
                binding.deliveryItemOption1.setPadding(0, 0, 0, 0)

                if (delivery.option2)
                    binding.deliveryItemOption2.visibility = View.VISIBLE
            }


            binding.deliveryItemMemberNumber.text = delivery.currentMember.toString() + "/" + delivery.totalMember.toString()

            if (delivery.totalMember - delivery.currentMember == 1) {
                binding.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_blue)
                binding.deliveryItemMemberNumber.setTextColor(Color.parseColor("#29ABE2"))
            }
            else {
                binding.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_gray)
                binding.deliveryItemMemberNumber.setTextColor(Color.parseColor("#A8A8A8"))
            }

            binding.deliveryHostProfile.setImageResource(delivery.hostProfile)
            binding.deliveryHostName.text = delivery.hostName
        }
    }
}