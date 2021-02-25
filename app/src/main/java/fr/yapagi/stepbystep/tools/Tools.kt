package fr.yapagi.stepbystep.tools

import android.util.Log
import fr.yapagi.stepbystep.routing.ActivityDetail
import fr.yapagi.stepbystep.routing.PathSettings

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