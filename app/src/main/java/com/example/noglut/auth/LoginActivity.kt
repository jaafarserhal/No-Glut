package com.example.noglut.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noglut.BaseActivity
import com.example.noglut.R
import com.example.noglut.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

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
        setupToolbar()
        setColoredTextWithClick(
            context = this,
            button = binding.buttonResetPassword,
            prefixResId = R.string.forgot_password,
            linkResId = R.string.reset,
            greyColorResId = R.color.dark_grey,
            linkColorResId = R.color.link_color,
            destinationActivity = ResetPasswordActivity::class.java
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

}