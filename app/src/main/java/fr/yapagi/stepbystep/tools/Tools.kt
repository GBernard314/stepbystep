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

@SuppressLint("LogNotTimber")
class Tools {

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
    fun isPermissionsGranted(activity: Activity): Boolean {
        //1) List manifest permissions
        val locPermission       = ActivityCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocPermission = ActivityCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        val networkPermission   = ActivityCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_NETWORK_STATE)
        val wifiPermission      = ActivityCompat.checkSelfPermission(activity.applicationContext, Manifest.permission.ACCESS_WIFI_STATE)

        return if (locPermission    == PackageManager.PERMISSION_GRANTED && //Location
                coarseLocPermission == PackageManager.PERMISSION_GRANTED && //Approx location
                networkPermission   == PackageManager.PERMISSION_GRANTED && //Mobile data
                wifiPermission      == PackageManager.PERMISSION_GRANTED    //WIFI
        ) {                                                 //PERMISSION GRANTED
            Log.d("maps", "Permissions Granted")
            true
        }
        else {                                              //PERMISSION DENIED
            Toast.makeText(activity.applicationContext,"Please, authorize location permission to load your position", Toast.LENGTH_SHORT).show()
            askForPermissions(activity)
            false
        }
    }                                                            //Check for manifest permissions
    fun isGPSEnable(locationManager: LocationManager, context: Context): Boolean {
        return if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("maps", "GPS OK")
            true
        }
        else{
            Toast.makeText(context,"ERROR : Please, enable GPS", Toast.LENGTH_SHORT).show()
            Log.d("maps", "ERROR : Please, enable GPS")
            false
        }
    }                                     //Check for GPS provider enable
    fun isNetworkEnable(connectionManager: ConnectivityManager, wifiManager: WifiManager, context: Context): Boolean {
        val networkInfo = connectionManager.activeNetworkInfo
        return if((networkInfo != null && networkInfo.isConnected) || wifiManager.isWifiEnabled){ //Network service enable
            Log.d("maps", "Network OK")
            true
        }
        else{                                                                                     //Network service disable
            Toast.makeText(context,"ERROR : Please, enable WIFI or mobile data", Toast.LENGTH_SHORT).show()
            Log.d("maps", "ERROR : Please, enable WIFI or mobile data")
            false
        }
    } //Check for Network service enable
    fun askForPermissions(activity: Activity) {
        //1) List permissions
        val permissions = arrayOf(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        )

        //2) Ask user
        ActivityCompat.requestPermissions(activity, permissions,1)
        Log.d("maps", "Permissions asked")
    }                                                                        //Ask user for permissions



    companion object {
        var weight = 0
        var age = 0
        lateinit var activitySelected: ActivityDetail
        var isLiteInfoSelected = false
        var isAFemale = true
        var height = 0

        //DISTANCE / CALORIES//
        fun init(weight: Int, height: Int, age: Int, activitySelected: ActivityDetail, isLiteInfoSelected: Boolean, isAFemale: Boolean){
            this.activitySelected = activitySelected
            this.weight = weight
            this.age = age
            this.isAFemale = isAFemale
            this.isLiteInfoSelected = isLiteInfoSelected
            this.height = height
        }
        fun distanceToCalories(distance: Float): PathSettings {
            //1) Calcul activity time
            val activityTime = distance / activitySelected.speed
            Log.d("tools", "Activity time : $activityTime")

            //2) Calcul BRM
            val brm = calculBrm()
            Log.d("tools", "BRM : $brm")

            //3) Calcul calories
            val calories = ((brm / 24) * activitySelected.met * activityTime).toInt()
            Log.d("tools", "Calories : $calories")

            return PathSettings(calories, activityTime, distance, ArrayList())
        }
        fun caloriesToDistance(caloriesToLoose: Int): PathSettings{
            //1) Calcul BRM (physical condition)
            val brm = calculBrm()
            Log.d("tools", "BRM : $brm")

            //2) Calcul activity time
            val activityTime = caloriesToLoose / ((brm/24) * activitySelected.met)
            Log.d("tools", "Activity time : " + (activityTime*60))

            //3) Calcul distance
            val distance = activitySelected.speed * activityTime
            Log.d("tools", "Distance : $distance")

            return PathSettings(caloriesToLoose, activityTime, distance, ArrayList())
        }
        private fun calculBrm(): Float{
            //1) Set lite or full height according to sex
            var heightInfo: Int = height
            if(isLiteInfoSelected){
                heightInfo = if(isAFemale) 160 else 180
            }
            Log.d("tools", "Height : $heightInfo")

            //2) Calcul & return BRM
            return if(isAFemale){
                ((9.56*weight) + (1.85*height) - (4.68*age) + 655).toFloat() //BRM female formula
            }
            else{
                ((13.75*weight) + (5*height) - (6.76*age) + 66).toFloat()    //BRM male formula
            }
        }
    }
}