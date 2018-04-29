package com.example.data.network.util

import com.example.data.portability.Supplier
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor(val supplier: Supplier<String>?): Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain!!.request().newBuilder()
        var coockie = supplier?.supply()
        if(coockie == null) coockie = ""
        builder.addHeader("Cookie", coockie)

        return chain.proceed(builder.build())
    }
}