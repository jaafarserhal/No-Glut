package com.example.noglut.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noglut.R
import com.example.noglut.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = ""
        val greyColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.dark_grey))
        val linkColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.link_color))

        val resetpasswordHtmlText = "<font color='$greyColor'>${getString(R.string.forgot_password)}</font> " +
                "<font color='$linkColor'>${getString(R.string.reset)}</font>"

        binding.buttonResetPassword.text = Html.fromHtml(resetpasswordHtmlText, Html.FROM_HTML_MODE_LEGACY)


        val signupHtmlText = "<font color='$greyColor'>${getString(R.string.didn_t_have_an_account)}</font> " +
                "<font color='$linkColor'>${getString(R.string.sign_up)}</font>"

        binding.buttonSignup.text = Html.fromHtml(signupHtmlText, Html.FROM_HTML_MODE_LEGACY)
        binding.buttonSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}