package fr.yapagi.stepbystep.network

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.yapagi.stepbystep.data.User

class Authenticator(private val activity: Activity) {
    companion object{
        private const val TAG = "AUTHENTICATOR"
        private var current_user: User? = null
    }

    private var auth: FirebaseAuth = Firebase.auth

    /**
     * This method registers a new user in firebase authenticator
     */
    fun register(uname: String, email: String, password: String){
        val db = Database()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity){ task ->
                if(task.isSuccessful){
                    //User created successfully
                    Log.d(TAG, "register::successfully_created_user")
                    val newUser = User(uname, email)
                    auth.currentUser?.uid?.let { db.updateUser(it, newUser) }
                    login(email, password)
                }else{
                    //User could not be created
                    Log.d(TAG, "register::failed_user_creation", task.exception)
                }
            }
    }

    /**
     * This method logs an user into the Firebase authenticator system
     */
    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity){task ->
                if(task.isSuccessful){
                    //User successfully logged in
                    Log.d(TAG, "login::successfully_logged_user_in")
                    updateLoggedUser()
                }else{
                    //Login failed
                    Log.d(TAG, "login::user_could_not_login", task.exception)
                }
            }
    }


    /**
     * This method updates the user attribute of this class using Firebase authenticator
     */
    private fun updateLoggedUser(){
        val db = Database()
        auth.currentUser?.uid?.let { db.getUser(it, object: DataListener{
            override fun onStart(){
                Log.d(TAG, "updateLoggedUser::request_for_logged_user_started")
            }

            override fun onSuccess(data: Any?){
                Log.d(TAG, "updateLoggedUser::successfully_loaded_logged_user")
                data?.let{user ->
                    current_user = user as User
                }
            }

            override fun onFailure(error: String){
                Log.d(TAG, "updateLoggedUser::failed_logged_user_load_up")
                current_user = null
            }
        })}
    }
}