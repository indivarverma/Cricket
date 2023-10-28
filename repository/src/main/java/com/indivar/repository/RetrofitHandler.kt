package com.indivar.repository

import android.util.Log
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.indivar.usecases.DetailedServerError
import com.indivar.usecases.Repository
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
        okhttpClientBuilder.addNetworkInterceptor(Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val errorCode = response.code
            val body = response.body
            if (body?.contentType()?.subtype?.lowercase(Locale.getDefault()) == "json") {
                var errorMessage = ""
                // Assume default OK
                try {
                    val source = body.source()
                    source.request(Long.MAX_VALUE) // Buffer the entire body.
                    val buffer = source.buffer
                    val charset = body.contentType()?.charset(Charset.forName("UTF-8"))
                        ?: Charset.forName("UTF-8")
                    // Clone the existing buffer is they can only read once so we still want to pass the original one to the chain.
                    val json: String = buffer.clone().readString(charset)
                    val obj = JsonParser.parseString(json)
                    if (obj is JsonObject && obj.has("error")) {
                        errorMessage = obj.get("error").getAsString()
                    }
                } catch (e: Exception) {
                    Log.e("filebrowser", "Error: " + e.message)
                }
                if (errorCode > 399) {
                    throw DetailedServerError(errorCode, errorMessage)
                }
            }
            response
        })

        val moshi = Moshi.Builder()
            .add(ZonedDateTime::class.java, ZonedDateTimeAdapter())
            .addLast(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(SERVER_URL)
            .client(okhttpClientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val SERVER_URL = "https://cricket-live-data.p.rapidapi.com"

}