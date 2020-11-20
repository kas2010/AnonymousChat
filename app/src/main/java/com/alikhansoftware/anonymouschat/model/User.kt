package com.alikhansoftware.anonymouschat.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class User(
    val id: String? = "",
    var name: String? = "",
    var gender: Int = 0,
    var age: Int = 18,
    var city: String? = "",
    var preferences: MutableList<String> = mutableListOf(),
    var filter: Filter? = Filter(),
    val createDate: Long = Date().time,
    var enterDate: Long = Date().time
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "gender" to gender,
            "age" to age,
            "city" to city,
            "preferences" to preferences,
        )
    }
}