package com.example.noglut.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noglut.MainActivity
import com.example.noglut.R
import com.example.noglut.databinding.ActivityResetPasswordBinding
import com.example.noglut.network.base.SessionManager
import com.example.noglut.network.user.models.LoginResponse
import com.example.noglut.utilities.HttpStatusCode
import com.example.noglut.utilities.loader.Loader
import com.example.noglut.viewModels.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sessionManager = SessionManager(this)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonChangePassword.setOnClickListener {
            resetPassword()
        }

    }

    private fun resetPassword() {
        val newPassword = binding.edittextNewPassword.text.toString()
        val confirmPassword = binding.edittextConfirmPassword.text.toString()
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")

        when {
            newPassword.isEmpty() -> {
                binding.edittextNewPassword.error = "Password is required"
                binding.edittextNewPassword.requestFocus()
                return
            }
            !passwordPattern.matches(newPassword) -> {
                binding.edittextNewPassword.error = "Password must be 8+ chars, include upper, lower, digit & special char"
                binding.edittextNewPassword.requestFocus()
                return
            }
            confirmPassword.isEmpty() -> {
                binding.edittextConfirmPassword.error = "Please confirm your password"
                binding.edittextConfirmPassword.requestFocus()
                return
            }
            newPassword != confirmPassword -> {
                binding.edittextConfirmPassword.error = "Passwords do not match"
                binding.edittextConfirmPassword.requestFocus()
                return
            }
        }

        // Show loader
        Loader.showLoader(this)

        // Make network call
        viewModel.resetPassword(
            email =  intent.getStringExtra("email")!!,
            resetCode =  intent.getStringExtra("code")!!,
            newPassword = newPassword,
            confirmPassword = confirmPassword,
            onSuccess = { response ->
               Loader.hideLoader()
                if (response.statusCode == HttpStatusCode.OK.code) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Snackbar.make(binding.root, response.message!!, Snackbar.LENGTH_SHORT).show()
                }
            },
            onError = { errorMessage ->
                Loader.hideLoader()
                println("Error: $errorMessage")
            }
        )
    }
}