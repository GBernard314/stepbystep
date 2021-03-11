package fr.yapagi.stepbystep.fragment

import android.content.Context
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
        val sharedPreferences = activity?.getSharedPreferences(AccountGenderFragment.APP_PREFS, Context.MODE_PRIVATE)
        (activity as AccountActivity?)?.setProgressBarValue(80)

        binding.lightButton.setOnClickListener{
            binding.lightButton.setBackgroundColor(Color.CYAN)
            binding.mediumButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.intenseButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            sharedPreferences?.edit()?.putString(USER_INTENSITY, binding.lightButton.text.toString())?.commit()
        }

        binding.mediumButton.setOnClickListener{
            binding.lightButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.mediumButton.setBackgroundColor(Color.CYAN)
            binding.intenseButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            sharedPreferences?.edit()?.putString(USER_INTENSITY, binding.mediumButton.text.toString())?.commit()
        }

        binding.intenseButton.setOnClickListener{
            binding.lightButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.mediumButton.setBackgroundColor(Color.parseColor("#FDFDFD"))
            binding.intenseButton.setBackgroundColor(Color.CYAN)
            sharedPreferences?.edit()?.putString(USER_INTENSITY, binding.intenseButton.text.toString())?.commit()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val APP_PREFS = "app_prefs"
        const val USER_INTENSITY = "intensity"
    }
}