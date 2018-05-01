package com.example.data.network.util

import okhttp3.Interceptor
import okhttp3.Response
import android.R.id.edit
import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.data.portability.Consumer


class ReceivedCookiesInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val originalResponse = chain!!.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = mutableListOf<String>()

            for (header in originalResponse.headers("Set-Cookie")) {
                Log.e("Cookie", header)
                saveCookie(header)
            }
        }

        return originalResponse
    }

    fun saveCookie(cookie: String) {
        val preferences = context.getSharedPreferences("com.koshka.origami", Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("Token", cookie)
        editor.apply()
    }
}