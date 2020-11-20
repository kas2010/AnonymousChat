package com.alikhansoftware.anonymouschat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.alikhansoftware.anonymouschat.util.ChipUtil
import com.alikhansoftware.anonymouschat.data.DataViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private lateinit var model: DataViewModel
    private lateinit var chipUtil: ChipUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            chipUtil = ChipUtil(layoutInflater)

            model = ViewModelProvider(activity).get(DataViewModel::class.java)

            val navController = findNavController(this)

            model.getUser().observe(viewLifecycleOwner, { user ->
                editName.setText(user.name)

                when (user.gender) {
                    1 -> radioMale.isChecked = true
                    2 -> radioFemale.isChecked = true
                    else -> {
                    }
                }

                editAge.setText(user.age.toString())

                editCity.setText(user.city)

                chipUtil.createChipList(
                    user.preferences.toTypedArray(),
                    chipGroupPreferences
                )
            })

            buttonAddPreference.setOnClickListener {
                chipUtil.addChip(activity, editPreference, chipGroupPreferences)
            }

            buttonContinue.setOnClickListener {
                val gender = getGenderId(radioGroupGender.checkedRadioButtonId)

                val age = try {
                    Integer.parseInt(editAge.text.toString())
                } catch (e: NumberFormatException) {
                    DEFAULT_AGE
                }

                model.setUser(
                    editName.text.toString(),
                    gender,
                    age,
                    editCity.text.toString(),
                    chipUtil.getChipList(chipGroupPreferences),
                )

                navController.popBackStack()
            }
        }
    }

    private fun getGenderId(radioButtonId: Int): Int {
        return when (radioButtonId) {
            R.id.radioMale -> 1
            R.id.radioFemale -> 2
            else -> 0
        }
    }

    companion object {
        private const val TAG = "ProfileFragment"
        private const val DEFAULT_AGE = 18
    }
}