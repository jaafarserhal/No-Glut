package com.example.noglut.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.noglut.R
import com.example.noglut.guide.StepByStepGuideActivity
import androidx.core.content.edit

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val hasSeenGuide = sharedPref.getBoolean("hasSeenGuide", false)

        if (!hasSeenGuide) {
            // Launch the guide/tutorial screen
            startActivity(Intent(this, StepByStepGuideActivity::class.java))
            sharedPref.edit { putBoolean("hasSeenGuide", true) }
            finish()
        } else setContentView(R.layout.activity_welcome)
    }
}