package com.feylabs.sumbangsih.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.HomeStatsResponse
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.data.source.remote.web.NewsApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson

class HomeViewModel(val newsApiClient: NewsApiClient) : ViewModel() {

    private var _newsLiveData: MutableLiveData<ManyunyuRes<NewsResponse?>> =
        MutableLiveData()
    val newsLiveData get() = _newsLiveData

    private var _statsLiveData: MutableLiveData<ManyunyuRes<HomeStatsResponse?>> =
        MutableLiveData()
    val statsLiveData get() = _statsLiveData

    private var _profileLiveData: MutableLiveData<ManyunyuRes<ProfileMainReq?>> =
        MutableLiveData()
    val profileLiveData get() = _profileLiveData

    fun getStatsData() {
        _statsLiveData.postValue(ManyunyuRes.Loading())

        AndroidNetworking.get(BASE_URL + "stats")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val res = Gson().fromJson(response, HomeStatsResponse::class.java)
                    _statsLiveData.postValue(ManyunyuRes.Success(res))
                }

                override fun onError(anError: ANError?) {
                    _statsLiveData.postValue(ManyunyuRes.Error(anError?.localizedMessage.toString()))
                }

            })
    }

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

    fun getNews() {
        _newsLiveData.postValue(ManyunyuRes.Loading())
        AndroidNetworking.get(BASE_URL + "news/get")
            .setPriority(Priority.IMMEDIATE)
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