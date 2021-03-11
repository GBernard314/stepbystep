package fr.yapagi.stepbystep.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.DashboardActivity
import fr.yapagi.stepbystep.MainActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.FragmentAccountEndingBinding
import fr.yapagi.stepbystep.databinding.FragmentAccountLandingBinding

private lateinit var binding : FragmentAccountEndingBinding
class AccountEndingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountEndingBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(90)
        super.onViewCreated(view, savedInstanceState)

        binding.endButton.setOnClickListener{
            val intent = Intent(activity?.applicationContext, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}