package com.example.noglut.utilities.loader

import android.content.Context

object Loader {

    private var currentLoader: LoaderDialog? = null

    fun showLoader(context: Context) {
        hideLoader()
        currentLoader = LoaderDialog(context)
        currentLoader?.show()
    }

    fun hideLoader() {
        currentLoader?.dismiss()
        currentLoader = null
    }
}