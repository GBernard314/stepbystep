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