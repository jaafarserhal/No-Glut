package com.example.noglut.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noglut.BaseActivity
import com.example.noglut.R
import com.example.noglut.databinding.ActivitySendResetPasswordCodeBinding
import com.example.noglut.utilities.HttpStatusCode
import com.example.noglut.utilities.loader.Loader
import com.example.noglut.viewModels.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar

class SendResetPasswordCodeActivity : BaseActivity(){
    private lateinit var binding: ActivitySendResetPasswordCodeBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySendResetPasswordCodeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        binding.buttonResetPassword.setOnClickListener {
            sendResetPasswordCode()
        }
    }

    private fun sendResetPasswordCode() {
        val email = binding.edittextEmail.text.toString().trim()

        if (email.isEmpty()) {
            binding.edittextEmail.error = "Email is required"
            binding.edittextEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edittextEmail.error = "Please enter a valid email address"
            binding.edittextEmail.requestFocus()
            return
        }

        // Show loader
        Loader.showLoader(this)

        viewModel.sendResetPasswordCode(
            email = email,

            onSuccess = { response ->
              Loader.hideLoader()
                if (response.statusCode == HttpStatusCode.OK.code) {
                    val intent = Intent(this, VerifyResetCodeActivity::class.java)
                    intent.putExtra("email", email)
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