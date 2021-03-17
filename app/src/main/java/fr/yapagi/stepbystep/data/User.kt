package fr.yapagi.stepbystep.data

import java.io.Serializable

data class User(
        val firstname: String? = null,
        val lastname: String? = null,
        val username: String? = null,
        val email: String? = null,
        val weight: Float = 0.0F
): Serializable {}