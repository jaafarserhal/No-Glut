package com.example.noglut.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noglut.BaseActivity
import com.example.noglut.MainActivity
import com.example.noglut.R
import com.example.noglut.databinding.ActivityRegisterBinding
import com.example.noglut.network.base.SessionManager
import com.example.noglut.network.user.models.LoginResponse
import com.example.noglut.utilities.HttpStatusCode
import com.example.noglut.viewModels.auth.AuthViewModel
import com.google.gson.Gson


class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        binding.buttonSignup.setOnClickListener {
            makeRegistrationRequest()
        }
    }

    private fun makeRegistrationRequest() {
        val firstName = binding.edittextFirstName.text.toString().trim()
        val lastName = binding.edittextLastName.text.toString().trim()
        val email = binding.edittextEmail.text.toString().trim()
        val password = binding.edittextPassword.text.toString().trim()
        val confirmPassword = binding.edittextConfirmPassword.text.toString().trim()
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")

        // Validate fields
        when {
            firstName.isEmpty() -> {
                binding.edittextFirstName.error = "First name is required"
                binding.edittextFirstName.requestFocus()
                return
            }
            lastName.isEmpty() -> {
                binding.edittextLastName.error = "Last name is required"
                binding.edittextLastName.requestFocus()
                return
            }
            email.isEmpty() -> {
                binding.edittextEmail.error = "Email is required"
                binding.edittextEmail.requestFocus()
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.edittextEmail.error = "Invalid email format"
                binding.edittextEmail.requestFocus()
                return
            }
            password.isEmpty() -> {
                binding.edittextPassword.error = "Password is required"
                binding.edittextPassword.requestFocus()
                return
            }
            !passwordPattern.matches(password) -> {
                binding.edittextPassword.error = "Password must be 8+ chars, include upper, lower, digit & special char"
                binding.edittextPassword.requestFocus()
                return
            }
            confirmPassword.isEmpty() -> {
                binding.edittextConfirmPassword.error = "Please confirm your password"
                binding.edittextConfirmPassword.requestFocus()
                return
            }
            password != confirmPassword -> {
                binding.edittextConfirmPassword.error = "Passwords do not match"
                binding.edittextConfirmPassword.requestFocus()
                return
            }
        }

        // Show loader
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Registering...")
            setCancelable(false)
            show()
        }

        // Make network call
        viewModel.registerUser(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            onSuccess = { response ->
                progressDialog.dismiss()
                if (response.statusCode == HttpStatusCode.OK.code) {
                    val gson = Gson()
                    val loginResponse = gson.fromJson(gson.toJson(response.data), LoginResponse::class.java)
                    sessionManager.saveUserSession(loginResponse)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
            },
            onError = { errorMessage ->
                progressDialog.dismiss()
                println("Error: $errorMessage")
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        )
    }
}