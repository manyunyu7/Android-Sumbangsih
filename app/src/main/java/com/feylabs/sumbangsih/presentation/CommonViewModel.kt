package com.feylabs.sumbangsih.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.feylabs.sumbangsih.data.source.remote.web.NewsApiClient
import com.feylabs.sumbangsih.di.ServiceLocator
import com.google.gson.Gson
import timber.log.Timber

class CommonViewModel(
    authApiClient: AuthApiClient,
    newsApiClient: NewsApiClient
) : ViewModel() {

    private var _newsLiveData: MutableLiveData<ManyunyuRes<NewsResponse?>> =
        MutableLiveData()
    val newsLiveData get() = _newsLiveData


    fun getNews() {
        _newsLiveData.postValue(ManyunyuRes.Loading())

        AndroidNetworking.get(ServiceLocator.BASE_URL + "news/get")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val model = Gson().fromJson(
                        response.toString(),
                        NewsResponse::class.java
                    )
                    _newsLiveData.postValue(ManyunyuRes.Success(model))
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN ERROR $anError")
                    _newsLiveData.postValue(ManyunyuRes.Error(anError?.localizedMessage.toString()))
                }

            })

        /*
        Retrofit Error Pak
        viewModelScope.launch {
            try {
                val req = newsApiClient.fetchNews()
                val body = req.body()
                if (req.isSuccessful) {
                    if (body != null) {
                        if (!body.isEmpty()) {
                            _newsLiveData.postValue(ManyunyuRes.Success(body))
                        } else {
                            _newsLiveData.postValue(ManyunyuRes.Error("Data Tidak Tersedia"))
                        }
                    }else{
                        _newsLiveData.postValue(ManyunyuRes.Error("Body Null"))
                    }
                } else {
                    _newsLiveData.postValue(ManyunyuRes.Error("Terjadi Kesalahan"))
                }

            } catch (e: Exception) {
                _newsLiveData.postValue(ManyunyuRes.Error(e.localizedMessage.toString()))
            }
        }
         */
    }
}