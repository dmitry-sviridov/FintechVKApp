package ru.sviridov.vkclient.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.sviridov.vkclient.feature_auth.handler.TokenHolder

object QueryParameterInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val httpUrl = chain.request().url().newBuilder()
            .addQueryParameter("access_token", TokenHolder.token!!.accessToken)
            .addQueryParameter("v", NetworkConstants.API_VERSION)
            .build()

        return chain.proceed(
            chain.request().newBuilder()
                .url(httpUrl)
                .build()
        )
    }
}