package com.feylabs.sumbangsih.util

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber as CatetLog

open class BaseFragment : Fragment() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

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

    fun hideActionBar() {
        this.activity?.actionBar?.hide()
        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
    }

    fun showActionBar() {
        this.activity?.actionBar?.show()
        (activity as AppCompatActivity?)?.getSupportActionBar()?.show()
    }

    fun hideSumbangsihToolbar() {
        try {
            (getActivity() as CommonControllerActivity).hideCustomTopbar()
        } catch (e: Exception) {
            showToast(e.toString())
        }
    }

    fun showSumbangsihToolbar() {
        try {
            (getActivity() as CommonControllerActivity).showCustomTopbar()
        } catch (e: Exception) {

        }
    }

    fun makeSrlLoading(value:Boolean=false){
        val srl = view?.findViewById<SwipeRefreshLayout>(R.id.srl)
        if(srl!=null){
            srl.isRefreshing = value
        }
    }


}