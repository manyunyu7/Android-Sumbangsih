package com.feylabs.sumbangsih.presentation.ktp_verif.model_json

import android.content.Context
import android.os.Parcelable
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class KTPVerifReq(
    var nik: String,
    var photo_requested: String,
    var photo_face: String
) : Parcelable

object VerifNIKHelper {

    fun savePref(context: Context, ktpVerifObject: KTPVerifReq?) {
        val obj = Gson().toJson(ktpVerifObject)
        RazPreferences(context).save("nik_verif", obj)
    }

    fun getKTPVerifReq(context: Context): KTPVerifReq {
        val objPref = RazPreferences(context).getPrefString("nik_verif")
        val obj = Gson().fromJson(objPref, KTPVerifReq::class.java)
        return obj
    }
}