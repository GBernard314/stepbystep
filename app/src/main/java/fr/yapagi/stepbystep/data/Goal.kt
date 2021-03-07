package fr.yapagi.stepbystep.data
import java.io.Serializable

data class Goal(
    val user_id: String? = null,
    val target_weight: Float = 0.0f,
    val intensity: Int = 0
) : Serializable {}