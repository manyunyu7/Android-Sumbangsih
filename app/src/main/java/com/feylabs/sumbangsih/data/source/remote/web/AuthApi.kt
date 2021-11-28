package com.feylabs.sumbangsih.data.source.remote.web

import com.google.gson.JsonElement
import retrofit2.Response

class AuthApi(private val apiClient: AuthApiClient) : AuthApiClient {

    override fun colekService(queryMap: Map<String, Any>): Response<JsonElement> {
        return apiClient.colekService(queryMap)
    }

    override fun checkIfNumberRegistered(queryMap: Map<String, Any>): Response<JsonElement> {
        return apiClient.checkIfNumberRegistered(queryMap)
    }

    override fun login(queryMap: Map<String, Any>): Response<JsonElement> {
        return apiClient.checkIfNumberRegistered(queryMap)
    }
}