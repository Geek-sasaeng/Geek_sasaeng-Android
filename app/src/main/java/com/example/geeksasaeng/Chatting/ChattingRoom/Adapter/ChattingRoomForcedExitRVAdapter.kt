package com.example.geeksasaeng.Chatting.ChattingRoom.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.MemberData
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.getForcedExitCheckList
import com.example.geeksasaeng.Utils.saveForcedExitCheckList

import com.example.geeksasaeng.databinding.ItemChattingRoomForcedExitListBinding

class ChattingRoomForcedExitRVAdapter(var memberList: MutableList<MemberData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mItemClickListener: OnItemClickListener
    private var mForcedExitMemberList: MutableList<MemberData> = ArrayList()
    private var mcheckboxList : MutableList<Boolean> = ArrayList()

    // 클릭 리스너 구현 위한 인터페이스
    interface OnItemClickListener {
        fun onItemClick(forcedExitMemberList: MutableList<MemberData>) //강퇴당할 멤버리스트 넘기기
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChattingRoomForcedExitListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChattingRoomForcedExitRVAdapter.ViewHolder).bind(memberList[position], position)

        //아이템 클릭이벤트 - RVAdapter 내부에서 수행되어야하는 내용
        if(memberList[position].accountTransferStatus){
            //송금완료한 애는 강제퇴장할 수 없다.
            holder.binding.itemChattingRoomForcedExitCheckBox.visibility = View.INVISIBLE
        }else{
            //아이템 클릭 이벤트
            holder.itemView.setOnClickListener {
                holder.binding.itemChattingRoomForcedExitCheckBox.isChecked = !holder.binding.itemChattingRoomForcedExitCheckBox.isChecked //체크를 먼저 시켜줘야 mForcedExitMemberList에 들어간채로 onItemClick이 수행됨!
                mItemClickListener.onItemClick(mForcedExitMemberList)
            }
            //체크박스 이벤트
            holder.binding.itemChattingRoomForcedExitCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("forcedExit", "forcedExit 체크박스 상태" + memberList[position] +" : "+ isChecked )
                if(isChecked){
                    //체크 기록
                    mcheckboxList[position] = true
                    //프로필 불투명하게
                    holder.binding.itemChattingRoomForcedExitImageIv.alpha = 1.0F //alpha값은 0.0(투명)~1.0(불투명)으로 존재
                    //이름 굵게 - 폰트변경
                    holder.binding.itemChattingRoomForcedExitTv.typeface = ResourcesCompat.getFont(holder.binding.root.context, R.font.spoqa_bold)
                    mForcedExitMemberList.add(memberList[position])
                }else{
                    mcheckboxList[position] = false
                    holder.binding.itemChattingRoomForcedExitImageIv.alpha = 0.5F
                    holder.binding.itemChattingRoomForcedExitTv.typeface = ResourcesCompat.getFont(holder.binding.root.context, R.font.spoqa_medium)
                    mForcedExitMemberList.remove(memberList[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = memberList.size

    // Item 하나에 대한 뷰를 바인딩 하고 있는 역할
    inner class ViewHolder(val binding: ItemChattingRoomForcedExitListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(member : MemberData, position: Int){
            Log.d("forcedExit", "체크박스리스트"+ mcheckboxList)
            if(position >= mcheckboxList.size) //뷰가 바인딩 될 때, 체크박스리스트에 객체를 하나씩 추가해준다.
                mcheckboxList.add(position, false)
            binding.itemChattingRoomForcedExitCheckBox.isChecked = mcheckboxList[position] //체크 상태 유지
            Glide.with(binding.root.context).load(member.profileImgUrl).into(binding.itemChattingRoomForcedExitImageIv)
            binding.itemChattingRoomForcedExitTv.text = member.nickName
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mcheckboxList = getForcedExitCheckList()
        Log.d("forcedExit", "onAttachedToRecyclerView : "+mcheckboxList)
    }
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        saveForcedExitCheckList(mcheckboxList)
        Log.d("forcedExit", "onDetachedFromRecyclerView : "+mcheckboxList)
    }

}