package fr.yapagi.stepbystep

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent
import android.hardware.SensorEventListener;
import android.widget.Toast
import fr.yapagi.stepbystep.databinding.ActivityGyroscopeBinding;
import androidx.core.content.getSystemService
//import fr.yapagi.stepbystep.R.id.step_number

private lateinit var binding: ActivityGyroscopeBinding;

class GyroscopeActivity : AppCompatActivity(), SensorEventListener {



    private var sensorManager: SensorManager? = null
    //lateinit var gyroSensor: Sensor
    lateinit var gyroEventListener: SensorEventListener
    private var numberOfSteps = 0f
    private var running = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGyroscopeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_gyroscope)
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        running = true
        val gyroSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(gyroSensor == null) {
            Toast.makeText(this,"Il n'y a pas de gyroscope sur cet appareil",Toast.LENGTH_SHORT).show()
        }
        else {
            sensorManager?.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(gyroEventListener)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running) {
            numberOfSteps = event!!.values[0]
            var steps = numberOfSteps.toInt()
            binding.stepsNumber.text = ("$steps")
        }
    }

    /*private fun resetSteps() {
        binding.stepsNumber.setOnLongClickListener{
           // numberOfSteps = 0f
            //binding.stepsNumber.text = numberOfSteps.toString()
        }
    }*/
}