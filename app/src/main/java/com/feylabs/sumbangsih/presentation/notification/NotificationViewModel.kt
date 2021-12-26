package com.feylabs.sumbangsih.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.MNotificatinRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import com.feylabs.sumbangsih.data.source.remote.web.NewsApiClient
import com.feylabs.sumbangsih.di.ServiceLocator
import com.google.gson.Gson
import timber.log.Timber

class NotificationViewModel(
    commonApiClient: CommonApiClient,
) : ViewModel() {

    private var _notifLiveData: MutableLiveData<ManyunyuRes<MNotificatinRes?>> =
        MutableLiveData()
    val notifLiveData get() = _notifLiveData


    fun getNotif(userId: String) {
        _notifLiveData.postValue(ManyunyuRes.Loading())

        AndroidNetworking.get(ServiceLocator.BASE_URL + "mnotification/user/$userId")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val model = Gson().fromJson(
                        response.toString(),
                        MNotificatinRes::class.java
                    )
                    _notifLiveData.postValue(ManyunyuRes.Success(model))
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN ERROR $anError")
                    _notifLiveData.postValue(ManyunyuRes.Error(anError?.localizedMessage.toString()))
                }

            })

    }


}