package com.example.data.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.data.network.api.exception.ApiErrorException
import com.example.data.network.api.exception.NetworkConnectionException
import com.example.data.network.models.ApiError
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.HttpException
import java.io.IOError
import java.io.IOException

class RequestExecutor(private val connectivityManager: ConnectivityManager) {

    private val TAG: String? = RequestExecutor::class.simpleName

    private val gson = GsonBuilder().create()

    fun <T> execute(request: Observable<T>): Observable<T> {
        if (!isNetworkAvailable()) {
            return Observable.error(NetworkConnectionException(IOError(IOException())))
        }

        return request.onErrorResumeNext { error: Throwable ->
            Log.e("Error", "at sending data")
            when (error) {
                is HttpException -> {
                    val apiError = getApiErrorFromHttpException(error)
                    when {
                        apiError != null -> Observable.error(ApiErrorException(apiError.code, apiError.message))
                        else -> Observable.error(error)
                    }
                }
                else -> {
                    Observable.error(error)
                }
            }
        }
    }

    private fun getApiErrorFromHttpException(httpException: HttpException): ApiError? {
        val responseBody = httpException.response().errorBody()?.string()
        return when {
            responseBody?.isNotEmpty()!! -> gson.fromJson<ApiError>(responseBody, ApiError::class.java)
            else -> null
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }
}