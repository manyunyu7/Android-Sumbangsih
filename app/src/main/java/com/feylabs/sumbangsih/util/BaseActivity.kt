package com.feylabs.sumbangsih.util

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber as CatetLog

open class BaseActivity : AppCompatActivity() {

    fun String.showLongToast() {
        Toast.makeText(applicationContext, this, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast() {
        Toast.makeText(applicationContext, this, Toast.LENGTH_LONG).show()
    }

    fun View.makeViewVisible() {
        this.visibility = View.VISIBLE
    }

    fun View.makeViewGone() {
        this.visibility = View.GONE
    }

    fun hideActionBar(){
        actionBar?.hide()
        supportActionBar?.hide();
    }

    fun showActionBar(){
        actionBar?.show()
        supportActionBar?.show();
    }

}