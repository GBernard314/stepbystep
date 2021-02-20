package fr.yapagi.stepbystep.path_finder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.BuildConfig
import fr.yapagi.stepbystep.DashboardActivity
import fr.yapagi.stepbystep.databinding.ActivityPathFinderBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


class PathFinderActivity : AppCompatActivity() {
    lateinit var binding : ActivityPathFinderBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Application ID needed for map API (Open Street Map)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>PERMISSION TEST>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }



    override fun onResume() {
        super.onResume()

        //1) Init map view
        binding.mapView.setBuiltInZoomControls(true);           //Zoom controls
        binding.mapView.setMultiTouchControls(true);            //Multi touch controls
        binding.mapView.controller.setZoom(4);                  //Zoom (without = mosaïque)
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK) //Map view source

        //2) Set test point
        val point = GeoPoint(51, 0);    //London, UK
        binding.mapView.controller.setCenter(point);

        //3) Access map if permission granted && service enable
        if(isAccessAuthorized()){
            Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show()
        }
    }



    //MAP ACCESS (PERMISSION/NETWORK/GPS)//
    private fun isAccessAuthorized(): Boolean {
        //1) Get managers
        val locationManager: LocationManager       = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val connectionManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiManager: WifiManager               = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //2) Check permissions
        if(!isPermissionGranted() && !askForPermissions()){ //First time -> Ask user
            Toast.makeText(this,"Error : Please, authorize GPS and Network permission in settings", Toast.LENGTH_SHORT).show()
            return false
        }

        //3) Check for GPS & Network service
        if(!isGPSEnable(locationManager) || !isNetworkEnable(connectionManager, wifiManager)){
            return false
        }

        return true
    }                                                              //Manage Map view access
    private fun isGPSEnable(locationManager: LocationManager): Boolean {
        return if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("maps", "GPS OK")
            true
        }
        else{
            Toast.makeText(this,"ERROR : Please, enable GPS", Toast.LENGTH_SHORT).show()
            Log.d("maps", "ERROR : Please, enable GPS")
            false
        }
    }                                     //Check for GPS service enable
    private fun isNetworkEnable(connectionManager: ConnectivityManager, wifiManager: WifiManager): Boolean {
        val networkInfo = connectionManager.activeNetworkInfo
        return if((networkInfo != null && networkInfo.isConnected) || wifiManager.isWifiEnabled){ //Network service enable
            Log.d("maps", "Network OK")
            true
        }
        else{                                                                             //Network service disable
            Toast.makeText(this,"ERROR : Please, enable WIFI or mobile data", Toast.LENGTH_SHORT).show()
            Log.d("maps", "ERROR : Please, enable WIFI or mobile data")
            false
        }
    } //Check for Network service enable
    private fun isPermissionGranted(): Boolean {
        val locPermission       = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val networkPermission   = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
        val wifiPermission      = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)

        return if (locPermission       == PackageManager.PERMISSION_GRANTED && //Location
                   coarseLocPermission == PackageManager.PERMISSION_GRANTED && //Approx location
                   networkPermission   == PackageManager.PERMISSION_GRANTED && //Mobile data
                   wifiPermission      == PackageManager.PERMISSION_GRANTED    //WIFI
        ) {                                                 //PERMISSION GRANTED
            Log.d("maps", "Permissions Granted")
            true
        }
        else {                                              //PERMISSION DENIED
            Log.d("maps", "Permissions denied")
            false
        }
    }                                                             //Check for Permissions
    private fun askForPermissions(): Boolean {
        val permissions = arrayOf(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        ) //Permission list

        //Ask user
        ActivityCompat.requestPermissions(this, permissions,1);
        Log.d("maps", "Permissions ask")

        return isPermissionGranted() //Granted -> True / Denied -> False
    }                                                               //Ask user for permissions

   /* fun getLocation(): Location? {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
            Log.d("Network", "Network")
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    latitude = location.getLatitude()
                    longitude = location.getLongitude()
                }
            }
        }

        // If GPS enabled, get latitude/longitude using GPS Services
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                Log.d("GPS Enabled", "GPS Enabled")
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null) {
                        latitude = location.getLatitude()
                        longitude = location.getLongitude()
                    }
                }
            }
        return location
    }
*/


   /* private fun createRequest() {
        
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
    }*/
}