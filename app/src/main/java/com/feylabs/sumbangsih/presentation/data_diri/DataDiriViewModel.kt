package com.feylabs.sumbangsih.presentation.data_diri

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.*
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DataDiriViewModel(private val commonApiClient: CommonApiClient) : ViewModel() {


    private var _profileLiveData: MutableLiveData<ManyunyuRes<ProfileMainReq?>> =
        MutableLiveData()
    val profileLiveData get() = _profileLiveData

    fun getProfile(id: String) {
        _profileLiveData.postValue(ManyunyuRes.Loading())
        AndroidNetworking.get(BASE_URL + "user/$id")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val req = Gson().fromJson(response.toString(), ProfileMainReq::class.java)
                    _profileLiveData.postValue(ManyunyuRes.Success(req))
                }

                override fun onError(anError: ANError?) {
                    _profileLiveData.postValue(ManyunyuRes.Error(anError?.localizedMessage.toString()))
                }

            })
    }




}