package fr.yapagi.stepbystep.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import fr.yapagi.stepbystep.data.User
import fr.yapagi.stepbystep.databinding.FragmentAccountNameBinding
import fr.yapagi.stepbystep.network.Authenticator
import fr.yapagi.stepbystep.network.DataListener
import fr.yapagi.stepbystep.network.Database

private lateinit var binding : FragmentAccountNameBinding

class AccountNameFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountNameBinding.inflate(inflater, container, false)
        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(20)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        val sharedPreferences = activity?.getSharedPreferences(APP_PREFS, MODE_PRIVATE)
        sharedPreferences?.edit()?.putString(USER_NAME, binding.etName.text.toString())?.commit()

        super.onStop()
    }


    companion object {

        const val APP_PREFS = "app_prefs"
        const val USER_NAME = "username"
    }
}