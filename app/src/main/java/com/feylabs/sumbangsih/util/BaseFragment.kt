package com.feylabs.sumbangsih.util

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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