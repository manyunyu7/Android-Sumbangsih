package com.feylabs.sumbangsih.presentation.pin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.LoginWithNumberRes
import com.feylabs.sumbangsih.data.source.remote.response.RegisterRes
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(
    private val authApiClient: AuthApiClient
) : ViewModel() {


    private var _regNumberLiveData: MutableLiveData<ManyunyuRes<RegisterRes?>> =
        MutableLiveData()
    val regNumberLiveData get() = _regNumberLiveData


    private var _loginNumberLiveData: MutableLiveData<ManyunyuRes<LoginWithNumberRes?>> =
        MutableLiveData()
    val loginNumberLiveData get() = _loginNumberLiveData

    fun fireLogin() {
        _loginNumberLiveData.value = ManyunyuRes.Default()
    }

    fun loginNumber(number: String, password: String) {
        viewModelScope.launch {
            _loginNumberLiveData.value = ManyunyuRes.Loading()
            try {
                val req = authApiClient.loginWithNumber(
                    mapOf(
                        "contact" to number,
                        "password" to password
                    )
                )
                if (req.isSuccessful) {
                    _loginNumberLiveData.postValue(ManyunyuRes.Success(req.body()))
                } else {
                    _loginNumberLiveData.postValue(ManyunyuRes.Error(req.message()))
                }
            } catch (e: Exception) {
                _loginNumberLiveData.postValue(ManyunyuRes.Error(e.message.toString()))
            }
        }
    }

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
                req?.enqueue(object : Callback<RegisterRes?> {
                    override fun onResponse(
                        call: Call<RegisterRes?>,
                        response: Response<RegisterRes?>
                    ) {
                        if (response.body() != null) {
                            val res = response.body()!!
                            if (res.statusCode == 1) {
                                _regNumberLiveData.postValue(ManyunyuRes.Success(res))
                            } else {
                                _regNumberLiveData.postValue(ManyunyuRes.Error(res.message))
                            }
                        } else {
                            _regNumberLiveData.postValue(ManyunyuRes.Empty())
                        }
                    }

                    override fun onFailure(call: Call<RegisterRes?>, t: Throwable) {
                        _regNumberLiveData.postValue(ManyunyuRes.Error(t.localizedMessage))
                    }

                })

            } catch (e: Exception) {
                _regNumberLiveData.postValue(ManyunyuRes.Error(e.localizedMessage))
            }
        }
    }


}