package fr.yapagi.stepbystep.routing

class UserDetails(
        var weight:             Int,
        var height:             Int,
        var age:                Int,
        var activitySelected:   ActivityDetail,
        var isLiteInfoSelected: Boolean,
        var isAFemale:          Boolean,
        var distance:           Float,
        var calories:           Int
) {
}