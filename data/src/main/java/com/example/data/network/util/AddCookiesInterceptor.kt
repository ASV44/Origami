package com.example.data.network.util

import android.app.Activity
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor(val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain!!.request().newBuilder()
        var coockie = getCookie()
        builder.addHeader("Cookie", coockie)

        return chain.proceed(builder.build())
    }

    fun getCookie(): String {
        val preferences = context.getSharedPreferences("com.koshka.origami", Activity.MODE_PRIVATE)
        return preferences.getString("Token", "")
    }
}