package com.alikhansoftware.anonymouschat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alikhansoftware.anonymouschat.R
import com.alikhansoftware.anonymouschat.data.DataViewModel
import com.alikhansoftware.anonymouschat.model.Dialog

class DialogAdapter : RecyclerView.Adapter<DialogAdapter.MyViewHolder>() {
    private var dialogs = mutableListOf<Dialog>()
    private lateinit var navController: NavController
    private lateinit var model: DataViewModel

    class MyViewHolder(v: View) :
        RecyclerView.ViewHolder(v) {
        val avatar: ImageView = v.findViewById(R.id.tvAvatar)
        val displayName: TextView = v.findViewById(R.id.tvDisplayName)
        val lastMessage: TextView = v.findViewById(R.id.tvLastMessage)
    }

    fun setDialogList(dialogs: List<Dialog>) {
        this.dialogs = dialogs.toMutableList()
        notifyDataSetChanged()
    }

    fun getDialogList(): List<Dialog> {
        return dialogs
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun setViewModel(model: DataViewModel) {
        this.model = model
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dialogs.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (dialogs.isNotEmpty() && dialogs.size > position) {

            val dialog = dialogs[position]

            with(dialog) {
                holder.displayName.text = model.getUserDisplayName(userId)
                holder.lastMessage.text = lastMessage?.text

                holder.itemView.setOnClickListener {
                    model.setDialog(dialog)
                    navController.navigate(R.id.chatFragment)
                }
            }
        }
    }
}