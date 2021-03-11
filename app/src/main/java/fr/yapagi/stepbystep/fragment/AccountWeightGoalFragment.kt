package fr.yapagi.stepbystep.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.super_rabbit.wheel_picker.WheelAdapter
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.FragmentAccountWeightBinding
import fr.yapagi.stepbystep.databinding.FragmentAccountWeightGoalBinding

private lateinit var binding : FragmentAccountWeightGoalBinding
class AccountWeightGoalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountWeightGoalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(70)
        val picker = binding.numberPicker;
        picker.setWheelItemCount(5)
        picker.setSelectedTextColor(R.color.colorPrimary)
        picker.setUnselectedTextColor(R.color.color_26_gray)
        picker.scrollTo(60)
        picker.setAdapter(PickerAdapter())
        super.onViewCreated(view, savedInstanceState)
    }

    class PickerAdapter : WheelAdapter {
        override fun getValue(position: Int): String {
            if (position in 1..9)
                return "0$position"
            if (position < 1)
                return "0"

            return position.toString()
        }

        override fun getMaxIndex(): Int {
            return 250
        }

        override fun getMinIndex(): Int {
            return 0
        }

        override fun getPosition(vale: String): Int {
            return when (vale) {
                "00" -> 0
                "01" -> 1
                "02" -> 2
                "03" -> 3
                "04" -> 4
                "05" -> 5
                "06" -> 6
                "07" -> 7
                "08" -> 8
                "09" -> 9
                else -> vale.toInt()
            }
        }

        override fun getTextWithMaximumLength(): String {
            return "00"
        }
    }
}