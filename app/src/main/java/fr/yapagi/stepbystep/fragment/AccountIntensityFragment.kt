package fr.yapagi.stepbystep.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.FragmentAccountGenderBinding
import fr.yapagi.stepbystep.databinding.FragmentAccountIntensityBinding

private lateinit var binding: FragmentAccountIntensityBinding
class AccountIntensityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountIntensityBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(80)

        binding.lightButton.setOnClickListener{
            binding.lightButton.setBackgroundColor(Color.CYAN)
            binding.mediumButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.intenseButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
        }

        binding.mediumButton.setOnClickListener{
            binding.lightButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.mediumButton.setBackgroundColor(Color.CYAN)
            binding.intenseButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
        }

        binding.intenseButton.setOnClickListener{
            binding.lightButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.mediumButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.intenseButton.setBackgroundColor(Color.CYAN)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}