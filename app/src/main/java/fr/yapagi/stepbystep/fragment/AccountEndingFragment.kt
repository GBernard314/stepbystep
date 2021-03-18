package fr.yapagi.stepbystep.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.DashboardActivity
import fr.yapagi.stepbystep.MainActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.data.Goal
import fr.yapagi.stepbystep.data.User
import fr.yapagi.stepbystep.databinding.FragmentAccountEndingBinding
import fr.yapagi.stepbystep.databinding.FragmentAccountLandingBinding
import fr.yapagi.stepbystep.network.Authenticator
import fr.yapagi.stepbystep.network.DataListener
import fr.yapagi.stepbystep.network.Database

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
            val sharedPreferences = activity?.getSharedPreferences(AccountNameFragment.APP_PREFS, Context.MODE_PRIVATE)

            val auth = Authenticator()
            val db = Database()
            auth.getUID()?.let {
                db.getUser(it, object : DataListener {
                    override fun onSuccess(data: Any?) {
                        val user: User = data as User
                        user.gender = sharedPreferences?.getString(AccountGenderFragment.USER_GENDER, "")
                        user.username = sharedPreferences?.getString(AccountNameFragment.USER_NAME, user.username)
                        user.height = sharedPreferences?.getString(AccountSizeFragment.USER_SIZE, "170")?.toInt()
                        user.weight = sharedPreferences?.getString(AccountWeightFragment.USER_WEIGHT, "0")?.toFloat()
                        db.updateUser(it, user, object: DataListener{
                            override fun onSuccess(data: Any?) {}
                            override fun onStart() {}
                            override fun onFailure(error: String) {}
                        })

                        val goal = Goal()
                        goal.user_id = it
                        goal.intensity = sharedPreferences?.getString(AccountIntensityFragment.USER_INTENSITY, "")
                        goal.target_weight = sharedPreferences?.getString(AccountWeightGoalFragment.USER_WEIGHT, "0")?.toFloat()
                        db.createGoal(goal)


                        val intent = Intent(activity?.applicationContext, DashboardActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    override fun onStart() {}
                    override fun onFailure(error: String) {}
                })
            }
        }
    }
}