package com.indivar.domain.di

import android.util.Log
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.indivar.common.adapters.LocalDateTimeAdapter
import com.indivar.common.adapters.ZonedDateTimeAdapter
import com.indivar.common.api.NetworkApi
import com.indivar.domain.repo.Repository
import com.indivar.domain.repo.RepositoryImpl
import com.indivar.domain.usecases.DetailedServerError
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitHandler {

    @Provides
    @Singleton
    fun createNetworkFlipperPlugin(): NetworkFlipperPlugin {
        return NetworkFlipperPlugin()
    }

    @Provides
    @Singleton
    fun createNetworkFlipperInterceptor(networkFlipperPlugin: NetworkFlipperPlugin): Interceptor {
        return FlipperOkhttpInterceptor(networkFlipperPlugin)
    }

    @Provides
    fun createRepository(repository: RepositoryImpl): Repository {
        return repository
    }

    @Provides
    fun createApiEndpoint(retrofit: Retrofit): NetworkApi {
        return retrofit.create(NetworkApi::class.java)
    }

    @Provides
    fun createCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun createRetrofit(
        networkFlipperInterceptor: Interceptor,
    ): Retrofit {
        val okhttpClientBuilder = OkHttpClient().newBuilder()
        okhttpClientBuilder.networkInterceptors().apply {
            add(Interceptor {

                val requestBuilder = it.request().newBuilder()

                requestBuilder.addHeader(
                    "X-RapidAPI-Key",
                    "da97bb56c8mshe96fe71c7cbe265p19f1fbjsn93fb34f2408f"
                )
                requestBuilder.addHeader("X-RapidAPI-Host", "cricket-live-data.p.rapidapi.com")
                it.proceed(requestBuilder.build())


            })
        }
        okhttpClientBuilder.addNetworkInterceptor(networkFlipperInterceptor)

        val moshi = Moshi.Builder()
            .add(ZonedDateTime::class.java, ZonedDateTimeAdapter())
            .add(LocalDateTime::class.java, LocalDateTimeAdapter())
            .addLast(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(SERVER_URL)
            .client(okhttpClientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val SERVER_URL = "https://cricket-live-data.p.rapidapi.com"

}