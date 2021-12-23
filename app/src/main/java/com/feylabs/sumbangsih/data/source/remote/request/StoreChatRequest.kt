package com.feylabs.sumbangsih.data.source.remote.request


import com.google.gson.annotations.SerializedName

data class StoreChatRequest(
    @SerializedName("message")
    val message: String,
    @SerializedName("photo")
    val photo: String?,
    @SerializedName("recepient_id")
    val recepientId: String,
    @SerializedName("user_id")
    val userId: String
) {

}