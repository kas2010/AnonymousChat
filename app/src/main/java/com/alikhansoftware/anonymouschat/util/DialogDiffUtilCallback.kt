package com.alikhansoftware.anonymouschat.util

import androidx.recyclerview.widget.DiffUtil
import com.alikhansoftware.anonymouschat.to.DialogTo

class DialogDiffUtilCallback(
    private val oldList: List<DialogTo>,
    private val newList: List<DialogTo>
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
        return oldDialog.uid == newDialog.uid
    }
}