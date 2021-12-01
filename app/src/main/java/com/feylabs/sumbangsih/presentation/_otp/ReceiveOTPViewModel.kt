package com.feylabs.sumbangsih.presentation._otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.CheckNumberRes
import com.feylabs.sumbangsih.data.source.remote.response.PokeApiRes
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
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
        viewModelScope.launch {
            _checkNumberLiveData.value = ManyunyuRes.Loading()
            try {
                val req = authApiClient.checkIfNumberRegistered(mapOf("number" to number))
                if (req.isSuccessful) {
                    if (req.body()?.apiCode == 1) {
                        _checkNumberLiveData.postValue(ManyunyuRes.Success(req.body()))
                    } else {
                        _checkNumberLiveData.postValue(ManyunyuRes.Empty("Nomor Tidak Ditemukan"))
                    }
                } else {
                    _checkNumberLiveData.postValue(
                        ManyunyuRes.Error(
                            message = req.raw().toString()
                        )
                    )
                }

            } catch (e: Exception) {
                _checkNumberLiveData.postValue(ManyunyuRes.Error(message = e.toString()))
            }
        }
    }


}