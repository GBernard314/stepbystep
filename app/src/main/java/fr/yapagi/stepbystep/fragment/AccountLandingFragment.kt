package fr.yapagi.stepbystep.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.AuthenticationActivity
import fr.yapagi.stepbystep.DashboardActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.data.User
import fr.yapagi.stepbystep.databinding.FragmentAccountLandingBinding
import fr.yapagi.stepbystep.network.Authenticator
import fr.yapagi.stepbystep.network.DataListener
import fr.yapagi.stepbystep.network.Database

private lateinit var binding: FragmentAccountLandingBinding

class AccountLandingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountLandingBinding.inflate(inflater, container, false)

        val auth = Authenticator()
        val db = Database()
        auth.getUID()?.let {
            db.getUser(it, object : DataListener {
                override fun onSuccess(data: Any?) {
                    val user: User = data as User
                    if(user.weight != 0.0F){
                        val intent = Intent(activity?.applicationContext, DashboardActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }

                override fun onStart() {}
                override fun onFailure(error: String) {}
            })
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AccountActivity?)?.setProgressBarValue(10)
        super.onViewCreated(view, savedInstanceState)

        binding.skipButton.setOnClickListener{
            val intent = Intent(activity?.applicationContext, DashboardActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }


}