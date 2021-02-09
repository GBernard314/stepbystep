package fr.yapagi.stepbystep.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.databinding.FragmentAccountLandingBinding

private lateinit var binding: FragmentAccountLandingBinding

class AccountLandingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountLandingBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(10)
        super.onViewCreated(view, savedInstanceState)
    }


}