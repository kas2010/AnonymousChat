package com.alikhansoftware.anonymouschat.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alikhansoftware.anonymouschat.model.Dialog
import com.alikhansoftware.anonymouschat.model.Filter
import com.alikhansoftware.anonymouschat.model.Message
import com.alikhansoftware.anonymouschat.model.User
import com.alikhansoftware.anonymouschat.to.UserTo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class DataViewModel : ViewModel() {
    private var state = MutableLiveData<State>()
    private var user = MutableLiveData<UserTo>()
    private var users = MutableLiveData<MutableList<UserTo>>()
    private var dialog = MutableLiveData<Dialog>()
    private var dialogs = MutableLiveData<MutableList<Dialog>>()

    private val usersListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Users onChildAdded:" + snapshot.key!!)

            if (users.value == null) {
                users.value = mutableListOf()
            }

            val newUser = snapshot.getValue(User::class.java)

            newUser?.let {
                if (isUserFiltered(it.id, it.gender, it.age, user.value!!.city, it.preferences)) {
                    users.value!!.add(UserTo(snapshot.key.toString(), it))
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Users onChildChanged:" + snapshot.key!!)
            val editUser = snapshot.getValue(User::class.java)
            editUser?.let {
                for ((index, u) in users.value!!.withIndex()) {
                    if (u.uid.equals(snapshot.key, ignoreCase = true)) {
                        if (isUserFiltered(
                                it.id,
                                u.gender,
                                u.age,
                                user.value!!.city,
                                u.preferences
                            )
                        ) {
                            users.value!![index] = UserTo(snapshot.key.toString(), it)
                        } else {
                            users.value!!.removeAt(index)
                        }
                        break
                    }
                }
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d(TAG, "Users onChildRemoved:" + snapshot.key!!)
            val removedUser = snapshot.getValue(User::class.java)
            removedUser?.let {
                for ((index, u) in users.value!!.withIndex()) {
                    if (u.uid.equals(snapshot.key, ignoreCase = true)) {
                        users.value!!.removeAt(index)
                        break
                    }
                }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Users onChildMoved:" + snapshot.key!!)
            val movedUser = snapshot.getValue(User::class.java)
            movedUser?.let {
                for (u in users.value!!) {
                    if (u.uid.equals(snapshot.key, ignoreCase = true)) {
                        u.uid = snapshot.key.toString()
                        break
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to load users", error.toException())
        }
    }

    private fun isUserFiltered(
        id: String?,
        gender: Int, age: Int, city: String?,
        preferences: List<String>
    ): Boolean {
        var isFiltered = true
        //exclude own user
        if (id == user.value!!.id) {
            return false
        }
        //filtering
        user.value?.filter?.let { filter ->
            //filter by gender
            if (filter.gender > 0) {
                if (filter.gender != gender) {
                    isFiltered = false
                }
            }
            //filter by age
            if (!(age >= filter.ageFrom && age <= filter.ageTo)) {
                isFiltered = false
            }
            //filter by city
            if (filter.checkCity && !city.isNullOrEmpty()) {
                if (!city.equals(city, ignoreCase = true)) {
                    isFiltered = false
                }
            }
            //filter by preferences
            if (!filter.preferences.isNullOrEmpty()) {
                for (filter_pref in filter.preferences) {
                    var fl = false
                    for (pref in preferences) {
                        if (pref.equals(filter_pref, ignoreCase = true)) {
                            fl = true
                            break
                        }
                    }
                    if (!fl) {
                        isFiltered = false
                        break
                    }
                }
            }
        }
        return isFiltered
    }

    private val dialogsListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Dialogs onChildAdded: " + snapshot.key)

            if (dialogs.value == null) {
                dialogs.value = mutableListOf()
            }

            val newDialog = snapshot.getValue(Dialog::class.java)
            newDialog?.let {
                dialogs.value!!.add(it)
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Dialogs onChildChanged:" + snapshot.key!!)
            val editDialog = snapshot.getValue(Dialog::class.java)
            editDialog?.let {
                for ((index, d) in dialogs.value!!.withIndex()) {
                    if (d.uid == snapshot.key) {
                        dialogs.value!![index] = it
                        break
                    }
                }
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d(TAG, "Dialogs onChildRemoved:" + snapshot.key!!)
            val removedDialog = snapshot.getValue(Dialog::class.java)
            removedDialog?.let {
                for ((index, d) in dialogs.value!!.withIndex()) {
                    if (d.uid == snapshot.key) {
                        dialogs.value!!.removeAt(index)
                        break
                    }
                }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Dialogs onChildMoved:" + snapshot.key!!)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to load dialogs", error.toException())
        }
    }

    enum class State {
        UNDEFINED,
        USER_NOT_EXIST,
        USER_EXIST
    }

    init {
        state.value = State.UNDEFINED
    }

    fun getState(): LiveData<State> {
        if (user.value == null) {
            loadUser()
        }
        return state
    }


    fun getUser(): LiveData<UserTo> {
        return user
    }

    private fun loadUser() {
        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .orderByChild("id")
            .equalTo(FirebaseAuth.getInstance().uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val existUser = userSnapshot.getValue(User::class.java)
                            existUser?.let {
                                it.enterDate = Date().time
                                user.value = UserTo(userSnapshot.key.toString(), it)
                                state.value = State.USER_EXIST
                            }
                            break
                        }
                        return
                    }
                    state.value = State.USER_NOT_EXIST
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to load current user", error.toException())
                }
            })
    }

    fun setUser(name: String, gender: Int, age: Int, city: String, preferences: List<String>) {
        val updateUser = User(
            FirebaseAuth.getInstance().uid,
            name,
            gender,
            age,
            city,
            preferences.toMutableList()
        )

        if (user.value == null) {
            FirebaseDatabase
                .getInstance()
                .getReference("users")
                .push()
                .setValue(updateUser)
            return
        }

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user.value!!.uid)
            .updateChildren(updateUser.toMap())
    }

    fun getUserDisplayName(userId: String?): String {
        users.value?.let { users ->
            for (user in users) {
                if (user.uid == userId) {
                    return "${user.name} ,${user.age}, ${user.city}"
                }
            }
        }
        return "Безымянный пользователь"
    }

    fun getUsers(): LiveData<MutableList<UserTo>> {
        if (users.value == null) {
            loadUsers()
        }
        return users
    }

    private fun loadUsers() {
        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .removeEventListener(usersListener)

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .addChildEventListener(usersListener)
    }

    fun getDialogs(): LiveData<MutableList<Dialog>> {
        if (dialogs.value == null) {
            loadDialogs()
        }
        return dialogs
    }

    private fun loadDialogs() {
        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user.value!!.uid)
            .child("dialogs")
            .removeEventListener(dialogsListener)

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user.value!!.uid)
            .child("dialogs")
            .addChildEventListener(dialogsListener)
    }

    fun getDialog(): LiveData<Dialog> {
        return dialog
    }

    fun setDialog(dialog: Dialog) {
        this.dialog.value = dialog
    }

    fun setDialog(userTo: UserTo) {
        //exist dialog
        dialogs.value?.let { dialogs ->
            for (d in dialogs) {
                val collection = d.users.filter { u -> u == user.value!!.uid || u == userTo.uid }
                    .toCollection(mutableListOf())
                collection.size
            }
        }

        //no exist dialog
        val keyDialog = FirebaseDatabase
            .getInstance()
            .getReference("dialogs")
            .push()
            .key

        val newDialog = Dialog(
            uid = keyDialog,
            users = mutableListOf<String>(
                user.value!!.uid,
                userTo.uid
            ),
            lastMessage = null
        )

        FirebaseDatabase
            .getInstance()
            .getReference("dialogs")
            .child(keyDialog!!)
            .setValue(newDialog)

        dialog.value = newDialog
    }

    fun createMessage(selectedUserId: String, text: String) {
        val newMessage = Message(
            dialogId = dialog.value!!.uid,
            sendUserId = user.value!!.uid,
            receiveUserId = selectedUserId,
            text = text
        )

        FirebaseDatabase
            .getInstance()
            .getReference("messages")
            .push()
            .setValue(newMessage)

        FirebaseDatabase
            .getInstance()
            .getReference("dialogs")
            .child(dialog.value!!.uid!!)
            .updateChildren(hashMapOf<String, Any>("lastMessage" to newMessage.toMap()))
    }

    fun setFilter(
        gender: Int,
        ageFrom: Float,
        ageTo: Float,
        checkCity: Boolean,
        preferences: List<String>
    ) {
        val newFilter = Filter(gender, ageFrom, ageTo, checkCity, preferences.toMutableList())

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user.value!!.uid)
            .child("filter")
            .setValue(newFilter)
    }

    companion object {
        private const val TAG = "DataViewModel"
    }
}