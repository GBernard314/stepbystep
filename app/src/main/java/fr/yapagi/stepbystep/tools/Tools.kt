package fr.yapagi.stepbystep.tools

import android.util.Log
import fr.yapagi.stepbystep.routing.ActivityDetail
import fr.yapagi.stepbystep.routing.PathSettings

class Tools {
    fun distanceToCalories(
            weight: Int,
            height: Int,
            age: Int,
            activity: ActivityDetail,
            distance: Float,
            isLiteInfo: Boolean,
            isAFemale: Boolean
    ): PathSettings {
        //1) Calcul activity time
        val activityTime = distance / activity.speed
        Log.d("tools", "Activity time : $activityTime")

        //2) Calcul BRM
        val brm = calculBrm(isAFemale, weight, height, age, isLiteInfo)
        Log.d("tools", "BRM : $brm")

        //3) Calcul calories
        val calories = ((brm / 24) * activity.met * activityTime).toInt()
        Log.d("tools", "Calories : $calories")

        return PathSettings(calories, activityTime, distance, ArrayList())
    }

    fun caloriesToDistance(
            weight: Int,
            height: Int,
            age: Int,
            activity: ActivityDetail,
            caloriesToLoose: Int,
            isLiteInfo: Boolean,
            isAFemale: Boolean
    ): PathSettings{
       //1) Calcul BRM (physical condition)
        val brm = calculBrm(isAFemale, weight, height, age, isLiteInfo)
        Log.d("tools", "BRM : $brm")

        //2) Calcul activity time
        val activityTime = caloriesToLoose / ((brm/24) * activity.met)
        Log.d("tools", "Activity time : " + (activityTime*60))

        //3) Calcul distance
        val distance = activity.speed * activityTime
        Log.d("tools", "Distance : $distance")

        return PathSettings(caloriesToLoose, activityTime, distance, ArrayList())
    }

    private fun calculBrm(
            isAFemale: Boolean,
            weight: Int,
            height: Int,
            age: Int,
            isLiteInfo: Boolean
    ): Float{

        //1) Set lite or full height according to sex
        var heightInfo: Int = height
        if(isLiteInfo){
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
}