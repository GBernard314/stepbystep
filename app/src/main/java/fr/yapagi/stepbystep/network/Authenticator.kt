package fr.yapagi.stepbystep.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.yapagi.stepbystep.data.User

class Authenticator {
    companion object{
        private const val TAG = "AUTHENTICATOR"
    }

    private var auth: FirebaseAuth = Firebase.auth
    private var user: User? = null

    /**
     * This method returns the data object corresponding to the currently logged Firebase user.
     * It may return null if no user is currently logged in or if there is another error.
     */
    fun getLoggedUser(){}
}