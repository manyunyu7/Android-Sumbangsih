package com.feylabs.sumbangsih.util

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import timber.log.Timber as CatetLog

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showToast(text: String?, isLong: Boolean = false) {
        if (isLong) {
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }


    fun String.showLongToast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_LONG).show()
    }

    fun View.makeViewVisible() {
        this.visibility = View.VISIBLE
    }

    fun View.makeViewGone() {
        this.visibility = View.GONE
    }

}