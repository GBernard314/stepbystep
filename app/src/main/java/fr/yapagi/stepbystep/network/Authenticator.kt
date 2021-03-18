package fr.yapagi.stepbystep.network

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.yapagi.stepbystep.data.Goal
import fr.yapagi.stepbystep.data.User

class Authenticator() {
    companion object{
        private const val TAG = "AUTHENTICATOR"
        private var current_user: User? = null
    }

    private var auth: FirebaseAuth = Firebase.auth
    var userGoal: Goal? = null

    /**
     * A status variable to tell if someone is signed in or not
     * Use this as a verification before any user / db action.
     *      Authenticator.status == 0 if no user is logged in
     *      Authenticator.status == 1 if a user is logged in
     */
     fun status(): Int{
        val id = getUID()
        id?.let{
            return 1
        }?: run{
            return 0
        }
    }

    /**
     * This method registers a new user in firebase authenticator
     */
    fun register(fname: String, lname: String, uname: String, email: String, password: String, listener: DataListener){
        logout()
        val db = Database()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                //User created successfully
                Log.d(TAG, "register::successfully_created_user")
                val newUser = User(firstname = fname, lastname = lname, username = uname, email = email)
                auth.currentUser?.uid?.let { db.updateUser(it, newUser, object: DataListener{
                    override fun onSuccess(data: Any?) {
                        login(email, password, listener)
                    }

                    override fun onStart() {}

                    override fun onFailure(error: String) {}

                }) }
            }else{
                //User could not be created
                Log.d(TAG, "register::failed_user_creation", task.exception)
                listener.onFailure(task.exception.toString())
            }
        }
    }

    /**
     * This method logs an user into the Firebase authenticator system
     */
    fun login(email: String, password: String, listener: DataListener){
        logout()
        //Log.d(TAG, "login::"+email+"_"+password)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
            if(task.isSuccessful){
                //User successfully logged in
                Log.d(TAG, "login::successfully_logged_user_in")
                updateLoggedUser(listener)
            }else{
                //Login failed
                Log.d(TAG, "login::user_could_not_login", task.exception)
                listener.onFailure(task.exception.toString())
            }
        }
    }

    fun logout(){
        auth.signOut()
    }

    /**
     * This method updates the user attribute of this class using Firebase authenticator
     */
    private fun updateLoggedUser(listener: DataListener){
        val db = Database()
        auth.currentUser?.uid?.let { db.getUser(it, object: DataListener{
            override fun onStart() {
                Log.d(TAG, "updateLoggedUser::request_for_logged_user_started")
            }

            override fun onSuccess(data: Any?){
                Log.d(TAG, "updateLoggedUser::successfully_loaded_logged_user")
                data?.let{user ->
                    current_user = user as User
                }
                listener.onSuccess(current_user)
            }

            override fun onFailure(error: String){
                Log.d(TAG, "updateLoggedUser::failed_logged_user_load_up")
                current_user = null
            }
        })}
    }

    fun getUID() : String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUser(): User? {
        return current_user
    }

    fun loadGoal(){
        val db: Database = Database()
        db.getAllGoals(object : DataListener{
            override fun onSuccess(data: Any?) {
                Log.d(TAG, "Goals list loaded, now finding User's goal")
                data?.let {
                    for (goal in data as Map<String, Goal>) {
                        if (goal.value.user_id == getUID()) {
                            userGoal = goal.value
                            Log.d(TAG, "User's goal retrieved successfully")
                        }
                    }
                }
            }

            override fun onStart() {
                Log.d(TAG, "Getting goals list")
            }

            override fun onFailure(error: String) {
                Log.d(TAG, "Failed to load user's goal")
            }

        })
    }
}