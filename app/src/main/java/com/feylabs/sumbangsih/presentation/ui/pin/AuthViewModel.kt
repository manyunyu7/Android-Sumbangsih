package com.feylabs.sumbangsih.presentation.ui.pin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.CheckNumberRes
import com.feylabs.sumbangsih.data.source.remote.response.PokeApiRes
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(
    private val authApiClient: AuthApiClient
) : ViewModel() {


    private var _regNumberLiveData: MutableLiveData<ManyunyuRes<String?>> =
        MutableLiveData()
    val regNumberLiveData get() = _regNumberLiveData


    fun registerNumber(number: String, password: String) {
        viewModelScope.launch {
            _regNumberLiveData.value = ManyunyuRes.Loading()
            try {
                val req = authApiClient.registerNumber(
                    mapOf(
                        "user_contact" to number,
                        "user_password" to password
                    )
                )
                req?.enqueue(object : Callback<JsonObject?> {
                    override fun onResponse(
                        call: Call<JsonObject?>,
                        response: Response<JsonObject?>
                    ) {
                        if (response.body() != null) {
                            val res = response.body()!!
                            val message = res.get("message").asString
                            val statusCode = res.get("status_code").asInt
                            if (statusCode == 1) {
                                _regNumberLiveData.postValue(ManyunyuRes.Success(message))
                            } else {
                                _regNumberLiveData.postValue(ManyunyuRes.Error(message))
                            }
                        } else {
                            _regNumberLiveData.postValue(ManyunyuRes.Empty())
                        }
                    }

                    override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                        _regNumberLiveData.postValue(ManyunyuRes.Empty())
                    }

                })

            } catch (e: Exception) {
                _regNumberLiveData.postValue(ManyunyuRes.Error(e.localizedMessage))
            }
        }
    }


}