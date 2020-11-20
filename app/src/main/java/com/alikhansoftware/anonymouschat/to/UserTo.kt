package com.alikhansoftware.anonymouschat.to

import com.alikhansoftware.anonymouschat.model.Filter
import com.alikhansoftware.anonymouschat.model.User

class UserTo(var uid: String, user: User) {
    val id: String? = user.id
    val name: String? = user.name
    val gender: Int = user.gender
    val age: Int = user.age
    val city: String? = user.city
    val preferences: MutableList<String> = user.preferences
    val filter: Filter? = user.filter
}