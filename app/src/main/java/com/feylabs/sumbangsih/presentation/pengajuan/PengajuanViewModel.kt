package com.feylabs.sumbangsih.presentation.pengajuan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.*
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PengajuanViewModel(private val commonApiClient: CommonApiClient) : ViewModel() {

    private var _activeEventVm =
        MutableLiveData<ManyunyuRes<CheckActiveEventRes?>>(ManyunyuRes.Default())
    val activeEventVm get() = _activeEventVm as MutableLiveData<ManyunyuRes<CheckActiveEventRes?>>

    private var _uploadPengajuanVM = MutableLiveData<ManyunyuRes<String>>(ManyunyuRes.Default())
    val uploadPengajuanVm get() = _uploadPengajuanVM as LiveData<ManyunyuRes<String>>

    private var _selfCheckVm =
        MutableLiveData<ManyunyuRes<SelfCheckPengajuanRes>>(ManyunyuRes.Default())
    val selfCheckVm get() = _selfCheckVm as LiveData<ManyunyuRes<SelfCheckPengajuanRes>>

    private var _historyVm =
        MutableLiveData<ManyunyuRes<GetHistoryResponse>>(ManyunyuRes.Default())
    val historyVm get() = _historyVm as LiveData<ManyunyuRes<GetHistoryResponse>>

    fun selfCheckEvent(userId: String) {
        _selfCheckVm.postValue(ManyunyuRes.Loading())
        val req = commonApiClient.selfCheckPengajuan(userId)

        req.enqueue(object : Callback<SelfCheckPengajuanRes> {
            override fun onResponse(
                call: Call<SelfCheckPengajuanRes>,
                response: Response<SelfCheckPengajuanRes>
            ) {
                val body = response.body()
                if (body != null) {
                    _selfCheckVm.value = ManyunyuRes.Success(body)
                } else {
                    _selfCheckVm.value = ManyunyuRes.Error("Request Kosong")
                }
            }

            override fun onFailure(call: Call<SelfCheckPengajuanRes>, t: Throwable) {
                _selfCheckVm.value = ManyunyuRes.Error(t.localizedMessage.toString())
            }

        })
    }

    fun fetchHistory(pengajuanId: String) {
        _historyVm.postValue(ManyunyuRes.Loading())
        val req = commonApiClient.getHistoryResponse(pengajuanId)

        req.enqueue(object : Callback<GetHistoryResponse> {
            override fun onResponse(
                call: Call<GetHistoryResponse>,
                response: Response<GetHistoryResponse>
            ) {
                val body = response.body()
                if (body != null) {
                    _historyVm.value = ManyunyuRes.Success(body)
                } else {
                    _historyVm.value = ManyunyuRes.Error("Request Kosong")
                }
            }

            override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {
                _historyVm.value = ManyunyuRes.Error(t.localizedMessage.toString())
            }

        })
    }

    fun activeEvent() {
        _activeEventVm.postValue(ManyunyuRes.Loading())
        val req = commonApiClient.getActiveEvent()

        req.enqueue(object : Callback<CheckActiveEventRes> {
            override fun onResponse(
                call: Call<CheckActiveEventRes>,
                response: Response<CheckActiveEventRes>
            ) {
                val apiCode = response.body()?.apiCode ?: 0
                val message = response.body()?.messageId ?: ""


                when (apiCode) {
                    1 -> {
                        _activeEventVm.value = ManyunyuRes.Success(response.body(), message)
                    }
                    0 -> {
                        _activeEventVm.value = ManyunyuRes.Error(message)
                    }
                    else -> {
                        _activeEventVm.value = ManyunyuRes.Error(message)
                    }
                }
            }

            override fun onFailure(call: Call<CheckActiveEventRes>, t: Throwable) {
                _uploadPengajuanVM.value = ManyunyuRes.Error(t.message.toString())
            }
        })
    }

    fun upload(obj: Map<String, Any>) {

        _uploadPengajuanVM.postValue(ManyunyuRes.Loading())
        val req = commonApiClient.uploadPengajuan(obj)

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
                Timber.d("NRYX $message")

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

    fun fireCheckBLT() {
        _activeEventVm.postValue(ManyunyuRes.Default())
    }

    fun fireHistoryVm() {
        _historyVm.value = ManyunyuRes.Default()
    }

}