package com.example.noglut

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar

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

    public fun showTopToBottomSnackbar(view: View) {
        // Create a Snackbar
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)

        // Inflate the custom layout for the Snackbar
        val customSnackbarView = layoutInflater.inflate(R.layout.custom_snackbar, null)
        val snackbarText: TextView = customSnackbarView.findViewById(R.id.snackbar_text)
        snackbarText.text = "This is an animated Snackbar!"

        // Set the custom view for the Snackbar
        snackbar.view.visibility = View.INVISIBLE // Initially hide the default Snackbar
        val snackbarLayout = snackbar.view as ViewGroup
        snackbarLayout.addView(customSnackbarView, 0)

        // Ensure the Snackbar layout is fully measured before applying the translationY
        snackbar.view.post {
            // Apply the animation: Move from top to bottom
            snackbar.view.translationY = -snackbar.view.height.toFloat()
            snackbar.view.animate().translationY(0f).duration = 500

            // Make the view visible after setting up the animation
            snackbar.view.visibility = View.VISIBLE
        }

        // Show the Snackbar
        snackbar.show()
    }




}
