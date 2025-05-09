package com.example.newsapi.utils

import okhttp3.Interceptor
import okhttp3.Response

internal class ApiKeyInterceptor(private val apikey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
        chain.request().newBuilder()
            .addHeader("X-Api-Key", apikey)
            .build()
        )
    }
}