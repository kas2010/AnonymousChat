package com.alikhansoftware.anonymouschat.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alikhansoftware.anonymouschat.model.Dialog
import com.alikhansoftware.anonymouschat.model.Filter
import com.alikhansoftware.anonymouschat.model.Message
import com.alikhansoftware.anonymouschat.model.User
import com.alikhansoftware.anonymouschat.to.DialogTo
import com.alikhansoftware.anonymouschat.to.FilterTo
import com.alikhansoftware.anonymouschat.to.UserTo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class DataViewModel : ViewModel() {
    private var state = MutableLiveData<State>()
    private var user = MutableLiveData<UserTo>()
    private var users = MutableLiveData<MutableList<UserTo>>()
    private var dialog = MutableLiveData<DialogTo>()
    private var dialogs = MutableLiveData<MutableList<DialogTo>>()
    private var filter = MutableLiveData<FilterTo>()

    private val usersListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Users onChildAdded:" + snapshot.key!!)

            if (users.value == null) {
                users.value = mutableListOf()
            }

            val newUser = snapshot.getValue(User::class.java)

            newUser?.let {
                if (isUserFiltered(it)) {
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
                        if (isUserFiltered(it)) {
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

    private fun isUserFiltered(filteredUser: User): Boolean {
        //exclude own user
        if (filteredUser.id == user.value!!.id) {
            return false
        }
        //filtering
        user.value!!.filter?.let { filter ->
            //filter by gender
            if (filter.gender > 0) {
                if (filter.gender != filteredUser.gender) {
                    return false
                }
            }
            //filter by age
            if (!(filteredUser.age >= filter.ageFrom && filteredUser.age <= filter.ageTo)) {
                return false
            }
            //filter by city
            if (filter.checkCity && !filteredUser.city.isNullOrEmpty()) {
                if (!filteredUser.city.equals(filteredUser.city, ignoreCase = true)) {
                    return false
                }
            }
            //filter by preferences
            if (!filter.preferences.isNullOrEmpty()) {
                for (filter_pref in filter.preferences) {
                    var fl = false
                    for (pref in filteredUser.preferences) {
                        if (pref.equals(filter_pref, ignoreCase = true)) {
                            fl = true
                            break
                        }
                    }
                    if (!fl) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private val dialogsListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Dialogs onChildAdded: " + snapshot.key)

            if (dialogs.value == null) {
                dialogs.value = mutableListOf()
            }

            val newDialog = snapshot.getValue(Dialog::class.java)
            newDialog?.let {
                if (isDialogFiltered(it)) {
                    dialogs.value!!.add(DialogTo(snapshot.key.toString(), user.value!!, it))
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "Dialogs onChildChanged:" + snapshot.key!!)

            val editDialog = snapshot.getValue(Dialog::class.java)

            editDialog?.let {
                for ((index, d) in dialogs.value!!.withIndex()) {
                    if (d.uid == snapshot.key) {
                        dialogs.value!![index] = DialogTo(snapshot.key.toString(), user.value!!, it)
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

            val movedDialog = snapshot.getValue(Dialog::class.java)

            movedDialog?.let {
                for (d in dialogs.value!!) {
                    if (d.uid.equals(snapshot.key, ignoreCase = true)) {
                        d.uid = snapshot.key.toString()
                        break
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to load dialogs", error.toException())
        }
    }

    private fun isDialogFiltered(filteredDialog: Dialog): Boolean {
        var flag = false
        for (u in filteredDialog.users) {
            if (u == user.value!!.uid) {
                flag = true
                break
            }
        }
        if (!flag) return false

        return true
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
        Log.d(TAG, "The fun <loadUser> executed.")
        Log.d(TAG, FirebaseAuth.getInstance().uid ?: "null")
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
            id = FirebaseAuth.getInstance().currentUser!!.uid,
            name = name,
            gender = gender,
            age = age,
            city = city,
            preferences = preferences.toMutableList()
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

    fun getDialogs(): LiveData<MutableList<DialogTo>> {
        if (dialogs.value == null) {
            loadDialogs()
        }
        return dialogs
    }

    private fun loadDialogs() {
        FirebaseDatabase
            .getInstance()
            .getReference("dialogs")
            .removeEventListener(dialogsListener)

        FirebaseDatabase
            .getInstance()
            .getReference("dialogs")
            .addChildEventListener(dialogsListener)
    }

    fun getDialog(): LiveData<DialogTo> {
        return dialog
    }

    fun setDialog(dialog: DialogTo) {
        this.dialog.value = dialog
    }

    fun setDialog(userTo: UserTo) {
        //if dialog exist
        dialogs.value?.let { dialogs ->
            for (d in dialogs) {
                var flagOne = false
                var flagTwo = false
                for (u in d.users) {
                    if (u == user.value!!.uid) {
                        flagOne = true
                    }
                    if (u == userTo.uid) {
                        flagTwo = true
                    }
                    if (flagOne && flagTwo) {
                        dialog.value = d
                        return
                    }
                }

            }
        }

        //if dialog not exist
        val keyDialog = FirebaseDatabase
            .getInstance()
            .getReference("dialogs")
            .push()
            .key

        val newDialog = Dialog(
            users = mutableListOf(
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

        dialog.value = DialogTo(keyDialog, user.value!!, newDialog)
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
            .child(dialog.value!!.uid)
            .updateChildren(hashMapOf<String, Any>("lastMessage" to newMessage.toMap()))
    }

    fun getFilter(): LiveData<FilterTo> {
        if (filter.value == null) {
            loadFilter()
        }
        return filter
    }

    private fun loadFilter() {
        FirebaseDatabase
            .getInstance()
            .getReference("filters")
            .orderByChild("userId")
            .equalTo(user.value!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (filterSnapshot in snapshot.children) {
                            val existFilter = filterSnapshot.getValue(Filter::class.java)

                            existFilter?.let {
                                filter.value = FilterTo(snapshot.key.toString(), it)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "Failed to load filter: " + error.toException())
                }
            })
    }

    fun setFilter(
        gender: Int,
        ageFrom: Float,
        ageTo: Float,
        checkCity: Boolean,
        preferences: List<String>
    ) {
        val updateFilter = Filter(
            userId = user.value!!.uid,
            gender = gender,
            ageFrom = ageFrom,
            ageTo = ageTo,
            checkCity = checkCity,
            preferences = preferences.toMutableList())

        if (filter.value == null) {
            FirebaseDatabase
                .getInstance()
                .getReference("filters")
                .push()
                .setValue(updateFilter)

            return
        }

        FirebaseDatabase
            .getInstance()
            .getReference("filters")
            .child(filter.value!!.uid)
            .updateChildren(updateFilter.toMap())
    }

    companion object {
        private const val TAG = "DataViewModel"
    }
}