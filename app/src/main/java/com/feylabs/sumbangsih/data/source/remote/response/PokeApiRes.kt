package com.feylabs.sumbangsih.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class PokeApiRes(
    @SerializedName("changeLog")
    val changeLog: List<String>,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("quotes_of_the_day")
    val quotesOfTheDay: String,
    @SerializedName("version")
    val version: String,
    @SerializedName("version_code_name")
    val versionCodeName: String
) {

//    fun toPokeApi(): PokeApi {
//        return PokeApi(
//            changeLog = changeLog,
//            httpResponse = httpResponse,
//            messageEn = messageEn,
//            messageId = messageId,
//            quotesOfTheDay = quotesOfTheDay,
//            version = version,
//        )
//    }
}