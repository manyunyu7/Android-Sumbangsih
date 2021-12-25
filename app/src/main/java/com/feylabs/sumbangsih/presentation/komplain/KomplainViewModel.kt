package com.feylabs.sumbangsih.presentation.komplain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.*
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class KomplainViewModel(private val commonApiClient: CommonApiClient) : ViewModel() {

    private var _uploadPengajuanVM = MutableLiveData<ManyunyuRes<String>>(ManyunyuRes.Default())
    val uploadPengajuanVm get() = _uploadPengajuanVM as LiveData<ManyunyuRes<String>>


    fun upload(obj: Map<String, Any>) {

        _uploadPengajuanVM.postValue(ManyunyuRes.Loading())
        val req = commonApiClient.uploadKomplain(obj)

        req.enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                val body = response.body()

                val apiCode = body?.statusCode
                val message = body?.message

                if (apiCode == 1) {
                    _uploadPengajuanVM.value =
                        ManyunyuRes.Success(message.toString(), message.toString())
                } else if (apiCode == 0) {
                    _uploadPengajuanVM.value = ManyunyuRes.Error(message.toString())
                } else {
                    _uploadPengajuanVM.value = ManyunyuRes.Error(message.toString())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _uploadPengajuanVM.value = ManyunyuRes.Error(t.message.toString())
            }

        })
    }

    fun fireUploadVerifVM() {
        _uploadPengajuanVM.value = ManyunyuRes.Default()
    }

}