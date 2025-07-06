package com.example.noglut.auth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noglut.BaseActivity
import com.example.noglut.MainActivity
import com.example.noglut.R
import com.example.noglut.databinding.ActivityLoginBinding
import com.example.noglut.network.base.SessionManager
import com.example.noglut.network.user.models.LoginResponse
import com.example.noglut.utilities.HttpStatusCode
import com.example.noglut.utilities.loader.Loader
import com.example.noglut.viewModels.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        setColoredTextWithClick(
            context = this,
            button = binding.buttonResetPassword,
            prefixResId = R.string.forgot_password,
            linkResId = R.string.reset,
            greyColorResId = R.color.dark_grey,
            linkColorResId = R.color.link_color,
            destinationActivity = SendResetPasswordCodeActivity::class.java
        )

        setColoredTextWithClick(
            context = this,
            button = binding.buttonSignup,
            prefixResId = R.string.didn_t_have_an_account,
            linkResId = R.string.sign_up,
            greyColorResId = R.color.dark_grey,
            linkColorResId = R.color.link_color,
            destinationActivity = RegisterActivity::class.java
        )

        binding.buttonLogin.setOnClickListener {
            makeLoginRequest()
        }
    }

    // Function to set a colored text with a clickable link
    private fun setColoredTextWithClick(
        context: Context,
        button: Button,
        prefixResId: Int,
        linkResId: Int,
        greyColorResId: Int,
        linkColorResId: Int,
        destinationActivity: Class<out Activity>
    ) {
        val greyColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, greyColorResId))
        val linkColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(context, linkColorResId))

        val htmlText = "<font color='$greyColor'>${context.getString(prefixResId)}</font> " +
                "<font color='$linkColor'>${context.getString(linkResId)}</font>"

        button.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        button.setOnClickListener {
            val intent = Intent(context, destinationActivity)
            context.startActivity(intent)
        }
    }

    private fun makeLoginRequest() {

        val email = binding.edittextEmail.text.toString().trim()
        val password = binding.edittextPassword.text.toString().trim()

        // Validate fields
        when {

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
        }

        // Show loader
        Loader.showLoader(this)


        // Make network call
        viewModel.login(
            email = email,
            password = password,
            onSuccess = { response ->
                Loader.hideLoader()

                if (response.statusCode == HttpStatusCode.OK.code) {
                    val gson = Gson()
                    val loginResponse = gson.fromJson(gson.toJson(response.data), LoginResponse::class.java)
                    sessionManager.saveUserSession(loginResponse)
                    val intent = Intent(this, MainActivity::class.java)
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