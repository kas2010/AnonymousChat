package com.alikhansoftware.anonymouschat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alikhansoftware.anonymouschat.adapter.DialogAdapter
import com.alikhansoftware.anonymouschat.util.DialogDiffUtilCallback
import com.alikhansoftware.anonymouschat.data.DataViewModel
import kotlinx.android.synthetic.main.fragment_dialog.*

class DialogFragment : Fragment() {
    private lateinit var model: DataViewModel
    private val adapter = DialogAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            model = ViewModelProvider(activity).get(DataViewModel::class.java)

            model.getState().observe(viewLifecycleOwner, { state ->
                when (state) {
                    DataViewModel.State.USER_NOT_EXIST -> {
                        findNavController().navigate(R.id.profileFragment)
                    }
                    DataViewModel.State.USER_EXIST -> {
                        model.getDialogs()
                            .observe(viewLifecycleOwner, { dialogs ->
                                val callback =
                                    DialogDiffUtilCallback(adapter.getDialogList(), dialogs)
                                val result = DiffUtil.calculateDiff(callback)
                                adapter.setDialogList(dialogs)
                                result.dispatchUpdatesTo(adapter)
                            })
                    }
                    else -> {
                    }
                }
            })

            adapter.setNavController(findNavController())
            adapter.setViewModel(model)
            dialogRecyclerView.adapter = adapter
            dialogRecyclerView.layoutManager = LinearLayoutManager(activity)
            dialogRecyclerView.setHasFixedSize(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    companion object {
        private const val TAG = "DialogFragment"
    }
}