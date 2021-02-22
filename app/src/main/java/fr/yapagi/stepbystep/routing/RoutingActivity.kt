package fr.yapagi.stepbystep.routing

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.ActivityRoutingBinding
import fr.yapagi.stepbystep.tools.Tools

class RoutingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityRoutingBinding

    //METHOD
    private var isDistanceMethodSelected: Boolean = false
    private var isLiteInfoSelected:      Boolean = false
    private var activities = HashMap<Long, ActivityDetail>()

    //PARAMETERS
    private val tools: Tools = Tools()
    private lateinit var activitySelected: ActivityDetail
    private var height:           Int = 0
    private var age:             Int = 0
    private var weight:          Int = 0
    private var distance:        Float = 0.0F
    private var caloriesToLoose: Int = 0
    private var isAFemale:       Boolean = true

    //ANSWER
    private lateinit var result: ResultFromConverter



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
            if(isDataValidated()){
                result = if(isDistanceMethodSelected){
                    tools.distanceToCalories(weight, height, age, activitySelected, distance, isLiteInfoSelected, isAFemale)
                } else{
                    tools.caloriesToDistance(weight, height, age, activitySelected, caloriesToLoose, isLiteInfoSelected, isAFemale)
                }
            }
        }
        loadUI()

        //SPINNER
        updateSpinner()
    }



    //INPUTS MANAGER//
    private fun updateSpinner(){
        binding.rActivityTypeSpinner.onItemSelectedListener = this

        //1) Init activities map with METs value
        activities[0]  = ActivityDetail("Normal walking (~5.5km/h)",        4.3F,  5.5F)
        activities[1]  = ActivityDetail("Normal running (~9.5km/h)",        9.8F,  9.5F)
        activities[2]  = ActivityDetail("Walking with backpack",            7.0F,  5.5F)
        activities[3]  = ActivityDetail("Climbing hills",                   6.3F,  5.5F)
        activities[4]  = ActivityDetail("Climbing hills with backpack",     7.3F,  5.5F)
        activities[5]  = ActivityDetail("Hiking",                           5.3F,  5.5F)
        activities[6]  = ActivityDetail("Slow walking (~3.5km/h)",          2.8F,  3.5F)
        activities[7]  = ActivityDetail("Fast walking (~6.5km/h)",          5.0F,  6.5F)
        activities[8]  = ActivityDetail("Very fast walking (~8km/h)",       8.3F,  8.0F)
        activities[9]  = ActivityDetail("Walking the dog",                  3.0F,  5.5F)
        activities[10] = ActivityDetail("Very slow running (~6.5km/h)",     6.0F,  6.5F)
        activities[11] = ActivityDetail("Slow running (~8km/h)",            8.3F,  8.0F)
        activities[12] = ActivityDetail("Little fast running (~11km/h)",    11.0F, 11.0F)
        activities[13] = ActivityDetail("Fast running (~13km/h)",           11.8F, 13.0F)
        activities[14] = ActivityDetail("Very fast running (~14.5km/h)",    12.8F, 14.5F)
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
        binding.rHeightInput.visibility    = if(!isLiteInfoSelected)      View.VISIBLE else View.GONE
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