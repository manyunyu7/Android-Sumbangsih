package com.feylabs.sumbangsih.data.source.remote

/*
This class will sealed network response from API
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Default<T>() : Resource<T>(message = "")
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
