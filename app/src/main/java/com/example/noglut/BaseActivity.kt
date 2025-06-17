package com.example.noglut

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class BaseActivity : AppCompatActivity() {
    protected fun setupToolbar(titleText: String = "") {
        val customToolbarInclude: View = findViewById(R.id.custom_toolbar)
        val toolbar: Toolbar = customToolbarInclude.findViewById(R.id.custom_toolbar)
        val backButton: ImageView = customToolbarInclude.findViewById(R.id.customBackButton)
        val title: TextView = customToolbarInclude.findViewById(R.id.toolbarTitle)

        title.text = titleText
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        backButton.setOnClickListener {
            finish()
        }
    }
}
