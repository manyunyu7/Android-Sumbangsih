package com.feylabs.sumbangsih.presentation.komplain

import android.content.Context
import com.feylabs.sumbangsih.util.ObjectHelperCommon.serializeToMap
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.google.gson.Gson
import java.util.*

data class KomplainRequestBody(
    val type: String,
    val user_id: String,
    val dana_option: String? = null,
    val dana_excess: String? = null,
    val rejected_at: String? = null,
    val feedback: String? = null,
    var photo: String? = null,
    val notes: String? = null,
)

object KomplainHelper {

    fun savePref(context: Context, objects: KomplainRequestBody) {
        val obj = Gson().toJson(objects)
        RazPreferences(context).save("komplains", obj)
    }

    fun asMap(string: KomplainRequestBody, context: Context): Map<String, Any> {
        return getObject(string).serializeToMap()
    }

    fun getObject(objects: KomplainRequestBody): String {
        val obj = Gson().toJson(objects, KomplainRequestBody::class.java)
        return obj
    }
}
