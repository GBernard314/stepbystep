package fr.yapagi.stepbystep.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import fr.yapagi.stepbystep.fragment.AccountLandingFragment
import fr.yapagi.stepbystep.fragment.AccountNameFragment

class AccountActivityAdapter(
    activity: AppCompatActivity,
    fragmentManager: FragmentManager
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return AccountLandingFragment() //ChildFragment1 at position 0
            1 -> return AccountNameFragment()
        }
        return AccountLandingFragment() //does not happen
    }
}