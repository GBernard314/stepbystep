@file:Suppress("DEPRECATION")

package fr.yapagi.stepbystep.routing

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.widget.Toast
import org.osmdroid.bonuspack.R
import org.osmdroid.bonuspack.routing.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow

@Suppress("DEPRECATION")
class UpdateRoadTask(var context: Context, var map: MapView) : AsyncTask<Any?, Void?, Array<Road>>() {
    override fun doInBackground(vararg params: Any?): Array<Road>? {
        val waypoints = params[0] as ArrayList<GeoPoint>
        val roadManager: RoadManager = OSRMRoadManager(context)
        return roadManager.getRoads(waypoints)
    }



    override fun onPostExecute(roads: Array<Road>) {
        if (roads[0].mStatus != Road.STATUS_OK)
            Toast.makeText(context, "Error " + roads[0].mStatus, Toast.LENGTH_SHORT).show()

        val mRoadOverlays = arrayOfNulls<Polyline>(roads.size)
        for (i in roads.indices) {
            val roadPolyline = RoadManager.buildRoadOverlay(roads[i])
            mRoadOverlays[i] = roadPolyline
            roadPolyline.title = "test"
            roadPolyline.color = Color.RED
            roadPolyline.infoWindow = BasicInfoWindow(R.layout.bonuspack_bubble, map)
            roadPolyline.relatedObject = i
            map.overlays.add(roadPolyline)
            map.invalidate()
        }
    }
}