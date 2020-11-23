package com.alikhansoftware.anonymouschat.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Dialog(
    var users: MutableList<String> = mutableListOf(),
    var lastMessage: Message? = Message(),
    val createDate: Long = Date().time
)