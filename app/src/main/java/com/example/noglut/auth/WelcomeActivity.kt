package com.example.noglut.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.noglut.guide.StepByStepGuideActivity
import androidx.core.content.edit
import com.example.noglut.MainActivity
import com.example.noglut.databinding.ActivityWelcomeBinding
import com.example.noglut.network.base.SessionManager

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sessionManager = SessionManager(this)

        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val hasSeenGuide = sharedPref.getBoolean("hasSeenGuide", false)

        if (!hasSeenGuide) {
            // Launch the guide/tutorial screen
            startActivity(Intent(this, StepByStepGuideActivity::class.java))
            sharedPref.edit { putBoolean("hasSeenGuide", true) }
            finish()
        } else {
            if (sessionManager.isSessionValid()) {
                navigateToMainActivity()
                return
            }
            binding = ActivityWelcomeBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.buttonLoginEmail.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            binding.buttonSignup.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}