package fr.yapagi.stepbystep

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import fr.yapagi.stepbystep.map.MapActivity
import fr.yapagi.stepbystep.timer.TimerActivity
import fr.yapagi.stepbystep.tools.Tools


class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        lateinit var locationManager: LocationManager
        lateinit var connectionManager: ConnectivityManager
        lateinit var wifiManager: WifiManager

        val pieChart = findViewById<PieChart>(R.id.chart)
        val NoOfEmp = ArrayList<PieEntry>()
        val tools = Tools()

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

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TMP>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        val tmpTimer = findViewById<CardView>(R.id.cardView6)
        tmpTimer.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val tmpMap = findViewById<CardView>(R.id.activity)
        tmpMap.setOnClickListener{
            locationManager   = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            wifiManager       = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            if(tools.isPermissionsGranted(this) &&
                tools.isGPSEnable(locationManager, this) &&
                tools.isNetworkEnable(connectionManager, wifiManager, this)
            ) {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
            else{
                tools.askForPermissions(this)
            }
        }
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TMP>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
}