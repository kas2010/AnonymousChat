package com.alikhansoftware.anonymouschat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivityForResult(Intent(this, WelcomeActivity::class.java), WELCOME_REQUEST_CODE)
        }

        val navController = findNavController(R.id.nav_host_fragment_container)

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    Log.d(TAG, "Select menu Home")
                    navController.navigate(R.id.mainFragment)
                    true
                }
                R.id.menuSearch -> {
                    Log.d(TAG, "Select menu Search")
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.menuDialog -> {
                    Log.d(TAG, "Select menu Dialog")
                    navController.navigate(R.id.dialogFragment)
                    true
                }
                R.id.menuProfile -> {
                    Log.d(TAG, "Select menu Profile")
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WELCOME_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.let {
                val result = it.getStringExtra("agreement")
                if (result == "adopt") {
                    FirebaseAuth.getInstance().signInAnonymously()
                    return
                }
            }
        }
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val WELCOME_REQUEST_CODE = 1001
    }
}