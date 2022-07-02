package com.example.geeksasaeng

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.databinding.DialogDtLayoutBinding
import java.util.*

class DialogDt : DialogFragment() {
    lateinit var binding: DialogDtLayoutBinding
    var dateString = ""
    var timeString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogDtLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initClickListener()
        return binding.root
    }


    //최초에만 실행시키기 위해??
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cal = Calendar.getInstance()   //캘린더뷰 만들기
        //TimePicker
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            timeString = "${hourOfDay}시 ${minute}분"
            binding.dateDialogTimeTv.text = timeString
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
        //DatePicker
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${month+1}월 ${dayOfMonth}일"
            Log.d("dialog", dateString)
            binding.dateDialogDateTv.text = dateString
        }
        DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun initClickListener(){
        binding.dateDialogDateTv.setOnClickListener { //날짜 정보
            //DatePickerApplicationClass
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${month+1}월 ${dayOfMonth}일"
                Log.d("dialog", dateString)
                binding.dateDialogDateTv.text = dateString
            }
            DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.dateDialogTimeTv.setOnClickListener { //시간 정보
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            //TimePicker
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                timeString = "${hourOfDay}시 ${minute}분"
                binding.dateDialogTimeTv.text = timeString
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()

        }

        binding.dateDialogNextBtn.setOnClickListener { //다음버튼

            //일단 테스트용용
            if(dateString!=""&&timeString!=""){
                Log.d("dialog", dateString)
                Log.d("dialog", timeString)

                val myFragment = CreatePartyFragment()
                var args = Bundle()
                args.putString("DateDialog", dateString + "  " +timeString)
                myFragment.arguments = args

                /*val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.myframe, myFragment, "counter")
                fragmentTransaction.commit()*/

                val dialogNum = DialogNum()
                dialogNum.show(parentFragmentManager, "CustomDialog")
            }else{
                Toast.makeText(requireContext(), "시간을 지정해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

}