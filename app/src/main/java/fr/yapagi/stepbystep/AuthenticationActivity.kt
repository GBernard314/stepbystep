package fr.yapagi.stepbystep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.yapagi.stepbystep.adapter.AuthenticationPagerAdapter
import fr.yapagi.stepbystep.databinding.ActivityAuthenticationBinding

private lateinit var binding: ActivityAuthenticationBinding
class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.viewPager.adapter = AuthenticationPagerAdapter(this, supportFragmentManager)
    }


}