package com.feylabs.sumbangsih.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.data.source.remote.web.NewsApiClient
import kotlinx.coroutines.launch

class HomeViewModel(val newsApiClient: NewsApiClient) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private var _newsLiveData: MutableLiveData<ManyunyuRes<NewsResponse?>> =
        MutableLiveData()
    val newsLiveData get() = _newsLiveData

    fun getNews() {
        _newsLiveData.postValue(ManyunyuRes.Loading())
        viewModelScope.launch {
            val req = newsApiClient.fetchNews()
            try {
                val body = req.body()
                if (req.isSuccessful) {
                    if (body != null) {
                        if (!body.isEmpty()) {
                            _newsLiveData.postValue(ManyunyuRes.Success(body))
                        } else {
                            _newsLiveData.postValue(ManyunyuRes.Empty())
                        }
                    }
                } else {
                    _newsLiveData.postValue(ManyunyuRes.Error("Terjadi Kesalahan"))
                }

            } catch (e: Exception) {
                _newsLiveData.postValue(ManyunyuRes.Error(e.localizedMessage.toString()))
            }
        }
    }
}