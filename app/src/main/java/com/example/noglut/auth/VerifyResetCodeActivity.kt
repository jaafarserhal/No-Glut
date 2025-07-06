package com.example.noglut.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noglut.R
import com.example.noglut.network.base.SessionManager
import com.example.noglut.utilities.HttpStatusCode
import com.example.noglut.viewModels.auth.AuthViewModel
import com.example.noglut.databinding.ActivityVerifyResetCodeBinding
import com.example.noglut.utilities.loader.Loader
import com.google.android.material.snackbar.Snackbar

class VerifyResetCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyResetCodeBinding
    private lateinit var etList: List<EditText>
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerifyResetCodeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etList = listOf(
            binding.et1,
            binding.et2,
            binding.et3,
            binding.et4,
            binding.et5,
            binding.et6
        )
        setupOtpInputs()
    }
    private fun setupOtpInputs() {
        for (i in etList.indices) {
            etList[i].apply {
                // Limit to 1 digit
                filters = arrayOf(InputFilter.LengthFilter(1))
                inputType = InputType.TYPE_CLASS_NUMBER

                // Move focus on input
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s?.length == 1 && i < etList.size - 1) {
                            etList[i + 1].requestFocus()
                        } else if (s?.isEmpty() == true && i > 0) {
                            // do nothing here, we handle backspace below
                        }
                        checkAndUpdateCode()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                // Handle backspace to move to previous input
                setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL &&
                        event.action == KeyEvent.ACTION_DOWN &&
                        text.isEmpty() && i > 0
                    ) {
                        etList[i - 1].requestFocus()
                        etList[i - 1].text = null
                        return@setOnKeyListener true
                    }
                    false
                }
            }
        }
    }

    private fun checkAndUpdateCode() {
        val code = etList.joinToString("") { it.text.toString() }
        if (code.length == 6) {
            submitCode(code)
        }
    }


    private fun submitCode(code: String) {
        val email = intent.getStringExtra("email")!!
        Loader.showLoader(this)

        viewModel.verifyResetPasswordCode(
            email = email,
            resetCode = code,
            onSuccess = { response ->
                Loader.hideLoader()
                if (response.statusCode == HttpStatusCode.OK.code) {
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("code", code)
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