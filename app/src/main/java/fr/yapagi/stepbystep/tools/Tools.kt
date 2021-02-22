package fr.yapagi.stepbystep.tools

import android.util.Log
import fr.yapagi.stepbystep.routing.ActivityDetail
import fr.yapagi.stepbystep.routing.ResultFromConverter

class Tools {
    fun distanceToCalories(){

    }

    fun caloriesToDistance(
        weight: Int,
        height: Int,
        age: Int,
        activity: ActivityDetail,
        caloriesToLoose: Int,
        isLiteInfo: Boolean,
        isAFemale: Boolean
    ): ResultFromConverter{
        //1) Set lite or full height according to sex
        var heightInfo: Int = height
        if(isLiteInfo){
            heightInfo = if(isAFemale) 160 else 180
        }
        Log.d("tools", "Height : $heightInfo")

        //2) Calcul BRM (physical condition)
        val brm = if(isAFemale){
            ((9.56*weight) + (1.85*heightInfo) - (4.68*age) + 655).toFloat()
        }
        else{
            ((13.75*weight) + (5*heightInfo) - (6.76*age) + 66).toFloat()
        }
        Log.d("tools", "BRM : $brm")

        //3) Calcul activity time
        val activityTime = caloriesToLoose / ((brm/24) * activity.met)
        Log.d("tools", "Activity time : " + (activityTime*60))

        //4) Calcul distance
        val distance = activity.speed * activityTime
        Log.d("tools", "Distance : $distance")

        return ResultFromConverter(caloriesToLoose, activityTime, distance)
    }
}