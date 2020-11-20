package com.alikhansoftware.anonymouschat.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.get
import com.alikhansoftware.anonymouschat.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipUtil(private val layoutInflater: LayoutInflater) {
    fun createChipList(list: Array<String>, chipGroup: ChipGroup) {

        chipGroup.removeAllViews()

        for (text in list) {
            val newChip = layoutInflater.inflate(
                R.layout.chip_item, chipGroup, false
            ) as Chip

            newChip.text = text
            newChip.isCloseIconVisible = true
            newChip.setOnCloseIconClickListener {
                chipGroup.removeView(it)
            }

            chipGroup.addView(newChip)
        }
    }

    fun addChip(context: Context, editText: EditText, chipGroup: ChipGroup) {
        val keyword = editText.text

        if (keyword.isNullOrEmpty()) {
            val toast = Toast.makeText(
                context,
                context.getString(R.string.message_enter_keyword), Toast.LENGTH_LONG
            )
            toast.show()
            return
        }

        val newChip = layoutInflater.inflate(
            R.layout.chip_item, chipGroup, false
        ) as Chip

        newChip.text = keyword
        newChip.isCloseIconVisible = true
        newChip.setOnCloseIconClickListener {
            chipGroup.removeView(it)
        }

        chipGroup.addView(newChip)

        editText.setText("")
    }

    fun getChipList(chipGroup: ChipGroup): List<String> {
        val result = mutableListOf<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup[i] as Chip
            result.add(chip.text.toString())
        }

        return result
    }
}