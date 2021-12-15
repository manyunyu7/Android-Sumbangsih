package com.feylabs.sumbangsih.presentation.ktp_verif

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.CommonResponse
import com.feylabs.sumbangsih.data.source.remote.response.FindNIKResponse
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.google.android.gms.common.internal.service.Common
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class KTPVerifViewModel(private val commonApiClient: CommonApiClient) : ViewModel() {

    private var _findKTPVm = MutableLiveData<ManyunyuRes<FindNIKResponse>>(ManyunyuRes.Default())
    val findKTPVm get() = _findKTPVm as MutableLiveData<ManyunyuRes<FindNIKResponse>>

    private var _uploadVerifVM = MutableLiveData<ManyunyuRes<String>>(ManyunyuRes.Default())
    val uploadVerifVM get() = _uploadVerifVM as LiveData<ManyunyuRes<String>>

    fun fireUploadVerifVM() {
        _uploadVerifVM.postValue(ManyunyuRes.Default())
    }

    fun upload(obj: KTPVerifReq) {

        _uploadVerifVM.postValue(ManyunyuRes.Loading())

        val map = mutableMapOf<String, Any>()
        map.put("nik", obj.nik)
        map.put("photo_face", obj.photo_face)
        map.put("photo_requested", obj.photo_requested)
        map.put("contact", obj.contact.toString())

        val req = commonApiClient.editDynamicInfo(
            nik = obj.nik,
            map
        )

        req.enqueue(object : Callback<CommonResponse> {

            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                val body = response.body()

                Timber.d("response  $response")
                Timber.d("bodyyy  $body")
                val apiCode = body?.statusCode
                val message = body?.message
                Timber.d("verif nik message $message")

                if (apiCode == 1) {
                    _uploadVerifVM.value = ManyunyuRes.Success(message.toString())
                } else if (apiCode == 0) {
                    _uploadVerifVM.value = ManyunyuRes.Error(message.toString())
                }else{
                    _uploadVerifVM.value = ManyunyuRes.Error(message.toString())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _uploadVerifVM.value = ManyunyuRes.Error(t.message.toString())
            }

        })
    }

    fun findKTPByNIK(nik: String) {
        _findKTPVm.value = ManyunyuRes.Loading()
        val fanURL = BASE_URL + "ktp/findNikMobile/$nik"
        Timber.d("FAN VERIF NIK URL :  $fanURL")
        AndroidNetworking.get(fanURL)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    try {
                        val res = Gson().fromJson(response.toString(), FindNIKResponse::class.java)
                        Timber.d("FAN VERIF NIK $res")
                        val message = res.messageId
                        if (res.apiCode == 1) {
                            _findKTPVm.postValue(ManyunyuRes.Success(res))
                        } else {
                            _findKTPVm.postValue(ManyunyuRes.Error(message))
                        }
                    } catch (e: Exception) {
                        _findKTPVm.postValue(ManyunyuRes.Error(e.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN VERIF NIK ERROR ${anError.toString()}")
                    Timber.d("FAN VERIF NIK ERROR ${anError?.errorBody.toString()}")
                    _findKTPVm.postValue(ManyunyuRes.Error(anError?.message.toString()))
                }

            })
    }

}