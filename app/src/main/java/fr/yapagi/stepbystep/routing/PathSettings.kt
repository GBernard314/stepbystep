package fr.yapagi.stepbystep.routing

import org.osmdroid.util.GeoPoint
import java.io.Serializable

class PathSettings(
        var calorie: Int,
        var activityTime: Float,
        var distance: Float,
        var waypoints: ArrayList<GeoPoint>
): Serializable {
}