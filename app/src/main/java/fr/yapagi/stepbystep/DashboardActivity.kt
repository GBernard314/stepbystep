package fr.yapagi.stepbystep

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.MenuItem
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import fr.yapagi.stepbystep.databinding.ActivityDashboardBinding
import fr.yapagi.stepbystep.map.MapActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import androidx.core.app.ActivityCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import fr.yapagi.stepbystep.databinding.ActivityDashboardBinding
import fr.yapagi.stepbystep.timer.TimerActivity
import fr.yapagi.stepbystep.tools.Tools

private lateinit var binding: ActivityDashboardBinding;

private lateinit var binding: ActivityDashboardBinding;


class DashboardActivity : AppCompatActivity(), SensorEventListener {
    /**
     * for gyroscope
     */

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private var sensorManager: SensorManager? = null
    lateinit var gyroEventListener: SensorEventListener
    private var numberOfSteps = 0f
    private var running = true

    override fun onResume() {
        super.onResume()
        running = true
        val gyroSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(gyroSensor == null) {
            Toast.makeText(this,"Il n'y a pas de gyroscope sur cet appareil", Toast.LENGTH_SHORT).show()
        }
        else {
            sensorManager?.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if(running) {
            numberOfSteps = event!!.values[0]
            var steps = numberOfSteps.toInt()
            binding.nbSteps.text = ("$steps")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater);
        //setContentView(R.layout.activity_dashboard)
        setContentView(binding.root)


        /*
        if(ActivityCompat.checkSelfPermission(this.applicationContext, android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED){​​​​​
            Toast.makeText(this.applicationContext,"Please, authorize location & data permission to load your position", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this as Activity,arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),1)
        }​​​​​
        */

        // TODO check if permisison exists
        ActivityCompat.requestPermissions(this as Activity,arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),1)


        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        /************************************
         *                                  *
         *          Dummy data              *
         *                                  *
         ************************************/
        /**
         * YANIS
         * - For the steps number an Int is enough
         * - For the steps goal an Int is enough (it can be hard coded for now
         * - For the activity, you need to convert to to float (i don't know why, but you'll see)
         *
         * Further improvement would be
         * - the user picture
         * - the user name
         */
        /*
            Steps
         */
        val NoOfStep = ArrayList<PieEntry>()


        val walked = 5000.toFloat()
        val goal = (15000 - walked)



        /*
            activity tracking
         */
        val barChart: BarChart = findViewById(R.id.activity);

        val steps: ArrayList<BarEntry> = ArrayList()
        steps.add( BarEntry(0f, 1000f));
        steps.add( BarEntry(1f, 500f));
        steps.add( BarEntry(2f, 564f))
        steps.add( BarEntry(3f, 234f))
        steps.add( BarEntry(4f, 700f))
        steps.add( BarEntry(5f, 1500f))
        steps.add( BarEntry(5f, 1500f))

        val calories: ArrayList<BarEntry> = ArrayList()
        calories.add( BarEntry(0f, 10f));
        calories.add( BarEntry(1f, 5f));
        calories.add( BarEntry(2f, 5f))
        calories.add( BarEntry(3f, 2f))
        calories.add( BarEntry(4f, 7f))
        calories.add( BarEntry(5f, 15f))
        calories.add( BarEntry(5f, 15f))


        var listBarDataSet = listOf<BarDataSet>()
        listBarDataSet += BarDataSet(steps, "steps")
        listBarDataSet += BarDataSet(calories, "calories")


        /************************************
         *                                  *
         *          Dummy data              *
         *                                  *
         ************************************/


        /*
            Start of pie chart
         */

        val pieChart = findViewById<PieChart>(R.id.chart)

        NoOfStep.add(PieEntry(walked, "steps"))
        NoOfStep.add(PieEntry(goal, "needed"))
        val dataSet = PieDataSet(NoOfStep, "Number Of Employees")

        dataSet.setColors(intArrayOf(R.color.od_blue, R.color.background_grey), applicationContext)

        dataSet.setDrawIcons(false)
        dataSet.setDrawValues(false)

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        pieChart.setDrawEntryLabels(false)
        pieChart.setDrawMarkers(false)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        pieChart.data = data
        pieChart.setDrawSlicesUnderHole(false)
        pieChart.holeRadius = 70.toFloat()
        pieChart.highlightValues(null)
        pieChart.invalidate()
        pieChart.animateXY(1000, 1000)

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
        /*
            End of pie chart
         */






        /*
            Start of bar chart
         */


        /*
            Default values
         */
        var actDisplay = 0
        activityTracking(listBarDataSet, barChart, actDisplay)
        actDisplay = 1



        binding.actLogo.setOnClickListener {
            /*
                Managing the change of data to display
             */
            if (actDisplay == 0){
                val bardataset =  BarDataSet(steps, "steps")
                actDisplay = activityTracking(listBarDataSet, barChart, actDisplay)
            } else {
                val bardataset =  BarDataSet(calories, "Calories");
                actDisplay = activityTracking(listBarDataSet, barChart, actDisplay)
            }
        }
        val barChart: BarChart = findViewById(R.id.activity);

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add( BarEntry(0f, 1000f));
        entries.add( BarEntry(1f, 500f));
        entries.add( BarEntry(2f, 564f))
        entries.add( BarEntry(3f, 234f))
        entries.add( BarEntry(4f, 700f))
        entries.add( BarEntry(5f, 1500f))

        val bardataset =  BarDataSet(entries, "Cells");
        val days = ArrayList<String>()
        days.add("Mon")
        days.add("Tue")
        days.add("Wed")
        val act7 = listOf<Float>(1000f, 500f, 465f)

        val labels = ArrayList<String>()
        labels.add("2016");
        labels.add("2015");
        labels.add("2014");
        labels.add("2013");
        labels.add("2012");
        labels.add("2011");

        val dataB = BarData(bardataset);
        barChart.data = dataB; // set the data and list of labels into chart
        //barChart.setDescription("Set Bar Chart Description Here");  // set the description
        barChart.legend
        bardataset.color = R.color.activity_yellow
        barChart.animateY(1000);
        //barChart.xAxis.valueFormatter = LabelFormatter(days)
        //barChart.setDescription("");    // Hide the description
        barChart.axisLeft.setDrawLabels(false);
        barChart.axisRight.setDrawLabels(false);
        barChart.xAxis.setDrawLabels(false);

        barChart.legend.isEnabled = false;   // Hide the legend
        barChart.axisRight.setDrawGridLines(false);
        barChart.axisLeft.setDrawGridLines(false);
        barChart.xAxis.setDrawGridLines(false);
        barChart.description.isEnabled = false;

        /*
            End of bar chart
         */



        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TMP>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        val tmpTimer = findViewById<CardView>(R.id.cardView6)
        tmpTimer.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val tmpMap = findViewById<CardView>(R.id.cardView)
        tmpMap.setOnClickListener{
            var tools = Tools()
            if(tools.isPermissionAndProvidersEnable(this)){
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TMP>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }


    fun activityTracking(listDataSet: List<BarDataSet>, barChart: BarChart, state: Int): Int{
        // Changing name of card
        var newState = 0

        when (state) {
            0 -> {
                binding.actText.text = "Activity : steps"
                newState ++
            }
            1 -> {
                binding.actText.text = "Activity : calories"
                newState ++
            }
        }
        if (state == listDataSet.size - 1){
            newState = 0
        }


        val dataB = BarData(listDataSet.elementAt(state));
        barChart.data = dataB; // set the data and list of labels into chart
        //barChart.setDescription("Set Bar Chart Description Here");  // set the description
        barChart.legend
        listDataSet.elementAt(state).color = resources.getColor(R.color.od_yellow)



        barChart.setDrawGridBackground(false);
        barChart.xAxis.setDrawAxisLine(false);
        barChart.xAxis.setDrawGridLines(false);
        barChart.axisLeft.setDrawAxisLine(false);
        barChart.axisLeft.setDrawGridLines(false);
        barChart.axisRight.setDrawGridLines(false);
        barChart.axisRight.setDrawAxisLine(false);



        //barChart.xAxis.valueFormatter = LabelFormatter(days)
        barChart.axisLeft.setDrawLabels(false);
        barChart.axisRight.setDrawLabels(false);
        barChart.xAxis.setDrawLabels(false);

        barChart.legend.isEnabled = false;   // Hide the legend
        barChart.description.isEnabled = false;
        barChart.animateXY(500, 500)
        return newState
    }

    class LabelFormatter(private val mLabels: ArrayList<String>) : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return mLabels[value.toInt()]
        }

    }
}
