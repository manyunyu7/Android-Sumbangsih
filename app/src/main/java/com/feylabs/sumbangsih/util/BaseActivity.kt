package com.feylabs.sumbangsih.util

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber as CatetLog

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun String.showLongToast() {
        Toast.makeText(applicationContext, this, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast() {
        Toast.makeText(applicationContext, this, Toast.LENGTH_LONG).show()
    }

    fun showToast(text:String,isLong:Boolean=false) {
        if(isLong){
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
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