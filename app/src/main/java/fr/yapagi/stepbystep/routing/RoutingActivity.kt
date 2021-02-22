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
    private var activities = HashMap<Long, String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //BTNS
        binding.rFullRoutingMethodSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            isDistanceMethodSelected = isChecked
            loadUI()
        }
        binding.rFullRoutingInfoDetailSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            isLightInfoSelected = isChecked
            loadUI()
            updateSpinner()
        }

        //SPINNER
        updateSpinner()
    }
    private fun updateSpinner(){
        binding.rActivityTypeSpinner.onItemSelectedListener = this

        //1) Init activities map
        activities[0]  = "Normal walking (~5.5km/h)"        //4.3
        activities[1]  = "Normal running (~9.5km/h)"        //9.8
        activities[2]  = "Walking with backpack"            //7.0
        activities[3]  = "Climbing hills"                   //6.3
        activities[4]  = "Climbing hills with backpack"     //7.3
        activities[5]  = "Hiking"                           //5.3
        activities[6]  = "Slow walking (~3.5km/h)"          //2.8
        activities[7]  = "Fast walking (~6.5km/h)"          //5.0
        activities[8]  = "Very fast walking (~8km/h)"       //8.3
        activities[9]  = "Walking the dog"                  //3.0
        activities[10] = "Very slow running (~6.5km/h)"     //6.0
        activities[11] = "Slow running (~8km/h)"            //8.3
        activities[12] = "Little fast running (~11km/h)"    //11.0
        activities[13] = "Fast running (~13km/h)"           //11.8
        activities[14] = "Very fast running (~14.5km/h)"    //12.8
        activities[15] = "Extremely fast running (~16km/h)" //14.5

        //2) Generate activity type
        val categories: MutableList<String> = ArrayList()
        val totalCategory = if(!isLightInfoSelected) 15 else 1
        for(nbActivitie in 0..totalCategory){
            categories.add(activities.getValue(nbActivitie.toLong()))
        }

        //3) Set activity list
        val dataAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories)
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.rActivityTypeSpinner.adapter = dataAdapter
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        // On selecting a spinner item
        val item = parent.getItemAtPosition(position).toString()

        // Showing selected spinner item
        Toast.makeText(parent.context, "Selected: $id", Toast.LENGTH_LONG).show()
    }



    private fun loadUI(){
        if(!isDistanceMethodSelected){     //CALORIES
            if(!isLightInfoSelected){          //FULL INFO

            }

            else{                              //LIGHT INFO

            }
        }

        else if(isDistanceMethodSelected){ //DISTANCE
            if(!isLightInfoSelected){           //FULL INFO

            }

            else{                               //LIGHT INFO

            }
        }
    }
}