package com.alikhansoftware.anonymouschat.to

import com.alikhansoftware.anonymouschat.model.Filter

class FilterTo(var uid: String, filter: Filter) {
    val userId: String? = filter.userId
    val gender: Int = filter.gender
    val ageFrom: Float = filter.ageFrom
    val ageTo: Float = filter.ageTo
    val checkCity: Boolean = filter.checkCity
    val preferences: MutableList<String> = filter.preferences
}