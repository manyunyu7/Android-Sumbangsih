package com.feylabs.sumbangsih.data.source.remote

/*
This class will sealed network response from API
 */
sealed class ManyunyuRes<T>(val data: T? = null, val message: String? = null) {
    class Empty<T>(message: String = "", data: T? = null) : ManyunyuRes<T>(data, message)
    class Default<T>(data: T?) : ManyunyuRes<T>(data)
    class Success<T>(data: T) : ManyunyuRes<T>(data)
    class Loading<T>(message: String = "", data: T? = null) : ManyunyuRes<T>(data, message)
    class Error<T>(message: String, data: T? = null) : ManyunyuRes<T>(data, message)
}
