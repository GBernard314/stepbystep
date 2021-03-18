package fr.yapagi.stepbystep.data

import java.io.Serializable

data class User(
        val firstname: String? = null,
        val lastname: String? = null,
        var username: String? = null,
        var gender: String? = null,
        val email: String? = null,
        var height: Int? = 0,
        var weight: Float? = 0.0F
): Serializable {}