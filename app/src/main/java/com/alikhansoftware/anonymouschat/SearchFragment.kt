package com.alikhansoftware.anonymouschat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alikhansoftware.anonymouschat.util.ChipUtil
import com.alikhansoftware.anonymouschat.data.DataViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*

class SearchFragment : Fragment() {
    private lateinit var model: DataViewModel
    private lateinit var chipUtil: ChipUtil

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rangeAge.setLabelFormatter { value -> String.format(Locale.getDefault(), "%.0f", value) }

        activity?.let { activity ->
            chipUtil = ChipUtil(layoutInflater)

            model = ViewModelProvider(activity).get(DataViewModel::class.java)

            model.getUser().observe(viewLifecycleOwner, { user ->
                user.filter?.let {
                    when (it.gender) {
                        1 -> chipMale.isChecked = true
                        2 -> chipFemale.isChecked = true
                        else -> chipAll.isChecked = true
                    }
                    checkCity.isChecked = it.checkCity
                    rangeAge.values = listOf(it.ageFrom, it.ageTo)
                    chipUtil.createChipList(
                        it.preferences.toTypedArray(),
                        chipGroupPreferences
                    )
                }
            })

            buttonAddPreference.setOnClickListener {
                chipUtil.addChip(activity, editPreference, chipGroupPreferences)
            }

            buttonSave.setOnClickListener {
                val gender = getGender()
                val ageFrom = rangeAge.values[0]
                val ageTo = rangeAge.values[1]
                val checkCity = checkCity.isChecked
                val preferences = chipUtil.getChipList(chipGroupPreferences)
                model.setFilter(gender, ageFrom, ageTo, checkCity, preferences)
            }

            buttonClose.setOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

    private fun getGender(): Int {
        return when (chipGroupGender.checkedChipId) {
            R.id.chipMale -> 1
            R.id.chipFemale -> 2
            else -> 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}