package com.alikhansoftware.anonymouschat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alikhansoftware.anonymouschat.R
import com.alikhansoftware.anonymouschat.to.UserTo
import com.alikhansoftware.anonymouschat.data.DataViewModel
import java.util.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private var users = mutableListOf<UserTo>()
    private lateinit var navController: NavController
    private lateinit var model: DataViewModel

    class MyViewHolder(v: View) :
        RecyclerView.ViewHolder(v) {
        val avatar: ImageView = v.findViewById(R.id.tvAvatar)
        val displayName: TextView = v.findViewById(R.id.tvDisplayName)
        val enter: TextView = v.findViewById(R.id.tvEnter)
        val preferences: TextView = v.findViewById(R.id.tvPreferences)
    }

    fun setUserList(users: List<UserTo>) {
        this.users = users.toMutableList()
        notifyDataSetChanged()
    }

    fun getUserList(): List<UserTo> {
        return users
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun setViewModel(model: DataViewModel) {
        this.model = model
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (users.isNotEmpty() && users.size > position) {

            val user = users[position]

            with(user) {
                holder.displayName.text = model.getUserDisplayName(uid)
                holder.enter.text = initEnter(Date().time)
                holder.preferences.text = preferences.toString()

                holder.itemView.setOnClickListener {
                    model.setDialog(user)
                    navController.navigate(R.id.chatFragment)
                }
            }
        }
    }

    private fun initEnter(date: Long): String {
        return "Online"
    }
}