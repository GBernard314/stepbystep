package fr.yapagi.stepbystep

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import fr.yapagi.stepbystep.adapter.AccountActivityAdapter
import fr.yapagi.stepbystep.databinding.ActivityAccountBinding
import fr.yapagi.stepbystep.transformation.DepthPageTransformer

private lateinit var binding: ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = AccountActivityAdapter(this, supportFragmentManager)
        binding.viewPager.setPageTransformer(DepthPageTransformer())


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId){
                R.id.page_1 -> {
                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    startActivity(intent)
                }
                R.id.page_2 -> {
                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    startActivity(intent)
                }
                R.id.page_3 -> {
                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public fun setProgressBarValue(value: Int){
        binding.progressBar2.setProgress(value, true)
    }




}