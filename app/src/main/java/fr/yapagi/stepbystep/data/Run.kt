package fr.yapagi.stepbystep.data
import java.io.Serializable

class Run(
    val userId: String,
    val calories: Int,
    val activityTime: Float,
    val distance: Float
): Serializable {}