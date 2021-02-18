package fr.yapagi.stepbystep.path_finder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.BuildConfig
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import fr.yapagi.stepbystep.databinding.ActivityPathFinderBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.config.Configuration


class PathFinderActivity : AppCompatActivity() {
    lateinit var binding : ActivityPathFinderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    override fun onResume() {
        super.onResume()

        binding.mapView.setBuiltInZoomControls(true);
        binding.mapView.setMultiTouchControls(true);

        binding.mapView.controller.setZoom(8);
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        val point = GeoPoint(51, 0);  // London, UK
        binding.mapView.controller.setCenter(point);
    }



    private fun createRequest() {


        val queue = Volley.newRequestQueue(this)
        val coordinate = "13.388860,52.517037;13.397634,52.529407;13.428555,52.523219"
        val tab = arrayOf( arrayOf(8.681495F, 49.41461F), arrayOf(8.686507F, 49.41943F), arrayOf(8.687872F, 49.420318F) )
        val url = "https://router.project-osrm.org/route/v1/driving/$coordinate?overview=false"

        val request = StringRequest(
                Request.Method.GET,
                url,
                Response.Listener<String> { response ->
                    Log.d("maps", response)
                },
                Response.ErrorListener { error ->
                    Log.d("maps", error.message.toString())
                }
        )

        queue.add(request)
    }
}