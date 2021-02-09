package fr.yapagi.stepbystep.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.yapagi.stepbystep.data.User

class Database {
    companion object{
        const val URL = "https://stepbystep-4a7a7-default-rtdb.europe-west1.firebasedatabase.app/"
        const val USERS = "users"
    }

    private val database = Firebase.database(URL)

    fun updateUser(uid: String, user: User){
        val usersDb = database.getReference(USERS)
        usersDb.child(uid).setValue(user)
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

    fun getAllUsers(){}
    fun deleteUser(uid: String){}
}