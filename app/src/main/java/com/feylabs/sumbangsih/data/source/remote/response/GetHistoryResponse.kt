package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(
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
    val resData: List<ResData>?,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class ResData(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("pengajuan_id")
        val pengajuanId: Int,
        @SerializedName("role")
        val role: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("updated_by")
        val updatedBy: Int,
        @SerializedName("user_id")
        val userId: Int
    )
}