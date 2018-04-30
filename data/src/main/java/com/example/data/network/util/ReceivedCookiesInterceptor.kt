package com.example.data.network.util

import okhttp3.Interceptor
import okhttp3.Response
import android.R.id.edit
import android.util.Log
import com.example.data.portability.Consumer


class ReceivedCookiesInterceptor(val consumer: Consumer<String>?): Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val originalResponse = chain!!.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = mutableListOf<String>()

            for (header in originalResponse.headers("Set-Cookie")) {
                Log.e("Cookie", header)
                consumer?.accept(header)
            }
        }

        return originalResponse
    }
}