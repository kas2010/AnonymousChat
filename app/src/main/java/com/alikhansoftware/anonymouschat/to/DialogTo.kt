package com.alikhansoftware.anonymouschat.to

import com.alikhansoftware.anonymouschat.model.Dialog
import com.alikhansoftware.anonymouschat.model.Message

class DialogTo(var uid: String, var currentUser: UserTo, dialog: Dialog) {
    val users: MutableList<String> = dialog.users
    val lastMessage: Message? = dialog.lastMessage
    val createDate: Long = dialog.createDate
}