package com.alikhansoftware.anonymouschat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alikhansoftware.anonymouschat.adapter.UserAdapter
import com.alikhansoftware.anonymouschat.util.UserDiffUtilCallback
import com.alikhansoftware.anonymouschat.data.DataViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_main.*


class HomeFragment : Fragment() {
    private lateinit var model: DataViewModel
    private val adapter = UserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            model = ViewModelProvider(activity).get(DataViewModel::class.java)

            model.getState().observe(viewLifecycleOwner, { state ->
                when (state) {
                    DataViewModel.State.UNDEFINED -> {
                        if (FirebaseAuth.getInstance().currentUser == null) {
                            startActivityForResult(Intent(activity, WelcomeActivity::class.java),
                                WELCOME_REQUEST_CODE
                            )
                        }
                    }
                    DataViewModel.State.USER_NOT_EXIST -> {
                        findNavController().navigate(R.id.profileFragment)
                    }
                    DataViewModel.State.USER_EXIST -> {
                        model.getUsers()
                            .observe(viewLifecycleOwner, { users ->
                                val callback = UserDiffUtilCallback(adapter.getUserList(), users)
                                val result = DiffUtil.calculateDiff(callback)
                                adapter.setUserList(users)
                                result.dispatchUpdatesTo(adapter)
                            })
                    }
                    else -> {}
                }
            })

            adapter.setNavController(findNavController())
            adapter.setViewModel(model)
            mainRecyclerView.adapter = adapter
            mainRecyclerView.layoutManager = LinearLayoutManager(activity)
            mainRecyclerView.setHasFixedSize(true)
        }

        //for test
        buttonSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WELCOME_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.let {
                val result = it.getStringExtra("agreement")
                if (result == "adopt") {
                    FirebaseAuth.getInstance().signInAnonymously()
                    return
                }
            }
        }
        activity?.finish()
    }

    companion object {
        private const val TAG = "UsersFragment"
        private const val WELCOME_REQUEST_CODE = 1001
    }
}