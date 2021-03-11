package fr.yapagi.stepbystep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import fr.yapagi.stepbystep.data.Goal
import fr.yapagi.stepbystep.data.User
import fr.yapagi.stepbystep.network.Authenticator
import fr.yapagi.stepbystep.network.Database
import fr.yapagi.stepbystep.adapter.AccountActivityAdapter
import fr.yapagi.stepbystep.databinding.ActivityAccountBinding
import fr.yapagi.stepbystep.databinding.ActivityAuthenticationBinding

private lateinit var binding : ActivityAuthenticationBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase stuff starts here
        FirebaseApp.initializeApp(this)
        val auth = Authenticator(this)
        //Logs user in as "JDoe" (Dummy values for test purposes)
        auth.login("john.doe@gmail.com", "password")

        //val goal = Goal(auth.getUID(), 80.0F, 1)
        //val db = Database()
        //db.createGoal(goal)
        //End of firebase stuff

        startActivity(Intent(this, AuthenticationActivity::class.java))
    }
}
