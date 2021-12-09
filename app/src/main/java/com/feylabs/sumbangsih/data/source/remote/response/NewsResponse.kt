package com.feylabs.sumbangsih.data.source.remote.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class NewsResponse : ArrayList<NewsResponse.NewsResponseItem>() {
    @Parcelize
    data class NewsResponseItem(
        @SerializedName("author")
        val author: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("created_at")
        val createdAt: String = "",
        @SerializedName("id")
        val id: Int = -99,
        @SerializedName("photo")
        val photo: String = "",
        @SerializedName("photo_path")
        val photoPath: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("date_indo")
        val dateIndo: String = "",
        @SerializedName("updated_at")
        val updatedAt: String = ""
    ) : Parcelable
}