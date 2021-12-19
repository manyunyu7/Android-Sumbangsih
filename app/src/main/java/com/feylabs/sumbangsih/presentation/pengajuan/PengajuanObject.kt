package com.feylabs.sumbangsih.presentation.pengajuan.with_sku

import android.content.Context
import com.feylabs.sumbangsih.util.ObjectHelperCommon
import com.feylabs.sumbangsih.util.ObjectHelperCommon.convert
import com.feylabs.sumbangsih.util.ObjectHelperCommon.serializeToMap
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.google.gson.Gson
import org.json.JSONObject

data class PengajuanSKU(
    val event_id: String,
    val user_id: String,
    var photo_selfie: String? = null,
    var photo_usaha: String? = null,
    var lat_selfie: String? = null,
    var long_selfie: String? = null,
    var lat_usaha: String? = null,
    var long_usaha: String? = null,
    var nama_usaha: String? = null,
    var type: String = "sku",
)

object PengajuanSKUObjectHelper {

    fun savePref(context: Context, pengajuanSKU: PengajuanSKU?) {
        val obj = Gson().toJson(pengajuanSKU)
        RazPreferences(context).save("pengajuanSKUzzz", obj)
    }

    fun asMap(context: Context): Map<String, Any> {
        return getObject(context).serializeToMap()
    }

    fun getObject(context: Context): PengajuanSKU? {
        val objPref = RazPreferences(context).getPrefString("pengajuanSKUzzz")
        val obj = Gson().fromJson(objPref, PengajuanSKU::class.java)
        return obj
    }
}

object PengajuanWithoutSKUObjectHelper {

    fun savePref(context: Context, pengajuanWithoutSKU: PengajuanWithoutSKU?) {
        val obj = Gson().toJson(pengajuanWithoutSKU)
        RazPreferences(context).save("pengajuanWithoutSKUzzz", obj)
    }

    fun asMap(context: Context): Map<String, Any> {
        return getObject(context).serializeToMap()
    }

    fun getObject(context: Context): PengajuanWithoutSKU? {
        val objPref = RazPreferences(context).getPrefString("pengajuanWithoutSKUzzz")
        val obj = Gson().fromJson(objPref, PengajuanWithoutSKU::class.java)
        return obj
    }
}

data class PengajuanWithoutSKU(
    val event_id: String,
    val user_id: String,
    var photo_usaha: String? = null,
    var lat_selfie: String? = null,
    var long_selfie: String? = null,
    var lat_usaha: String? = null,
    var long_usaha: String? = null,
    var nama_usaha: String? = null,
    var nib: String? = null,
    var type: String = "nosku",
)


