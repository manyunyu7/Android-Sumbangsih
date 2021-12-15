package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class ProfileMainReq(
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
        @SerializedName("ktp")
        val ktp: Ktp?,
        @SerializedName("user")
        val user: User?
    ) {
        data class Ktp(
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
            val jk: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("nik")
            val nik: String,
            @SerializedName("no_kk")
            val noKk: String,
            @SerializedName("photo_face")
            val photoFace: String,
            @SerializedName("photo_requested")
            val photoRequested: String,
            @SerializedName("photo_stored")
            val photoStored: Any,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("user_detail")
            val userDetail: String,
            @SerializedName("user_id")
            val userId: Int,
            @SerializedName("verified_at")
            val verifiedAt: Any
        )

        data class User(
            @SerializedName("contact")
            val contact: String,
            @SerializedName("contact_verified_at")
            val contactVerifiedAt: Any,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("email")
            val email: Any,
            @SerializedName("email_verified_at")
            val emailVerifiedAt: Any,
            @SerializedName("id")
            val id: Int,
            @SerializedName("ktp_data")
            val ktpData: String,
            @SerializedName("name")
            val name: Any,
            @SerializedName("photo")
            val photo: Any,
            @SerializedName("photo_path")
            val photoPath: String,
            @SerializedName("role")
            val role: String,
            @SerializedName("role_desc")
            val roleDesc: String,
            @SerializedName("status")
            val status: Any,
            @SerializedName("updated_at")
            val updatedAt: String
        )
    }
}