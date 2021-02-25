@file:Suppress("DEPRECATION")

package fr.yapagi.stepbystep.routing

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.view.View
import android.widget.TextView
import android.widget.Toast
import fr.yapagi.stepbystep.tools.Tools
import org.osmdroid.bonuspack.R
import org.osmdroid.bonuspack.routing.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class UpdateRoadTask(var context: Context, var map: MapView, var distanceText: TextView, var caloriesText: TextView, var activityTimeText: TextView) : AsyncTask<Any?, Void?, Array<Road>>() {
    override fun doInBackground(vararg params: Any?): Array<Road>? {
        val waypoints = params[0] as ArrayList<GeoPoint>
        val roadManager: RoadManager = OSRMRoadManager(context)
        return roadManager.getRoads(waypoints) //waypoints list
    }



    override fun onPostExecute(roads: Array<Road>) {
        if (roads[0].mStatus != Road.STATUS_OK) {
            Toast.makeText(context, "Error " + roads[0].mStatus, Toast.LENGTH_SHORT).show()
        }

        //Build polylines
        val mRoadOverlays = arrayOfNulls<Polyline>(roads.size)
        for (i in roads.indices) {
            val roadPolyline = RoadManager.buildRoadOverlay(roads[i])

            mRoadOverlays[i] = roadPolyline
            roadPolyline.title = "test"
            roadPolyline.color = Color.RED
            roadPolyline.infoWindow = BasicInfoWindow(R.layout.bonuspack_bubble, map)
            roadPolyline.relatedObject = i

            //Display info
            var dist: Double = 0.0
            for(road in mRoadOverlays){
                dist += road?.distance as Double
            }
            val pathSettings = Tools.distanceToCalories((dist/1000).toFloat())
            distanceText.visibility     = View.VISIBLE
            caloriesText.visibility     = View.VISIBLE
            activityTimeText.visibility = View.VISIBLE
            distanceText.text     = Math.round(pathSettings.distance).toString() + " km"
            caloriesText.text     = pathSettings.calorie.toString() + " kcal"
            activityTimeText.text = (pathSettings.activityTime*60).roundToInt().toString() + " min"

            map.overlays.add(roadPolyline)
            map.invalidate()
        }
    }
}