package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class SimpleRes(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)