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
import fr.yapagi.stepbystep.databinding.FragmentAccountGenderBinding


private lateinit var binding: FragmentAccountGenderBinding

class AccountGenderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountGenderBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(30)

        binding.maleButton.setOnClickListener{
            binding.maleButton.setBackgroundColor(Color.CYAN)
            binding.femaleButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.noBinaryButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
        }

        binding.femaleButton.setOnClickListener{
            binding.maleButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.femaleButton.setBackgroundColor(Color.CYAN)
            binding.noBinaryButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
        }

        binding.noBinaryButton.setOnClickListener{
            binding.maleButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.femaleButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.noBinaryButton.setBackgroundColor(Color.CYAN)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}