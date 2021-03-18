package fr.yapagi.stepbystep.data
import java.io.Serializable

class Run(
    val user_id: String,
    val calories: Int,
    val heart_rate: Int,
    val steps_goal: Int,
    val steps: Int
): Serializable {}