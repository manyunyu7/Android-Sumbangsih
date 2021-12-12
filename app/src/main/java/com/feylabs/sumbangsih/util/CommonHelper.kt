package com.feylabs.sumbangsih.util

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.presentation._walkthrough.WalkthroughActivity
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import android.app.Activity


object CommonHelper {

    const val PHOTO_TAG = "camerax"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"

    const val REQUEST_CODE_PERMISSION = 123

    val REQUIRED_PERMISSIONS_PHOTO_KTP = arrayOf(
        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    fun Fragment.logout() {
        val logoutIntent = Intent(this.activity, WalkthroughActivity::class.java)
        RazPreferences(this.requireContext()).clearPreferences()
        this?.activity?.finish()
        this.activity?.startActivity(logoutIntent)
    }

    fun logout(activity: Context) {
        val logoutIntent = Intent(activity, WalkthroughActivity::class.java)
        RazPreferences(activity).clearPreferences()
        (activity as Activity).finish()
        activity.startActivity(logoutIntent)
    }

}