package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class SelfCheckPengajuanRes(
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
        @SerializedName("approved_kecamatan")
        val approvedKecamatan: Int,
        @SerializedName("approved_kelurahan")
        val approvedKelurahan: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("event_data")
        val eventData: EventData,
        @SerializedName("event_id")
        val eventId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("isFinish")
        val isFinish: Any,
        @SerializedName("ktp_data")
        val ktpData: KtpData,
        @SerializedName("lat_selfie")
        val latSelfie: Any,
        @SerializedName("lat_usaha")
        val latUsaha: Any,
        @SerializedName("long_selfie")
        val longSelfie: Any,
        @SerializedName("long_usaha")
        val longUsaha: Any,
        @SerializedName("nama_usaha")
        val namaUsaha: String,
        @SerializedName("nib")
        val nib: Any,
        @SerializedName("photo_ktp")
        val photoKtp: Any,
        @SerializedName("photo_selfie")
        val photoSelfie: String,
        @SerializedName("photo_usaha")
        val photoUsaha: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    ) {
        data class EventData(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("kuotas")
            val kuotas: Any,
            @SerializedName("name")
            val name: String,
            @SerializedName("show_announcement")
            val showAnnouncement: Any,
            @SerializedName("status")
            val status: String,
            @SerializedName("time_end")
            val timeEnd: String,
            @SerializedName("time_start")
            val timeStart: String,
            @SerializedName("updated_at")
            val updatedAt: String
        )

        data class KtpData(
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
            @SerializedName("lat")
            val lat: Any,
            @SerializedName("long")
            val long: Any,
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
            @SerializedName("verification_notes")
            val verificationNotes: Any,
            @SerializedName("verification_status")
            val verificationStatus: String,
            @SerializedName("verified_at")
            val verifiedAt: Any
        )
    }
}