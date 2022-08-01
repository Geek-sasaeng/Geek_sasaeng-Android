package com.example.geeksasaeng.Home.Party.UpdateParty

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.Home.Party.CreateParty.DialogDt
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDtLayoutBinding
import com.example.geeksasaeng.databinding.DialogDtUpdateLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogDtUpdate : DialogFragment() {
    lateinit var binding: DialogDtUpdateLayoutBinding
    private var dialogDtUpdateClickListener: DialogDtUpdateClickListener? =null
    var dateString = ""
    var dateString2 = ""
    var timeString = ""
    var timeString2 = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogDtUpdateLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initClickListener()
        return binding.root
    }

    interface DialogDtUpdateClickListener{
        //TODO: 뷰모델 이용하면서 사실 여기서 매개변수로 안넘겨줘도 ACTIVITY에서 값 알 수 있어..
        //TODO: 근데 이걸 하는 이유는 정보 갱신을 위함.
        fun onDtClicked(dt:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogDtUpdateClickListener = requireParentFragment() as DialogDtUpdateClickListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogDtUpdateClickListener = null
    }

    private fun initData(){
        //정보 받아와서 설정해주기
        val orderTime = arguments?.getString("DT")
        Log.d("DT", orderTime+"받아옴")
        binding.dialogDateUpdateDateTv.text = "${orderTime?.substring(5, 7)}월 ${orderTime?.substring(8, 10)}일"
        dateString2 = "${orderTime?.substring(0, 10)}"
        timeString2 = "${orderTime?.substring(11, 19)}"
        binding.dialogDateUpdateTimeTv.text = "${orderTime?.substring(11, 13)}시 ${orderTime?.substring(14, 16)}분"
    }

    override fun onResume() {
        super.onResume()
        //<방법2: 지정해둔 dp크기로로 다이얼로그 기 조정>
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initClickListener(){

        binding.dialogDateUpdateDateTv.setOnClickListener { //날짜 정보
            //DatePickerApplicationClass
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                dateString = "${month+1}월 ${dayOfMonth}일"

                //TODO: 이 부분 DateFormat이용할 수 있으면 좋을 텐뎅.. 아직 모르겠음
                var sMonth : String = (month+1).toString()
                var sDay : String = dayOfMonth.toString()
                if(month.toString().length==1){
                    sMonth = "0"+ sMonth
                }
                if(dayOfMonth.toString().length==1){
                    sDay = "0"+sDay
                }
                dateString2 = "${year}-" + sMonth + "-" + sDay


                Log.d("dialog", dateString)
                binding.dialogDateUpdateDateTv.text = dateString
            }
            var dialog = DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = System.currentTimeMillis()
            dialog.show()
        }

        binding.dialogDateUpdateTimeTv.setOnClickListener { //시간 정보
            val cal = Calendar.getInstance()   //캘린더뷰 만들기
            //TimePicker
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                timeString = "${hourOfDay}시 ${minute}분"
                var sHour : String = hourOfDay.toString()
                var sMinute : String = minute.toString()
                if(minute.toString().length==1){
                    sMinute = "0"+ sMinute
                }
                if(hourOfDay.toString().length==1){
                    sHour = "0"+sHour
                }
                timeString2 = sHour+":"+sMinute+":00" //TODO:임의로 그냥 초는 00초로 해두기로 함


                binding.dialogDateUpdateTimeTv.text = timeString
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()

        }

        //완료 버튼
        binding.dialogDateUpdateBtn.setOnClickListener {
            dialogDtUpdateClickListener?.onDtClicked(dateString2 +" "+ timeString2)
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }

}