package com.feylabs.sumbangsih.data.source.remote.web

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiClient {

    @GET("/auth/check-number")
    fun colekService(
        @Body() queryMap: Map<String, Any>
    ): Response<JsonElement>

    @GET("/auth/check-number")
    fun checkIfNumberRegistered(
        @Body() queryMap: Map<String, Any>
    ): Response<JsonElement>

    @POST("/login")
    fun login(
        @Body() queryMap: Map<String, Any>
    ): Response<JsonElement>


}