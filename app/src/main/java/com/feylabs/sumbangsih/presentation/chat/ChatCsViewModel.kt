package com.feylabs.sumbangsih.presentation.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.request.StoreChatRequest
import com.feylabs.sumbangsih.data.source.remote.response.ListChatResponse
import com.feylabs.sumbangsih.data.source.remote.response.StoreChatRes
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatCsViewModel(commonApiClient: CommonApiClient) : ViewModel() {

    private var _saveChatVm: MutableLiveData<ManyunyuRes<StoreChatRes>> = MutableLiveData()
    val saveChatVm get() = _saveChatVm

    private var _listChatVm: MutableLiveData<ManyunyuRes<ListChatResponse>> = MutableLiveData()
    val listChatVm get() = _listChatVm

    fun repeatFun() {
        viewModelScope.launch(Dispatchers.IO) {
            while(isActive) {
                delay(1000)
            }
        }
    }


    fun fetchChatByUserId(userId: String) {
        _listChatVm.postValue(ManyunyuRes.Loading())
        AndroidNetworking.get(BASE_URL + "chat/user/$userId/get")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val req = Gson().fromJson(response, ListChatResponse::class.java)
                    _listChatVm.postValue(ManyunyuRes.Success(req))
                }

                override fun onError(anError: ANError?) {
                    _listChatVm.postValue(ManyunyuRes.Error(anError.toString()))
                }

            })
    }

    fun saveChat(userId: String, message: String, photo: String? = null) {
        _saveChatVm.value = ManyunyuRes.Loading()
        AndroidNetworking.post(BASE_URL + "chat/store")
            .addBodyParameter("message_chat", message + ".")
            .addBodyParameter("user_id", userId)
            .addBodyParameter("photo", photo)
            .addBodyParameter("recepient_id", "1")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val req = Gson().fromJson(response, StoreChatRes::class.java)
                    _saveChatVm.value = ManyunyuRes.Success(req)
                }

                override fun onError(anError: ANError?) {
                    _saveChatVm.value = ManyunyuRes.Error(anError?.localizedMessage.toString())
                }

            })
    }

}