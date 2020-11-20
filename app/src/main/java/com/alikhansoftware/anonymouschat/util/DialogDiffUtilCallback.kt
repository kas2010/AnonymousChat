package com.alikhansoftware.anonymouschat.util

import androidx.recyclerview.widget.DiffUtil
import com.alikhansoftware.anonymouschat.model.Dialog

class DialogDiffUtilCallback(
    private val oldList: List<Dialog>,
    private val newList: List<Dialog>
) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDialog = oldList[oldItemPosition]
        val newDialog = newList[newItemPosition]
        return oldDialog.uid == newDialog.uid
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDialog = oldList[oldItemPosition]
        val newDialog = newList[newItemPosition]
        return oldDialog.userId == newDialog.userId
                && oldDialog.displayName == oldDialog.displayName
    }
}