package fr.yapagi.stepbystep.routing

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.geometry.LatLng
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.ActivityRoutingBinding
import fr.yapagi.stepbystep.map.MapActivity
import fr.yapagi.stepbystep.tools.Tools
import kotlin.random.Random

@SuppressLint("LogNotTimber")
class RoutingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityRoutingBinding

    //METHOD
    private var isDistanceMethodSelected = false
    private var isLiteInfoSelected       = false
    private var activities               = HashMap<Long, ActivityDetail>()

    //PARAMETERS
    private val tools            = Tools()
    private var activitySelected = ActivityDetail("", 0f, 0f)
    private var height           = 0
    private var age              = 0
    private var weight           = 0
    private var distance         = 0.0F
    private var caloriesToLoose  = 0
    private var isAFemale        = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ACTION
        binding.rMethodSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.rMethodSwitch.text = if(isChecked) "Distance" else "Calories"
            isDistanceMethodSelected = isChecked
            loadUI()
        }
        binding.rInfoDetailSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.rInfoDetailSwitch.text = if(isChecked) "Lite" else "Full"
            isLiteInfoSelected = isChecked
            loadUI()
            updateSpinner()
        }
        binding.rSexSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.rSexSwitch.text = if(isChecked) "Male" else "Female"
            isAFemale = !isChecked
        }
        binding.rBtnValidate.setOnClickListener {
            sendData()
        }
        loadUI()

        //SPINNER
        updateSpinner()
    }



    //PATH//
    private fun generatePathWaypoints(userLocation: LatLng, pathSettings: PathSettings) : ArrayList<Pair<String, String>> {

        //1) Set first point (user current location)
        val firstsPoint = ArrayList<Pair<String, String>>()
        firstsPoint.add(Pair(userLocation.latitude.toString(), userLocation.longitude.toString()))
        Log.d("maps", "Wp 0 -> ${firstsPoint[0].first} : ${firstsPoint[0].second}")

        //2) Set two more points according to the total distance wanted
        val pathPartDistance = (pathSettings.distance/4).toDouble()
        for(nbPoint in 0..1){

            //2.1) Get kilometer distance
            val longDistanceInKm = Random.nextDouble(pathPartDistance/10, pathPartDistance - pathPartDistance/10).toFloat() //Lat dist between 1/8 -> 7/8
            val latDistanceInKm = (pathPartDistance - longDistanceInKm).toFloat()                                                       //Long dist = 1/8 -> size remaining

            //2.2) Convert distance to lat/long
            var latDist  = tools.distanceToLat(latDistanceInKm)
            var longDist = tools.distanceToLong(longDistanceInKm, latDist)

            //2.3) Set negative/positive value
            latDist  = if(Random.nextBoolean()) -latDist else latDist
            longDist = if(Random.nextBoolean()) -longDist else longDist

            //2.4) Add to last point registered
            val pointLat  = firstsPoint[nbPoint].first.toDouble() + latDist
            val pointLong = firstsPoint[nbPoint].second.toDouble() + longDist

            firstsPoint.add(Pair(pointLat.toString(), pointLong.toString()))
            Log.d("maps", "Wp ${nbPoint+1} -> ${firstsPoint[firstsPoint.size-1].first} : ${firstsPoint[firstsPoint.size-1].second}")
        }

        firstsPoint.add(Pair(userLocation.latitude.toString(), userLocation.longitude.toString()))

        Log.d("maps", "Wp 3 -> ${firstsPoint[3].first} : ${firstsPoint[3].second}")
        return firstsPoint
    } //Generate waypoints according to user parameters
    private fun sendData() {
        if(isDataValidated()){
            //1) Ask for path details
            val tools = Tools()
            val userDetails = UserDetails(
                weight,
                height,
                age,
                activitySelected,
                isLiteInfoSelected,
                isAFemale,
                distance,
                caloriesToLoose
            )
            val result = if(isDistanceMethodSelected){
                tools.distanceToCalories(userDetails)
            } else{
                tools.caloriesToDistance(userDetails)
            }

            //2) Get waypoint list
            result.waypoints = generatePathWaypoints(MapActivity.getCurrentLocation(), result)

            //3) Send result to map activity
            val intent = Intent()
            intent.putExtra(MapActivity.WAYPOINTS, result)
            intent.putExtra(MapActivity.USER_DETAILS, userDetails)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }                                                                                                //Send data back to mapActivity



    //INPUTS MANAGER//
    private fun updateSpinner(){
        binding.rActivityTypeSpinner.onItemSelectedListener = this

        //1) Init activities map with METs value
        activities[0]  = ActivityDetail("Normal walking (~5.5km/h)",        4.3F,  5.5F)
        activities[1]  = ActivityDetail("Normal running (~8km/h)",          8.3F,  8.0F)
        activities[2]  = ActivityDetail("Walking with backpack",            7.0F,  5.5F)
        activities[3]  = ActivityDetail("Climbing hills",                   6.3F,  5.5F)
        activities[4]  = ActivityDetail("Climbing hills with backpack",     7.3F,  5.5F)
        activities[5]  = ActivityDetail("Hiking",                           5.3F,  5.5F)
        activities[6]  = ActivityDetail("Slow walking (~3.5km/h)",          2.8F,  3.5F)
        activities[7]  = ActivityDetail("Fast walking (~6.5km/h)",          5.0F,  6.5F)
        activities[8]  = ActivityDetail("Very fast walking (~8km/h)",       8.3F,  8.0F)
        activities[9]  = ActivityDetail("Walking the dog",                  3.0F,  5.5F)
        activities[10] = ActivityDetail("slow running (~6.5km/h)",          6.0F,  6.5F)
        activities[11]  = ActivityDetail("Little fast running (~9.5km/h)",  9.8F,  9.5F)
        activities[12] = ActivityDetail("fast running (~11km/h)",           11.0F, 11.0F)
        activities[13] = ActivityDetail("Very Fast running (~13km/h)",      11.8F, 13.0F)
        activities[14] = ActivityDetail("Super fast running (~14.5km/h)",   12.8F, 14.5F)
        activities[15] = ActivityDetail("Extremely fast running (~16km/h)", 14.5F, 16.0F)
        activitySelected = activities.getValue(0) //Init default activity

        //2) Generate activity type
        val categories: MutableList<String> = ArrayList()
        val totalCategory = if(!isLiteInfoSelected) 15 else 1
        for(nbActivity in 0..totalCategory){
            categories.add(activities.getValue(nbActivity.toLong()).name)
        }

        //3) Set activity list
        val dataAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories)
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.rActivityTypeSpinner.adapter = dataAdapter
    }                                                                //Update spinner activities according to switch positions
    override fun onNothingSelected(parent: AdapterView<*>?) {}                                      //Useless here (Default selection = normal walking)
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        activitySelected = activities.getValue(id)
    } //Update activity selected when switch are used
    private fun loadUI(){
        binding.rHeightInput.visibility   = if(!isLiteInfoSelected)       View.VISIBLE else View.GONE
        binding.rCalorieInput.visibility  = if(!isDistanceMethodSelected) View.VISIBLE else View.GONE
        binding.rDistanceInput.visibility = if(isDistanceMethodSelected)  View.VISIBLE else View.GONE
    }                                                                       //Display input according to user method and info choices
    private fun isDataValidated(): Boolean {
        //1) Age
        val ageText = binding.rAgeInput.editText?.text
        if(!ageText.isNullOrEmpty()){
            age = Integer.parseInt(ageText.toString())
        }
        else{
            Toast.makeText(this,"Please, fill AGE area", Toast.LENGTH_SHORT).show()
            return false
        }

        //2) Weight
        val weightText = binding.rWeightInput.editText?.text
        if(!weightText.isNullOrEmpty()){
            weight = Integer.parseInt(weightText.toString())
        }
        else{
            Toast.makeText(this,"Please, fill WEIGHT area", Toast.LENGTH_SHORT).show()
            return false
        }

        //3) Distance
        if(isDistanceMethodSelected){
            val distanceText = binding.rDistanceInput.editText?.text
            if(!distanceText.isNullOrEmpty()){
                distance = distanceText.toString().toFloat()
            }
            else{
                Toast.makeText(this,"Please, fill DISTANCE area", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        //4) Calories
        if(!isDistanceMethodSelected){
            val caloriesText = binding.rCalorieInput.editText?.text
            if(!caloriesText.isNullOrEmpty()){
                caloriesToLoose = Integer.parseInt(caloriesText.toString())
            }
            else{
                Toast.makeText(this,"Please, fill CALORIES area", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        //5) Height
        if(!isLiteInfoSelected){
            val heightText = binding.rHeightInput.editText?.text
            if(!heightText.isNullOrEmpty()){
                height = Integer.parseInt(heightText.toString())
            }
            else{
                Toast.makeText(this,"Please, fill HEIGHT area", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }                                                    //Check if data from path generator are corrects
}