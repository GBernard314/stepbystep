package fr.yapagi.stepbystep.map

import android.annotation.SuppressLint
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.ActivityMapBinding


@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding

    //PROVIDERS
    private lateinit var locationManager:   LocationManager
    private lateinit var connectionManager: ConnectivityManager
    private lateinit var wifiManager:       WifiManager

    //MAP LOCATION
    private var isMapReady: Boolean = false
    private var isFollowingUser : Boolean = true
    private lateinit var permissionsManager: PermissionsManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mbMap: MapboxMap

        binding.map.getMapAsync { mapboxMap ->
            mbMap = mapboxMap
            mapboxMap.setStyle(
                Style.MAPBOX_STREETS
            ) { style -> //create this function & code further stuff there
                initMapStuff(style, mbMap)
                enableLocationComponent(style, mbMap)
                addMarkers(style, mbMap)
            }
        }
        binding.map.onCreate(savedInstanceState)
    }
    private fun addMarkers(style: Style, mbMap: MapboxMap){
        val symbols: List<Symbol> = ArrayList()

        var symbolManager = SymbolManager(binding.map, mbMap, style)
        symbolManager.iconAllowOverlap = true //your choice t/f
        //symbolManager.textAllowOverlap = false //your choice t/f

        symbolManager.addClickListener { symbol ->
            Toast.makeText(
                this,
                "clicked  " + symbol.textField.toLowerCase(),
                Toast.LENGTH_SHORT
            ).show()
        }

        symbolManager.create(
            SymbolOptions()
                .withLatLng(LatLng(43.123886, 5.927908))
                .withIconImage("") //set the below attributes according to your requirements
                .withIconSize(1.5f)
                .withIconOffset(arrayOf(0f, -1.5f))
                .withZIndex(10)
                .withTextField("my-marker")
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(arrayOf(0f, 1.5f))
                .setDraggable(false)
        )

    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style, mbMap: MapboxMap) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponent: LocationComponent = mbMap.locationComponent
            locationComponent.activateLocationComponent(this, loadedMapStyle)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(this@MapActivity, "location not enabled", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mbMap.getStyle { style -> initMapStuff(style, mbMap) }
                    } else {
                        Toast.makeText(this@MapActivity, "Location services not allowed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(this)
        }
    }
    @SuppressLint("MissingPermission")
    private fun initMapStuff(style: Style, mbMap: MapboxMap){
        binding.mBtnCurrentLocation.setOnClickListener {
            if (mbMap.locationComponent
                    .lastKnownLocation != null
            ) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                mbMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            mbMap.locationComponent.lastKnownLocation!!.latitude,
                            mbMap.locationComponent.lastKnownLocation!!.longitude
                        ),
                        14.0
                    )
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
    }


/*
        //Application ID needed for map API (Open Street Map)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        //BTN
        binding.mBtnCurrentLocation.setOnClickListener {
            isFollowingUser = !isFollowingUser
            if(isFollowingUser){
                zoomOnUser()
                binding.mBtnCurrentLocation.text = "X"
            }
            else{
                binding.mBtnCurrentLocation.text = "O"
            }
        }
        binding.mBtnReload.setOnClickListener {
            //Check for providers
            if(isGPSEnable()){
                binding.mBtnReload.visibility = View.GONE
                binding.mBtnCurrentLocation.visibility = View.VISIBLE
                binding.mHideView.visibility = View.GONE
                binding.mBtnFindPath.visibility = View.VISIBLE

                enableAutoRequestLocation()
            }
            else
            {
                waitForGPS()
            }
        }
        binding.mBtnFindPath.setOnClickListener {
            val intent = Intent(this, RoutingActivity::class.java)
            startActivityForResult(intent, FIND_PATH_CODE)
        }
    }*/


/*
    override fun onResume() {
        super.onResume()

        if(!isMapReady){
            initMapSettings()
        }
    }*/



    //MAP LOCATION//
    /*fun zoomOnUser(){
        binding.mapView.controller.setZoom(20)

        //1) Reset overlay
        binding.mapView.overlay.clear()
        binding.mapView.invalidate()

        //2) Set point & center view on it
        val point = GeoPoint(getCurrentLocation().latitude, getCurrentLocation().longitude)
        binding.mapView.controller.setCenter(point)

        //3) Generate marker
        val marker = Marker(binding.mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        binding.mapView.overlays.add(marker)
    } //Center & Zoom in on current user location



    //MAP SETTINGS (PERMISSION/NETWORK/GPS)//
    private fun initMapSettings(){

        //1) Init map providers
        locationManager   = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        wifiManager       = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //2) Check for permissions granted & providers enable
        isMapReady = isAccessAuthorized()

        //3) Enable location update
        enableAutoRequestLocation()

        //4) Init map default settings
        binding.mapView.setBuiltInZoomControls(true)                                           //Zoom settings
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.minZoomLevel = 4.0
        binding.mapView.controller.setZoom(4)
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)                   //Tile save
        binding.mapView.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS) //Map colors
        binding.mapView.isHorizontalMapRepetitionEnabled = false                               //Map view lock (no mosaic)
        binding.mapView.isVerticalMapRepetitionEnabled = false
        binding.mapView.setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
        )
        binding.mapView.setScrollableAreaLimitLongitude(
                MapView.getTileSystem().minLongitude,
                MapView.getTileSystem().maxLongitude,
                0
        )
    }               //Init map providers & settings
    @SuppressLint("MissingPermission")
    private fun enableAutoRequestLocation(){
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0F,
            object : LocationListener {
                override fun onProviderDisabled(provider: String) {
                    locationManager.removeUpdates(this)
                    waitForGPS()
                }  //Call in first if providers disable
                override fun onLocationChanged(location: Location) {
                    currentLocation = location
                    if(isFollowingUser){
                        zoomOnUser()
                    }

                } //Update currentLocation each movement detected

                override fun onProviderEnabled(provider: String) {
                    super.onProviderEnabled(provider)
                    zoomOnUser()
                }
            }
        )
    }     //Get location each time the user move
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
        ActivityCompat.requestPermissions(this, permissions,1)
        Log.d("maps", "Permissions ask")

        return isPermissionGranted() //Granted -> True / Denied -> False
    }   //Ask user for permissions
    private fun waitForGPS(){
        binding.mBtnReload.visibility = View.VISIBLE
        binding.mBtnCurrentLocation.visibility = View.GONE
        binding.mHideView.visibility = View.VISIBLE
        binding.mBtnFindPath.visibility = View.GONE
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == FIND_PATH_CODE && data?.getSerializableExtra(WAYPOINTS) != null) {
            val pathSettings = data.getSerializableExtra(WAYPOINTS) as PathSettings

            //1) Reset overlay
            binding.mapView.overlays.clear()
            binding.mapView.invalidate()

            for(waypoint in pathSettings.waypoints){
                val marker = Marker(binding.mapView)
                marker.position = waypoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                binding.mapView.overlays.add(marker)
            }

            val updateRoadTask = UpdateRoadTask(this, binding.mapView, binding.mDistanceText, binding.mCaloriesText, binding.mActivityTimeText)
            updateRoadTask.execute(pathSettings.waypoints)
       }
    }



    companion object{
        private var currentLocation = Location("")
        const val FIND_PATH_CODE    = 0
        const val WAYPOINTS         = "waypoints"

        fun getCurrentLocation(): GeoPoint {
            return GeoPoint(currentLocation.latitude, currentLocation.longitude)
        }
    }*/
}