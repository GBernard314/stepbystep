package fr.yapagi.stepbystep

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import fr.yapagi.stepbystep.databinding.ActivityDashboardBinding
import fr.yapagi.stepbystep.timer.TimerActivity

private lateinit var binding: ActivityDashboardBinding;


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater);
        //setContentView(R.layout.activity_dashboard)
        setContentView(binding.root)




        val pieChart = findViewById<PieChart>(R.id.chart)
        
        val NoOfStep = ArrayList<PieEntry>()


        val walked = 5000.toFloat()
        val goal = (15000 - walked)


        /*
            Start of pie chart
         */
        NoOfStep.add(PieEntry(walked, "steps"))
        NoOfStep.add(PieEntry(goal, "needed"))
        val dataSet = PieDataSet(NoOfStep, "Number Of Employees")

        dataSet.setColors(intArrayOf(R.color.step_blue, R.color.background_grey), applicationContext)

        dataSet.setDrawIcons(false)
        dataSet.setDrawValues(false)
        //dataSet.sliceSpace = 3f
        //dataSet.iconsOffset = MPPointF(0F, 40F)
        //dataSet.selectionShift = 5f

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
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

        /*
            End of pie chart
         */

        /*
            Start of bar chart
         */

        val barChart: BarChart = findViewById(R.id.activity);

        /*
            changing display of barchart
            0 = steps count
            1 = calories
         */

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

        var actDisplay = 0
        //var bardataset = BarDataSet(steps, "Cells")
        binding.actLogo.setOnClickListener {

            /*
                Managing the change of data to display
             */
            if (actDisplay == 0){
                val bardataset =  BarDataSet(steps, "steps")
                activityTracking(bardataset, barChart, actDisplay)

                actDisplay = 1
            } else {
                val bardataset =  BarDataSet(calories, "Calories");
                activityTracking(bardataset, barChart, actDisplay)

                actDisplay = 0
            }
        }

        /*
            End of bar chart
         */



        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TMP>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        val tmpMap = findViewById<CardView>(R.id.cardView6)
        tmpMap.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TMP>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }

    fun activityTracking(barDataSet: BarDataSet, barChart: BarChart, state: Int){
        // Changing name of card
        if (state == 0){
            binding.actText.text = "Activity : steps"
        } else {
            binding.actText.text = "Activity : Calories"
        }
        val dataB = BarData(barDataSet);
        barChart.data = dataB; // set the data and list of labels into chart
        //barChart.setDescription("Set Bar Chart Description Here");  // set the description
        barChart.legend
        barDataSet.color = R.color.activity_yellow
        //barChart.xAxis.valueFormatter = LabelFormatter(days)
        barChart.axisLeft.setDrawLabels(false);
        barChart.axisRight.setDrawLabels(false);
        barChart.xAxis.setDrawLabels(false);

        barChart.legend.isEnabled = false;   // Hide the legend
        barChart.axisRight.setDrawGridLines(false);
        barChart.axisLeft.setDrawGridLines(false);
        barChart.xAxis.setDrawGridLines(false);
        barChart.description.isEnabled = false;
        barChart.animateXY(500, 500)
    }

    class LabelFormatter(private val mLabels: ArrayList<String>) : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return mLabels[value.toInt()]
        }

    }
}