package com.feylabs.sumbangsih.util.sharedpref

import android.content.Context
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_CONTACT
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_EMAIL
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_GENDER
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_NAME
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_NIK
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_PHOTO
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.USER_USERNAME
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences as Preference

object RazPreferenceHelper {

    fun updateUserPreference(
        context: Context,
        name: String,
        username: String,
        email: String,
        contact: String,
        gender: String,
        photo: String,
        nik: String
    ) {
        Preference(context).save(USER_NAME, name)
        Preference(context).save(USER_USERNAME, username.toString())
        Preference(context).save(USER_EMAIL, email.toString())
        Preference(context).save(USER_CONTACT, contact.toString())
        Preference(context).save(USER_GENDER, contact.toString())
        Preference(context).save(USER_PHOTO, photo.toString())
        Preference(context).save(USER_NIK, nik.toString())
    }

    fun setFirstTimeFalse(context: Context) {
        Preference(context).apply {
            save("FIRST_TIME", "DSDS")
        }
    }

    fun isFirstTime(context: Context): Boolean {
        Preference(context).apply {
            return !this.sharedPref.contains("FIRST_TIME")
        }
    }

    fun setLoggedIn(context: Context) {
        Preference(context).apply {
            save("LOGGED_IN", true)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        Preference(context).apply {
            return getPrefBool("LOGGED_IN") ?: false
        }
    }

    fun savePhoneNumber(context: Context, number: String) {
        Preference(context).apply {
            save("CURRENT_PHONE_NUMBER", number)
        }
    }

    fun getPhoneNumber(context: Context): String {
        Preference(context).apply {
            val number = getPrefString("CURRENT_PHONE_NUMBER").orEmpty()
            return if (number.isNotEmpty())
                number
            else ""
        }
    }


}