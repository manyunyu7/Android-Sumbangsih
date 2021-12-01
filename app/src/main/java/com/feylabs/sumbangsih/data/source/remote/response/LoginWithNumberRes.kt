package com.feylabs.sumbangsih.data.source.remote.response
import com.google.gson.annotations.SerializedName

data class LoginWithNumberRes(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user")
    val user: User?
) {
    data class User(
        @SerializedName("contact")
        val contact: String,
        @SerializedName("contact_verified_at")
        val contactVerifiedAt: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified_at")
        val emailVerifiedAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("photo")
        val photo: String,
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