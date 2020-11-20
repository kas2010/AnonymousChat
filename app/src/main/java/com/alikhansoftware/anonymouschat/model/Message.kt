package com.alikhansoftware.anonymouschat.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Message(
    val dialogId: String? = "",
    val text: String? = "",
    val sendUserId: String? = "",
    val receiveUserId: String? = "",
    val createDate: Long = Date().time
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "sendUserId" to sendUserId,
            "receiveUserId" to receiveUserId
        )
    }
}