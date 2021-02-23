package fr.yapagi.stepbystep.routing

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import fr.yapagi.stepbystep.map.MapActivity
import org.osmdroid.bonuspack.R
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow


class UpdateRoadTask(var context: Context, var map: MapView) : AsyncTask<Any?, Void?, Array<Road>>() {
    override fun doInBackground(vararg params: Any?): Array<Road>? {
        val waypoints = params[0] as ArrayList<GeoPoint>
        val roadManager: RoadManager = OSRMRoadManager(context)
        return roadManager.getRoads(waypoints)
    }

    override fun onPostExecute(roads: Array<Road>) {

        val mRoads = roads
        if (roads[0].mStatus != Road.STATUS_OK)
            Toast.makeText(context, "Error " + roads[0].mStatus, Toast.LENGTH_SHORT).show()

        val mRoadOverlays = arrayOfNulls<Polyline>(roads.size)
        val mapOverlays: List<Overlay> = map.overlays
        for (i in roads.indices) {
            val roadPolyline = RoadManager.buildRoadOverlay(roads[i])
            mRoadOverlays[i] = roadPolyline
            val routeDesc = roads[i].getLengthDurationText(context, -1)
            roadPolyline.title = "test"
            roadPolyline.infoWindow = BasicInfoWindow(R.layout.bonuspack_bubble, map)
            roadPolyline.relatedObject = i
            //                roadPolyline.setOnClickListener(new RoadOnClickListener());
            map.overlays.add(roadPolyline)
            //selectRoad(0);
                map.invalidate();
            //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
            //to avoid covering the other overlays.
        }
    }
}