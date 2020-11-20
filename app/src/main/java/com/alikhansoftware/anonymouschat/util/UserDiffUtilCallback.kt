package com.alikhansoftware.anonymouschat.util

import androidx.recyclerview.widget.DiffUtil
import com.alikhansoftware.anonymouschat.to.UserTo

class UserDiffUtilCallback(private val oldList: List<UserTo>, private val newList: List<UserTo>) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.id == newUser.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.name == newUser.name &&
                oldUser.age == newUser.age &&
                oldUser.city == newUser.city
    }
}