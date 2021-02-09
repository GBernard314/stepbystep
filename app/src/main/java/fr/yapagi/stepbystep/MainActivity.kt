package fr.yapagi.stepbystep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import fr.yapagi.stepbystep.network.Authenticator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //Firebase stuff starts here
        FirebaseApp.initializeApp(this)
        val auth = Authenticator(this)
        //Logs user in as "JDoe" (Dummy values for test purposes)
        auth.login("john.doe@gmail.com", "password")
        //End of firebase stuff

        startActivity(Intent(this, DashboardActivity::class.java))
    }
}
