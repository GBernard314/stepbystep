package fr.yapagi.stepbystep.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.ActivityMapBinding
import fr.yapagi.stepbystep.routing.PathSettings
import fr.yapagi.stepbystep.routing.RoutingActivity
import retrofit.Call
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

@SuppressLint("LogNotTimber")
class MapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {
    lateinit var binding: ActivityMapBinding

    //MAP
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var style: Style
    private var isFollowingUser = false

    //ROUTING
    var navigationMapRoute: NavigationMapRoute? = null
    var currentRoute: DirectionsRoute? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MAP
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync { mapboxMap ->
            MapActivity.mapboxMap = mapboxMap
            initMapSettings()
        }

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
        binding.mBtnFindPath.setOnClickListener {
            val intent = Intent(this, RoutingActivity::class.java)
            startActivityForResult(intent, FIND_PATH_CODE)
        }
    }
    private fun getRoute(originPoint: Point, endPoint: Point) {
        NavigationRoute.builder(this) //1
            .accessToken(Mapbox.getAccessToken()!!) //2
            .origin(originPoint) //3
            .destination(endPoint) //4
            .build() //5
            .getRoute(object : Callback<DirectionsResponse>,
                retrofit2.Callback<DirectionsResponse> { //6

                override fun onFailure(t: Throwable?) {
                    Log.d("MainActivity", t?.localizedMessage.toString())
                }

                override fun onResponse(
                    response: Response<DirectionsResponse>?,
                    retrofit: Retrofit?
                ) {
                    if (navigationMapRoute != null) {
                        navigationMapRoute?.updateRouteVisibilityTo(false)
                    } else {
                        navigationMapRoute = NavigationMapRoute(binding.map, mapboxMap, null)
                    }

                    currentRoute = response?.body()?.routes()?.first()
                    if (currentRoute != null) {
                        navigationMapRoute?.addRoute(currentRoute)
                    }
                }

                override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable?) {
                    Log.d("MainActivity", t?.localizedMessage.toString())
                }

                override fun onResponse(
                    call: retrofit2.Call<DirectionsResponse>,
                    response: retrofit2.Response<DirectionsResponse>
                ) {
                    if (navigationMapRoute != null) {
                        navigationMapRoute?.updateRouteVisibilityTo(false)
                    } else {
                        navigationMapRoute = NavigationMapRoute(binding.map, mapboxMap, null)
                    }

                    currentRoute = response.body()?.routes()?.first()
                    if (currentRoute != null) {
                        navigationMapRoute?.addRoute(currentRoute)
                    }
                }
            })
    }



    //MAP FEATURES//
    private fun zoomOnUser(){
        mapboxMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(getCurrentLocation(),18.0)
        )
    }
    private fun addMarker(location: LatLng, name: String, color: Int){


/*
        //1) Create marker manager
        val symbolManager = SymbolManager(binding.map, mapboxMap, style)
        symbolManager.iconAllowOverlap = true

        //2) Marker click listener
        symbolManager.addClickListener { symbol ->
            Toast.makeText(this, "clicked " + symbol.textField, Toast.LENGTH_SHORT).show()
        }

        //3) Generate marker
        symbolManager.create(
            SymbolOptions()
                .withLatLng(location)
                .withIconImage("") //set the below attributes according to your requirements
                .withIconSize(1.5f)
                .withIconOffset(arrayOf(0f, -1.5f))
                .withZIndex(10)
                .withTextField(name)
                .withTextHaloColor("rgba(${Color.red(color)}, ${Color.green(color)}, ${Color.blue(color)}, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(arrayOf(0f, 1.5f))
                .setDraggable(false)
        )*/
    }



    //MAP SETTINGS//
    private fun initMapSettings(){
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            this.style = style

            enableLocationComponent()
        }
    }
    private fun enableLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            //1) Check for permission denied
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            //2) Enable auto location
            val locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(this, style)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
        } else { //Request permission
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {
        Toast.makeText(this, "Please, enable user location provider", Toast.LENGTH_LONG).show()
    }
    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent()
        } else {
            Toast.makeText(this, "Error : User location provider denied", Toast.LENGTH_LONG).show()
            finish()
        }
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
    override fun onMapReady(mapboxMap: MapboxMap) {}



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == FIND_PATH_CODE && data?.getSerializableExtra(WAYPOINTS) != null) {
            val pathSettings = data.getSerializableExtra(WAYPOINTS) as PathSettings

            //1) Reset overlay
            binding.map.invalidate()
            mapboxMap.clear()

            for(waypoint in pathSettings.waypoints){
                val wp = LatLng(waypoint.first.toDouble(), waypoint.second.toDouble())
                mapboxMap.addMarker(MarkerOptions().position(wp))
            }
        }
    }



    companion object{
        lateinit var mapboxMap: MapboxMap
        const val FIND_PATH_CODE = 0
        const val WAYPOINTS      = "waypoints"

        @SuppressLint("MissingPermission")
        fun getCurrentLocation(): LatLng {
            val currentLoc = this.mapboxMap.locationComponent.lastKnownLocation

            return if(currentLoc != null){
                val userLat = currentLoc.latitude ?: 48.85750444119949
                val userLong= currentLoc.longitude ?: 2.3516698662490363
                LatLng(userLat, userLong)
            }
            else{
                LatLng(48.85750444119949, 2.3516698662490363)
            }
        }
    }
}