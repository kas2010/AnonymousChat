package com.alikhansoftware.anonymouschat.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Filter(
    var gender: Int = 0,
    var ageFrom: Float = 18.0F,
    var ageTo: Float = 100.0F,
    var checkCity: Boolean = false,
    var preferences: MutableList<String> = mutableListOf()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "gender" to gender,
            "ageFrom" to ageFrom,
            "ageTo" to ageTo,
            "checkCity" to checkCity,
            "preferences" to preferences
        )
    }
}