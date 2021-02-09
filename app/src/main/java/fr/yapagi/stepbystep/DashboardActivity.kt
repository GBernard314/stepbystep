package fr.yapagi.stepbystep

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val pieChart = findViewById<PieChart>(R.id.chart)
        val NoOfEmp = ArrayList<PieEntry>()


        val walked = 5000.toFloat()
        val goal = (15000 - walked)

        NoOfEmp.add(PieEntry(walked, "steps"))
        NoOfEmp.add(PieEntry(goal, "needed"))
        val dataSet = PieDataSet(NoOfEmp, "Number Of Employees")

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
    }
}