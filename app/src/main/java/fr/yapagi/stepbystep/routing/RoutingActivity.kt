package fr.yapagi.stepbystep.routing

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.ActivityRoutingBinding


class RoutingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityRoutingBinding
    private var isDistanceMethodSelected: Boolean = false
    private var isLightInfoSelected:      Boolean = false
    private var activities = HashMap<Long, Pair<String, Float>>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //BTNS
        binding.rHeighInput.visibility = View.VISIBLE
        binding.rCalorieInput.visibility = View.VISIBLE
        binding.rDistanceInput.visibility = View.GONE
        binding.rFullRoutingMethodSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.rFullRoutingMethodSwitch.text = if(isChecked) "Distance" else "Calories"
            isDistanceMethodSelected = isChecked
            loadUI()
        }
        binding.rFullRoutingInfoDetailSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.rFullRoutingInfoDetailSwitch.text = if(isChecked) "Light" else "Full"
            isLightInfoSelected = isChecked
            loadUI()
            updateSpinner()
        }

        //SPINNER
        updateSpinner()
    }
    private fun updateSpinner(){
        binding.rActivityTypeSpinner.onItemSelectedListener = this

        //1) Init activities map with METs value
        activities[0]  = Pair("Normal walking (~5.5km/h)",        4.3F)
        activities[1]  = Pair("Normal running (~9.5km/h)",        9.8F)
        activities[2]  = Pair("Walking with backpack",            7.0F)
        activities[3]  = Pair("Climbing hills",                   6.3F)
        activities[4]  = Pair("Climbing hills with backpack",     7.3F)
        activities[5]  = Pair("Hiking",                           5.3F)
        activities[6]  = Pair("Slow walking (~3.5km/h)",          2.8F)
        activities[7]  = Pair("Fast walking (~6.5km/h)",          5.0F)
        activities[8]  = Pair("Very fast walking (~8km/h)",       8.3F)
        activities[9]  = Pair("Walking the dog",                  3.0F)
        activities[10] = Pair("Very slow running (~6.5km/h)",     6.0F)
        activities[11] = Pair("Slow running (~8km/h)",            8.3F)
        activities[12] = Pair("Little fast running (~11km/h)",    11.0F)
        activities[13] = Pair("Fast running (~13km/h)",           11.8F)
        activities[14] = Pair("Very fast running (~14.5km/h)",    12.8F)
        activities[15] = Pair("Extremely fast running (~16km/h)", 14.5F)

        //2) Generate activity type
        val categories: MutableList<String> = ArrayList()
        val totalCategory = if(!isLightInfoSelected) 15 else 1
        for(nbActivitie in 0..totalCategory){
            categories.add(activities.getValue(nbActivitie.toLong()).first)
        }

        //3) Set activity list
        val dataAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories)
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.rActivityTypeSpinner.adapter = dataAdapter
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val item = parent.getItemAtPosition(position).toString()

        Toast.makeText(parent.context, "Selected: $id", Toast.LENGTH_LONG).show()
    }



    private fun loadUI(){
        if(!isDistanceMethodSelected){     //CALORIES
            if(!isLightInfoSelected){          //FULL INFO
                enableInputs(true, true, false)
            }

            else{                              //LIGHT INFO
                enableInputs(false, true, false)
            }
        }

        else if(isDistanceMethodSelected){ //DISTANCE
            if(!isLightInfoSelected){           //FULL INFO
                enableInputs(true, false, true)
            }

            else{                               //LIGHT INFO
                enableInputs(false, false, true)
            }
        }
    }
    private fun enableInputs(isHeighEnable: Boolean, isCaloriesEnable: Boolean, isDistanceEnable: Boolean){
        binding.rHeighInput.visibility    = if(isHeighEnable)    View.VISIBLE else View.GONE
        binding.rCalorieInput.visibility  = if(isCaloriesEnable) View.VISIBLE else View.GONE
        binding.rDistanceInput.visibility = if(isDistanceEnable) View.VISIBLE else View.GONE
    }
}