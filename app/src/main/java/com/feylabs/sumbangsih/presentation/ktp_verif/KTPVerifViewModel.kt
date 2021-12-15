package com.feylabs.sumbangsih.presentation.ktp_verif

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.FindNIKResponse
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson
import timber.log.Timber

class KTPVerifViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private var _findKTPVm = MutableLiveData<ManyunyuRes<FindNIKResponse>>(ManyunyuRes.Default())
    val findKTPVm get() = _findKTPVm as MutableLiveData<ManyunyuRes<FindNIKResponse>>

    fun findKTPByNIK(nik: String) {
        _findKTPVm.value = ManyunyuRes.Loading()
        val fanURL = BASE_URL + "ktp/findNikMobile/$nik"
        Timber.d("FAN VERIF NIK URL :  $fanURL")
        AndroidNetworking.get(fanURL)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    try {
                        val res = Gson().fromJson(response.toString(), FindNIKResponse::class.java)
                        Timber.d("FAN VERIF NIK $res")
                        val message = res.messageId
                        if (res.apiCode == 1) {
                            _findKTPVm.postValue(ManyunyuRes.Success(res))
                        } else {
                            _findKTPVm.postValue(ManyunyuRes.Error(message))
                        }
                    } catch (e: Exception) {
                        _findKTPVm.postValue(ManyunyuRes.Error(e.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN VERIF NIK ERROR ${anError.toString()}")
                    Timber.d("FAN VERIF NIK ERROR ${anError?.errorBody.toString()}")
                    _findKTPVm.postValue(ManyunyuRes.Error(anError?.message.toString()))
                }

            })
    }

}