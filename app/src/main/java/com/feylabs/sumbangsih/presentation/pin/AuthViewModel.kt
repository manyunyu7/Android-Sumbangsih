package com.feylabs.sumbangsih.presentation.pin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.LoginWithNumberRes
import com.feylabs.sumbangsih.data.source.remote.response.RegisterRes
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

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
        Timber.d("OASA login, $number")
        Timber.d("OASA login, $password")
        AndroidNetworking.post(BASE_URL + "auth/login")
            .addBodyParameter("contact", number)
            .addBodyParameter("password", password)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val req = Gson().fromJson(response, LoginWithNumberRes::class.java)
                    if (req.statusCode == 1) {
                        _loginNumberLiveData.postValue(ManyunyuRes.Success(req))
                    } else {
                        _loginNumberLiveData.postValue(ManyunyuRes.Error("Login Gagal"))
                    }
                }

                override fun onError(anError: ANError?) {
                    _loginNumberLiveData.postValue(ManyunyuRes.Error("Terjadi Kesalahan ${anError?.localizedMessage.toString()}"))
                }

            })
    }

    fun registerNumber(number: String, password: String) {

        val req = authApiClient.registerNumber(
            mapOf(
                "user_contact" to number,
                "user_password" to password
            )
        )

        _regNumberLiveData.value = ManyunyuRes.Loading()
        AndroidNetworking.post(BASE_URL + "auth/registerNumber")
            .addBodyParameter("user_contact", number)
            .addBodyParameter("user_password", password)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val res = Gson().fromJson(response, RegisterRes::class.java)
                    if (res.statusCode == 1) {
                        _regNumberLiveData.postValue(ManyunyuRes.Success(res))
                    } else {
                        _regNumberLiveData.postValue(ManyunyuRes.Error(res.message))
                    }
                }

                override fun onError(anError: ANError?) {
                    _regNumberLiveData.postValue(ManyunyuRes.Error(anError?.localizedMessage.toString()))
                }

            })


    }

}