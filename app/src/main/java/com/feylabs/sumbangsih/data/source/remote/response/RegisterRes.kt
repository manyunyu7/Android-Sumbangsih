package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class RegisterRes(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: ResData,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class ResData(
        @SerializedName("id")
        val id: Int,
        @SerializedName("number")
        val number: String
    )
}