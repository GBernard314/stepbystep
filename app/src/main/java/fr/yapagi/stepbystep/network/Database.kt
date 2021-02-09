package fr.yapagi.stepbystep.network

import fr.yapagi.stepbystep.data.User

class Database {
    companion object{
        const val URL = "https://stepbystep-4a7a7-default-rtdb.europe-west1.firebasedatabase.app/"
        const val USERS = "users"
    }

    fun createUser(){}
    fun updateUser(uid: String){}
    fun getUser(uid: String){}
    fun getAllUsers(){}
    fun deleteUser(uid: String){}
}