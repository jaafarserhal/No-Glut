package com.example.noglut.utilities.loader

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.graphics.toColorInt
import androidx.core.graphics.drawable.toDrawable

class LoaderDialog(context: Context) : Dialog(context) {

    private lateinit var loader: CustomLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Create the layout programmatically
        val layout = RelativeLayout(context).apply {
            setBackgroundColor("#80000000".toColorInt()) // Semi-transparent background
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Create and add the loader
        loader = CustomLoader(context).apply {
            layoutParams = RelativeLayout.LayoutParams(200, 200).apply {
                addRule(RelativeLayout.CENTER_IN_PARENT)
            }
        }

        layout.addView(loader)
        setContentView(layout)

        // Make dialog non-cancelable and full screen
        setCancelable(false)
        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    override fun dismiss() {
        loader.stopAnimation()
        super.dismiss()
    }
}