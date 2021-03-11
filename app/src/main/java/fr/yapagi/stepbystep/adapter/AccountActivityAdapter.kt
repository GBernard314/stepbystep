package fr.yapagi.stepbystep.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.yapagi.stepbystep.fragment.*

class AccountActivityAdapter(
    activity: AppCompatActivity,
    fragmentManager: FragmentManager
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return AccountLandingFragment() //ChildFragment1 at position 0
            1 -> return AccountNameFragment()
            2 -> return AccountGenderFragment()
            3 -> return AccountSizeFragment()
            4 -> return AccountWeightFragment()
            5 -> return AccountWeightGoalFragment()
            6 -> return AccountIntensityFragment()
        }
        return AccountLandingFragment() //does not happen
    }
}