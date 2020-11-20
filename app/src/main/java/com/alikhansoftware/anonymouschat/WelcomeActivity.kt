package com.alikhansoftware.anonymouschat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        buttonApply.setOnClickListener {
            val intent = Intent()
            intent.putExtra("agreement", "adopt")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}