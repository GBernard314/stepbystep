package fr.yapagi.stepbystep.network

import fr.yapagi.stepbystep.data.User

/**
 * A standard listener with an "onStart()" method, an "onSuccess()" method and an "onFailure()"
 * This must be overridden every time you call a Database async method as they take one of those
 * as a parameter. This is the contract we have with the database
 */
interface DataListener{
    fun onSuccess(data: Any?)
    fun onStart()
    fun onFailure(error: String)
}