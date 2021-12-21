package com.feylabs.sumbangsih.util.sharedpref

import android.content.Context
import android.content.SharedPreferences

class RazPreferences(context: Context) {

    private val PREFS_NAME = "kotlinpref"
    val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()


    fun save(KEY_NAME: String, value: Int) {
        editor.putInt(KEY_NAME, value)
        editor.commit()
    }

    fun removeKey(KEY_NAME: String) {
        editor.remove(KEY_NAME)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: String) {
        editor.putString(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: Boolean) {
        editor.putBoolean(KEY_NAME, value)
        editor.commit()
    }

    fun getPrefBool(KEY_NAME: String): Boolean {
        if (sharedPref.contains(KEY_NAME)) {
            return sharedPref.getBoolean(KEY_NAME, false)
        } else
            return false
    }

    fun getPrefString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getPrefInt(KEY_NAME: String): Int? {
        return sharedPref.getInt(KEY_NAME, -9999)
    }

    fun clearPreferences() {
        editor.clear()
        editor.commit()
//        val keys = sharedPref.all
//        keys.forEach {
//            if(it.key=="IS_LOGGEDn"){
//
//            }else{
//                editor.remove(it.key)
//                editor.commit()
//            }
//        }
    }

}