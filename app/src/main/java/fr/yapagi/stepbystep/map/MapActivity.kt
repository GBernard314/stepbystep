package fr.yapagi.stepbystep.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.ViewAnimator
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
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.ActivityMapBinding
import fr.yapagi.stepbystep.routing.ActivityDetail
import fr.yapagi.stepbystep.routing.PathSettings
import fr.yapagi.stepbystep.routing.RoutingActivity
import fr.yapagi.stepbystep.routing.UserDetails
import fr.yapagi.stepbystep.tools.Tools
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

@SuppressLint("LogNotTimber")
class MapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {
    lateinit var binding: ActivityMapBinding

    //MAP
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var style: Style

    //ROUTING
    private var navigationMapRoute: NavigationMapRoute? = null
    private var roads: ArrayList<DirectionsRoute> = ArrayList()
    private var pathSettings = PathSettings(0, 0f, 0f, ArrayList())
    private var userDetails = UserDetails(0, 0, 0, ActivityDetail("", 0f, 0f), false, false, 0f, 0)



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
            zoomOnUser()
        }
        binding.mBtnFindPath.setOnClickListener {
            val intent = Intent(this, RoutingActivity::class.java)
            startActivityForResult(intent, FIND_PATH_CODE)
        }
    }



    //MAP FEATURES//
    private fun zoomOnUser(){
        mapboxMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(getCurrentLocation(),17.0)
        )
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



    //ROUTING//
    private fun getRoute(originPoint: Point, endPoint: Point) {
        NavigationRoute.builder(this)
            .accessToken(Mapbox.getAccessToken().toString())
            .origin(originPoint)
            .destination(endPoint)
            .profile("walking")
            .build()
            .getRoute(object : Callback<DirectionsResponse>,
                    retrofit2.Callback<DirectionsResponse> {

                override fun onFailure(t: Throwable?) {
                    Toast.makeText(applicationContext, t?.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
                override fun onResponse(response: Response<DirectionsResponse>, retrofit: Retrofit?) {}
                override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable?) {
                    Toast.makeText(applicationContext, t?.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: retrofit2.Call<DirectionsResponse>, response: retrofit2.Response<DirectionsResponse>) {

                    //1) Test for road data back
                    if (response.body() == null) {
                        Log.e("maps", "No routes found, make sure you set the right user and access token.")
                        Toast.makeText(applicationContext, "User or Token problem, please contact support", Toast.LENGTH_LONG).show()
                        return
                    } else if (response.body()?.routes()?.size!! < 1) {
                        Log.e("maps", "No routes found")
                        Toast.makeText(applicationContext, "No route found", Toast.LENGTH_LONG).show()
                        return
                    }

                    //2) Save data
                    roads.add(response.body()!!.routes()[0])

                    //3) Display on map
                    if(roads.size > 2) {
                        displayRoadOnMap()
                    }
                }
            })
    }
    private fun displayRoadOnMap(){

        //1) Reset map road & markers
        if (navigationMapRoute != null) {
            navigationMapRoute!!.removeRoute()
        } else {
            navigationMapRoute = NavigationMapRoute(null, binding.map, mapboxMap, R.style.NavigationMapRoute)
        }

        //2) Display road
        navigationMapRoute!!.addRoutes(roads)

        //3) Save roads data
        val tools = Tools()
        pathSettings.calorie = 0
        pathSettings.distance = 0f
        pathSettings.activityTime = 0f

        userDetails.distance = 0f
        for(road in roads){

            //Update user details with real information generated
            userDetails.distance += road.distance()!!.toFloat()

            pathSettings.distance += road.distance()!!.toFloat()
            pathSettings.activityTime += road.duration()!!.toFloat()
        }

        pathSettings.calorie += tools.distanceToCalories(userDetails).calorie

        binding.mActivityTimeText.visibility = View.VISIBLE
        binding.mActivityTimeText.text       = "${pathSettings.activityTime/100} min"

        binding.mCaloriesText.visibility     = View.VISIBLE
        binding.mCaloriesText.text           = "${pathSettings.calorie/1000} kcal"

        binding.mDistanceText.visibility     = View.VISIBLE
        binding.mDistanceText.text           = "${pathSettings.distance/1000} km"

        zoomOnUser()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == FIND_PATH_CODE && data?.getSerializableExtra(WAYPOINTS) != null) {
            val pathSettings = data.getSerializableExtra(WAYPOINTS) as PathSettings
            this.userDetails = data.getSerializableExtra(USER_DETAILS) as UserDetails

            //1) Reset overlay
            binding.map.invalidate()
            mapboxMap.clear()

            //2) Get routes
            if(pathSettings.waypoints.size > 1){
                val titles = ArrayList<String>()
                titles.add("Départ/Arrivé")
                titles.add("1")
                titles.add("2")
                for(numWaypoint in 0..2){
                    val wp = LatLng(pathSettings.waypoints[numWaypoint].first.toDouble(), pathSettings.waypoints[numWaypoint].second.toDouble())
                    mapboxMap.addMarker(MarkerOptions().position(wp).title(titles[numWaypoint]))

                    val latStartPoint  = pathSettings.waypoints[numWaypoint].first.toDouble()
                    val longStartPoint = pathSettings.waypoints[numWaypoint].second.toDouble()

                    val latFinalPoint  = pathSettings.waypoints[numWaypoint+1].first.toDouble()
                    val longFinalPoint = pathSettings.waypoints[numWaypoint+1].second.toDouble()

                    getRoute(Point.fromLngLat(longStartPoint, latStartPoint), Point.fromLngLat(longFinalPoint, latFinalPoint))
                }
            }
        }
    }



    companion object{
        lateinit var mapboxMap: MapboxMap
        const val FIND_PATH_CODE = 0
        const val WAYPOINTS      = "waypoints"
        const val USER_DETAILS      = "user_details"

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