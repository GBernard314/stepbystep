package fr.yapagi.stepbystep.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.yapagi.stepbystep.data.Goal
import fr.yapagi.stepbystep.data.Run
import fr.yapagi.stepbystep.data.User

class Database {
    companion object{
        const val URL = "https://stepbystep-4a7a7-default-rtdb.europe-west1.firebasedatabase.app/"
        const val USERS = "users"
        const val GOALS = "goals"
        const val RUNS = "runs"
    }

    private val database = Firebase.database(URL)

    fun updateUser(uid: String, user: User, listener: DataListener){
        val usersDb = database.getReference(USERS)
        usersDb.child(uid).setValue(user).addOnCompleteListener{
            listener.onSuccess(user)
        }
    }

    fun getUser(uid: String, listener: DataListener){
        val usersDb = database.getReference(USERS)
        listener.onStart()
        usersDb.child(uid).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                listener.onSuccess(dataSnapshot.getValue<User>())
            }

            override fun onCancelled(error: DatabaseError){
                listener.onFailure(error.toString())
            }
        })
    }

    fun getAllUsers(listener: DataListener){
        val usersDb = database.getReference(USERS)
        listener.onStart()
        usersDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersJson: Map<String, Any>? = dataSnapshot.getValue<HashMap<String, Any>>()
                val users: MutableMap<String, User>? = mutableMapOf()
                if (usersJson != null) {
                    for (user in usersJson) {
                        //IMPORTANT : We use Gson().toJson(str) instead of str.toString()
                        //Bc toString() wipes out "" and they are needed by the parser
                        val newUser: User =
                                Gson().fromJson(Gson().toJson(user.value), User::class.java)
                        users?.set(user.key, newUser)
                    }
                }
                listener.onSuccess(users)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onFailure("Failed to get users")
            }
        })
    }

    fun deleteUser(uid: String){}

    fun createGoal(goal: Goal){
        val goalsDb = database.getReference(GOALS)
        val uid: String? = goalsDb.push().key
        uid?.let{updateGoal(it, goal)}
    }

    fun updateGoal(uid: String, goal: Goal){
        val goalsDb = database.getReference(GOALS)
        goalsDb.child(uid).setValue(goal)
    }

    fun getGoal(uid: String, listener: DataListener){
        val goalsDb = database.getReference(GOALS)
        listener.onStart()
        goalsDb.child(uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listener.onSuccess(dataSnapshot.getValue<Goal>())
            }

            override fun onCancelled(error: DatabaseError){
                listener.onFailure(error.toString())
            }
        })
    }

    fun createRun(run: Run){
        val runsDb = database.getReference(RUNS)
        val uid: String? = runsDb.push().key
        uid?.let{updateRun(it, run)}
    }

    fun updateRun(uid: String, run: Run){
        val runsDb = database.getReference(RUNS)
        runsDb.child(uid).setValue(run)
    }

    fun getAllGoals(listener: DataListener){
        val goalsDb = database.getReference(GOALS)
        listener.onStart()
        goalsDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val goalsJson: Map<String, Any>? = dataSnapshot.getValue<HashMap<String, Any>>()
                val goals: MutableMap<String, Goal>? = mutableMapOf()
                if (goalsJson != null) {
                    for (goal in goalsJson) {
                        //IMPORTANT : We use Gson().toJson(str) instead of str.toString()
                        //Bc toString() wipes out "" and they are needed by the parser
                        val newGoal: Goal =
                                Gson().fromJson(Gson().toJson(goal.value), Goal::class.java)
                        goals?.set(goal.key, newGoal)
                    }
                }
                listener.onSuccess(goals)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onFailure("Failed to get Goals")
            }
        })
    }
}