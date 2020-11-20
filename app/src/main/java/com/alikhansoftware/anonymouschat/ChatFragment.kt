package com.alikhansoftware.anonymouschat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alikhansoftware.anonymouschat.model.Message
import com.alikhansoftware.anonymouschat.data.DataViewModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.message_item.view.*

class ChatFragment : Fragment() {
    private lateinit var model: DataViewModel
    private lateinit var adapter: FirebaseRecyclerAdapter<Message, MessageViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            model = ViewModelProvider(activity).get(DataViewModel::class.java)

            model.getDialog().observe(viewLifecycleOwner, { dialog ->
                displayMessages(dialog.uid!!)

                buttonOK.setOnClickListener {
                    model.createMessage(dialog.userId!!, editMessageText.text.toString())
                    editMessageText.setText("")
                }
            })
        }
    }

    private fun displayMessages(dialogId: String) {
        Log.d(TAG, "Run function displayMessages()...")

        val ref = FirebaseDatabase
            .getInstance()
            .getReference("messages")
            .orderByChild("dialogId")
            .equalTo(dialogId)

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(ref, Message::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_item, parent, false)
                return MessageViewHolder(v)
            }

            override fun onBindViewHolder(
                holder: MessageViewHolder,
                position: Int,
                model: Message
            ) {
                holder.bindMessage(model)
            }

        }

        messageRecyclerView.adapter = adapter
        messageRecyclerView.layoutManager = LinearLayoutManager(activity)
        messageRecyclerView.setHasFixedSize(true)

        adapter.startListening()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindMessage(message: Message?) {
            with(message!!) {
                itemView.tvMessageText.text = text
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        private const val TAG = "ChatFragment"
    }
}