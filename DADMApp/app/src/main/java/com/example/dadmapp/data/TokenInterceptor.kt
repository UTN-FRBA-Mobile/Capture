package com.example.dadmapp.data

import okhttp3.Interceptor
import okhttp3.Response


class TokenInterceptor: Interceptor {
    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBuilder = request.newBuilder()

        if (request.header(AUTH_HEADER_NAME) == null) {
            if (token != null) {
                requestBuilder.addHeader(AUTH_HEADER_NAME, "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val AUTH_HEADER_NAME = "Authorization"
    }
}