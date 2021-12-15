package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class FindNIKResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
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
        @SerializedName("alamat")
        val alamat: String,
        @SerializedName("birth_date")
        val birthDate: String,
        @SerializedName("birth_place")
        val birthPlace: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("jk")
        val jk: Any,
        @SerializedName("name")
        val name: String,
        @SerializedName("nik")
        val nik: String,
        @SerializedName("no_kk")
        val noKk: String,
        @SerializedName("photo_face")
        val photoFace: Any,
        @SerializedName("photo_requested")
        val photoRequested: Any,
        @SerializedName("photo_stored")
        val photoStored: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_detail")
        val userDetail: Any,
        @SerializedName("user_id")
        val userId: Any,
        @SerializedName("verified_at")
        val verifiedAt: Any
    )
}