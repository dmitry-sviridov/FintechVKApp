package ru.sviridov.vkclient.network.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.sviridov.vkclient.network.NetworkConstants
import ru.sviridov.vkclient.network.QueryParameterInterceptor
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkCommonModule {

    @Provides
    @Singleton
    @Named("UnwrapRoot")
    fun provideObjectMapperWithUnwrappingRoot(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
    }

    @Provides
    @Singleton
    @Named("NotUnwrapRoot")
    fun provideObjectMapperWithOutUnwrappingRoot(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient()
            .newBuilder()
            .addInterceptor(QueryParameterInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("withUnwrap")
    fun provideRetrofitWithUnwrapper(
        okHttpClient: OkHttpClient,
        @Named("UnwrapRoot") objectMapper: ObjectMapper
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConstants.API_URL)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("withOutUnwrap")
    fun provideRetrofitWithOutUnwrapper(
        okHttpClient: OkHttpClient,
        @Named("NotUnwrapRoot") objectMapper: ObjectMapper
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConstants.API_URL)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}