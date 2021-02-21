package fr.yapagi.stepbystep.path_finder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.BuildConfig
import fr.yapagi.stepbystep.databinding.ActivityPathFinderBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay


class PathFinderActivity : AppCompatActivity(), LocationListener {
    lateinit var binding : ActivityPathFinderBinding
    private lateinit var locationManager:   LocationManager
    private lateinit var connectionManager: ConnectivityManager
    private lateinit var wifiManager:       WifiManager
    private lateinit var currentLocation:   Location
    private var isFollowingEnable:          Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Application ID needed for map API (Open Street Map)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        //BTN
        binding.pfBtnCurrentLocation.setOnClickListener {
            isFollowingEnable = !isFollowingEnable
            if(isFollowingEnable){
                binding.pfBtnCurrentLocation.text = "O"
                binding.mapView.controller.setZoom(17)
                followUser()
            }
            else{
                binding.pfBtnCurrentLocation.text = "X"
            }
        }
    }



    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        initMapSettings() //Colors, position, bound, repetition...

        //1) Access map if permission granted && service enable
        if(isAccessAuthorized()){
            Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show()
        }

        //2) Enable location update
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0F,
                this
        )
    }



    //MAP LOCATION//
    override fun onLocationChanged(location: Location) {
        Log.d("maps", "update Latitude:" + location.latitude + ", Longitude:" + location.longitude)
        currentLocation = location

        if(isFollowingEnable){
            followUser()
        }
    } //Update currentLocation each movement detected
    private fun followUser(){
        val point = GeoPoint(currentLocation.latitude, currentLocation.longitude)
        binding.mapView.controller.setCenter(point)
    }                            //Center & zoom map view on current user location



    //MAP SETTINGS (PERMISSION/NETWORK/GPS)//
    private fun initMapSettings(){
        //1) Init map providers
        locationManager   = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        wifiManager       = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //2) Init map settings
        binding.mapView.setBuiltInZoomControls(true)                                           //Zoom settings
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(4)
        binding.mapView.minZoomLevel = 4.0
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)                   //Tile save
        binding.mapView.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS) //Map colors
        binding.mapView.isHorizontalMapRepetitionEnabled = false                               //Map view lock (no mosaic)
        binding.mapView.isVerticalMapRepetitionEnabled = false
        binding.mapView.setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
        );
        binding.mapView.setScrollableAreaLimitLongitude(
                MapView.getTileSystem().minLongitude,
                MapView.getTileSystem().maxLongitude,
                0
        )
    }               //Init map providers & settings
    private fun isAccessAuthorized(): Boolean {
        //1) Check permissions
        if(!isPermissionGranted() && !askForPermissions()){ //First time -> Ask user
            Toast.makeText(this,"Error : Please, authorize GPS and Network permission in settings", Toast.LENGTH_SHORT).show()
            return false
        }

        //2) Check for GPS & Network service
        if(!isGPSEnable() || !isNetworkEnable()){
            return false
        }

        return true
    }  //Manage Map view access
    private fun isGPSEnable(): Boolean {
        return if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("maps", "GPS OK")
            true
        }
        else{
            Toast.makeText(this,"ERROR : Please, enable GPS", Toast.LENGTH_SHORT).show()
            Log.d("maps", "ERROR : Please, enable GPS")
            false
        }
    }         //Check for GPS service enable
    private fun isNetworkEnable(): Boolean {
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
    }     //Check for Network service enable
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
    } //Check for Permissions
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
    }   //Ask user for permissions



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