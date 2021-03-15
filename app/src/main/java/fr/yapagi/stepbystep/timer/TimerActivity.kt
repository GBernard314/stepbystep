package fr.yapagi.stepbystep.timer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.yapagi.stepbystep.databinding.TimerActivityBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: TimerActivityBinding
    private var isTimerOn: Boolean = false
    private var min:  Short = 0
    private var hour: Short = 0
    private var sec:  Short = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //BTNS
        binding.timerBtnPlay.setOnClickListener {
            play()
        }
        binding.timerBtnRestart.setOnClickListener {
            restart()
        }
        binding.timerBtnStop.setOnClickListener{
            stop()
        }
    }



    private fun play(){
        Log.d("timer", "Start")
        isTimerOn = true
        loadBtn(false, true, false)

        //1) Start counting coroutine
        GlobalScope.launch {
            while (isTimerOn) {      //While stop btn not used
                sec++                //Seconde
                if (sec >= 60) {     //Minute
                    min++
                    sec = 0

                    if (min >= 60) { //Hour
                        hour++
                        min = 0
                    }
                }

                //2) Update time in UI thread
                runOnUiThread {
                    //Display with good design
                    var time = ""
                    time = if(hour < 10) time + "0$hour:" else time + hour
                    time = if(min < 10)  time + "0$min:"  else time + min
                    time = if(sec < 10)  time + "0$sec"   else time + sec
                    binding.timerDisplay.text = time
                }
                delay(1000)
            }
        }
    }
    private fun restart(){
        Log.d("timer", "Restart")
        isTimerOn = false //Use to stop the current coroutine
        loadBtn(true, false, false)

        sec  = 0
        min  = 0
        hour = 0
        binding.timerDisplay.text = "0$hour:0$min:0$sec"
    }
    private fun stop(){
        Log.d("timer", "Stop")
        loadBtn(true, false,  true)

        isTimerOn = false
    }
    private fun loadBtn(btnPlay: Boolean, btnStop: Boolean, btnRestart: Boolean){
        binding.timerBtnPlay.visibility    = if(btnPlay)    View.VISIBLE else View.GONE
        binding.timerBtnRestart.visibility = if(btnRestart) View.VISIBLE else View.GONE
        binding.timerBtnStop.visibility    = if(btnStop)    View.VISIBLE else View.GONE
    }
}