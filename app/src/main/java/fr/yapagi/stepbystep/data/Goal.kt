package fr.yapagi.stepbystep.data
import java.io.Serializable

data class Goal(
    var user_id: String? = null,
    var target_weight: Float? = 0.0f,
    var intensity: String? = null
) : Serializable {}