package fr.yapagi.stepbystep.routing

import java.io.Serializable

class PathSettings(
        var calorie:      Int,
        var activityTime: Float,
        var distance:     Float,
        var waypoints:    ArrayList<Pair<String, String>>
): Serializable {
}