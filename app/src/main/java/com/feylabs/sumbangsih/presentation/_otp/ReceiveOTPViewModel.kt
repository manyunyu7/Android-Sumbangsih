package com.feylabs.sumbangsih.presentation._otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.CheckNumberRes
import com.feylabs.sumbangsih.data.source.remote.response.PokeApiRes
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ReceiveOTPViewModel(
    private val authApiClient: AuthApiClient
) : ViewModel() {

    private var _pokeLiveData: MutableLiveData<ManyunyuRes<PokeApiRes?>> = MutableLiveData()
    val pokeLiveData get() = _pokeLiveData

    private var _checkNumberLiveData: MutableLiveData<ManyunyuRes<CheckNumberRes?>> =
        MutableLiveData()
    val checkNumberLiveData get() = _checkNumberLiveData

    fun pokeApi() {
        viewModelScope.launch {
            _pokeLiveData.value = ManyunyuRes.Loading()
            try {
                val req = authApiClient.colekService()
                if (req.isSuccessful) {
                    _pokeLiveData.postValue(ManyunyuRes.Success(req.body()))
                } else {
                    _pokeLiveData.postValue(ManyunyuRes.Error(req.raw().toString()))
                }

            } catch (e: Exception) {
                _pokeLiveData.postValue(ManyunyuRes.Error(e.toString()))
            }
        }
    }

    fun checkNumber(number: String) {
        _checkNumberLiveData.postValue(ManyunyuRes.Loading())
        AndroidNetworking.get(BASE_URL+"auth/check-number?number=$number")
            .build()
            .getAsString(object :StringRequestListener{
                override fun onResponse(response: String?) {
                    val reqBody = Gson().fromJson(response,CheckNumberRes::class.java)
                    if(reqBody.apiCode==1){
                        _checkNumberLiveData.postValue(ManyunyuRes.Success(reqBody))
                    }else{
                        _checkNumberLiveData.postValue(ManyunyuRes.Empty("Nomor Tidak Ditemukan"))
                    }
                }

                override fun onError(anError: ANError?) {
                    _checkNumberLiveData.postValue(ManyunyuRes.Error(message = anError.toString()))
                }

            })
    }


}