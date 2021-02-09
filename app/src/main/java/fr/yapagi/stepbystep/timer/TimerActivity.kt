package fr.yapagi.stepbystep.timer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.TimerActivityBinding

class TimerActivity : AppCompatActivity() {
    lateinit var binding: TimerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}