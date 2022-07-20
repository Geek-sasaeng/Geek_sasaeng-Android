package com.example.geeksasaeng.Home.CreateParty

import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDtLayoutBinding
import java.util.*

class DialogDt : DialogFragment() {
    lateinit var binding: DialogDtLayoutBinding
    private var dialogDtNextClickListener: DialogDtNextClickListener? =null
    var dateString = ""
    var timeString = ""
    var orderNow : Boolean = false

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

    override fun onResume() {
        super.onResume()

        //<방법1: 디바이스 크기에 비례해서 다이얼로그 크기 조정>
        /*val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params : ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams*/

        //<방법2: 지정해둔 dp크기로로 다이얼로그 기 조정>
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogDtNextClickListener{
        fun onDtClicked(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogDtNextClickListener = activity as DialogDtNextClickListener
    }

    override fun onDetach() {
        super.onDetach()
        //frag-> activity 정보전달
        dialogDtNextClickListener?.onDtClicked(dateString+ " " + timeString)
        dialogDtNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

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

            //frag-> activity 정보전달
            dialogDtNextClickListener?.onDtClicked(dateString+ " " + timeString)


            //다음 다이얼로그 띄우기
            val dialogNum = DialogNum()
            dialogNum.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }

}