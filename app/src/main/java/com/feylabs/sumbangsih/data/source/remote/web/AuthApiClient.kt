package com.feylabs.sumbangsih.data.source.remote.web

import com.feylabs.sumbangsih.data.source.remote.response.CheckNumberRes
import com.feylabs.sumbangsih.data.source.remote.response.PokeApiRes
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.FieldMap

import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.FormUrlEncoded




interface AuthApiClient {

    @GET("auth/colek")
    suspend fun colekService(
    ): Response<PokeApiRes>

    @GET("auth/check-number")
    @JvmSuppressWildcards
    suspend fun checkIfNumberRegistered(
        @QueryMap queryMap: Map<String, Any>
    ): Response<CheckNumberRes>

    @POST("login")
    @JvmSuppressWildcards
    fun login(
        @Body() queryMap: Map<String, Any>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("auth/registerNumber")
    fun registerNumber(@FieldMap params: Map<String?, String?>?): Call<JsonObject?>?


}