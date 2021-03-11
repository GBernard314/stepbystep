package fr.yapagi.stepbystep.tools

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import fr.yapagi.stepbystep.routing.ActivityDetail
import fr.yapagi.stepbystep.routing.PathSettings
import fr.yapagi.stepbystep.routing.UserDetails

@SuppressLint("LogNotTimber")
class Tools {

    //PERMISSIONS//
    private lateinit var locationManager: LocationManager
    private lateinit var connectionManager: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    lateinit var activityContext: Context
    private val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
            //Add permissions here
    )



    //DISTANCE / LAT//
    fun distanceToLat(distance: Float): Double {
        //Latitude: 1 deg = 110.574 km
        return distance * (1/110.574)
    }
    fun distanceToLong(distance: Float, latitude: Double): Double {
        //Longitude: 1 deg = 111.320*cos(latitude).toDeg() km
        val latInDeg = Math.toDegrees(kotlin.math.cos(latitude))
        return distance / (111.320*latInDeg)
    }
    fun latToDistance(latitude: Double): Float {
        return (latitude * 110.574).toFloat()
    }
    fun longToDistance(longitude: Double, latitude: Double): Float {
        return (longitude * 111.320 * Math.toDegrees(latitude)).toFloat()
    }



    //PERMISSIONS & PROVIDERS//
    fun isPermissionAndProvidersEnable(context: Context): Boolean{
        locationManager   = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        wifiManager       = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        activityContext   = context

        return if(isPermissionsGranted() &&
                    isGPSEnable(locationManager) &&
                    isNetworkEnable(connectionManager, wifiManager)
        ) {
            Log.d("maps", "Tools -> Permission OK")
            true
        }
        else{
            askForPermissions()
            false
        }
}                                           //Permissions & providers manager
    private fun isPermissionsGranted(): Boolean {
        //Check for each permissions register in compagnon object
        for(permission in permissions){
            if(ActivityCompat.checkSelfPermission(activityContext.applicationContext, permission) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activityContext.applicationContext,"Please, authorize location & data permission to load your position", Toast.LENGTH_SHORT).show()
                askForPermissions()
                return false
            }
        }

        Log.d("maps", "Tools -> Permission OK")
        return true
    }                                                            //Check for manifest permissions
    private fun isGPSEnable(locationManager: LocationManager): Boolean {
        return if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("maps", "GPS OK")
            true
        }
        else{
            Toast.makeText(activityContext,"Please, enable GPS", Toast.LENGTH_SHORT).show()
            Log.d("maps", "Tools -> Please, enable GPS")
            false
        }
    }                                     //Check for GPS provider enable
    private fun isNetworkEnable(connectionManager: ConnectivityManager, wifiManager: WifiManager): Boolean {
        val networkInfo = connectionManager.activeNetworkInfo
        return if((networkInfo != null && networkInfo.isConnected) || wifiManager.isWifiEnabled){ //Network service enable
            Log.d("maps", "Network OK")
            true
        }
        else{                                                                                     //Network service disable
            Toast.makeText(activityContext,"Please, enable WIFI or mobile data", Toast.LENGTH_SHORT).show()
            Log.d("maps", "Tools -> Please, enable WIFI or mobile data")
            false
        }
    } //Check for Network service enable
    private fun askForPermissions() {
        //Ask user for permissions in compagnon object
        ActivityCompat.requestPermissions(activityContext as Activity, permissions,1)
        Log.d("maps", "Permissions asked")
    }                                                                        //Ask user for permissions



    //DISTANCE / CALORIES//
    fun distanceToCalories(userDetails: UserDetails): PathSettings {
        //1) Calcul activity time
        val activityTime = userDetails.distance / userDetails.activitySelected.speed
        Log.d("tools", "Activity time : $activityTime")

        //2) Calcul BRM
        val brm = calculBrm(userDetails)
        Log.d("tools", "BRM : $brm")

        //3) Calcul calories
        val calories = ((brm / 24) * userDetails.activitySelected.met * activityTime).toInt()
        Log.d("tools", "Calories : $calories")

        return PathSettings(calories, activityTime, userDetails.distance, ArrayList())
    }
    fun caloriesToDistance(userDetails: UserDetails): PathSettings {
        //1) Calcul BRM (physical condition)
        val brm = calculBrm(userDetails)
        Log.d("tools", "BRM : $brm")

        //2) Calcul activity time
        val activityTime = userDetails.calories / ((brm / 24) * userDetails.activitySelected.met)
        Log.d("tools", "Activity time : " + (activityTime * 60))

        //3) Calcul distance
        val distance = userDetails.activitySelected.speed * activityTime
        Log.d("tools", "Distance : $distance")

        return PathSettings(userDetails.calories, activityTime, distance, ArrayList())
    }
    private fun calculBrm(userDetails: UserDetails): Float {
        //1) Set lite or full height according to sex
        var heightInfo: Int = userDetails.height
        if (userDetails.isLiteInfoSelected) {
            heightInfo = if (userDetails.isAFemale) 160 else 180
        }
        Log.d("tools", "Height : $heightInfo")

        //2) Calcul & return BRM
        return if (userDetails.isAFemale) {
            ((9.56 * userDetails.weight) + (1.85 * userDetails.height) - (4.68 * userDetails.age) + 655).toFloat() //BRM female formula
        } else {
            ((13.75 * userDetails.weight) + (5 * userDetails.height) - (6.76 * userDetails.age) + 66).toFloat()    //BRM male formula
        }
    }
    fun getTime(distance: Float, activitySelected: ActivityDetail): Float {
        return distance / activitySelected.speed
    }
}