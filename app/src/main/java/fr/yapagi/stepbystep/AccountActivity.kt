package fr.yapagi.stepbystep

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.get
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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public fun setProgressBarValue(value: Int){
        binding.progressBar2.setProgress(value, true)
    }




}