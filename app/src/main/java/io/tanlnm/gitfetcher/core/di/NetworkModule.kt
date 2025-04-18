package io.tanlnm.gitfetcher.core.di

import android.content.Context
import com.google.gson.Gson
import io.tanlnm.gitfetcher.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Koin module responsible for providing network-related dependencies.
 *
 * This module configures and provides instances of OkHttp, Retrofit, and related components
 * necessary for making network requests.  It includes factories for:
 *
 * - `Cache`: Configures an OkHttp cache for storing responses, using the application's cache directory.
 * - `HttpLoggingInterceptor`: Interceptor for logging HTTP requests and responses for debugging purposes, enabled only in debug builds.
 * - `Interceptor`:  Adds default headers to all requests (Content-Type and Accept).
 * - `OkHttpClient`:  Builds an OkHttpClient with configured timeouts, interceptors (including logging and header interception), and a cache.
 * - `Retrofit`: Builds a Retrofit instance configured with the base URL (from BuildConfig), a Gson converter factory, and the OkHttpClient.
 *
 * The dependencies are provided as factories, ensuring a new instance is created each time they are requested.  Dependencies can be injected using Koin's dependency injection mechanism.
 */
val networkModule = module {

    factory { (context: Context) ->
        Cache(context.cacheDir, (25 * 1024 * 1024).toLong())
    }

    factory {
        HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").i(message)
        }.apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    factory {
        Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Accept", "application/json; charset=UTF-8")
            chain.proceed(newRequest.build())
        }
    }

    factory { (cache: Cache, logging: HttpLoggingInterceptor, headerInterceptor: Interceptor) ->
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            interceptors().addAll(listOf(logging, headerInterceptor))
            cache(cache)
        }.build()
    }

    factory { (okHttpClient: OkHttpClient) ->
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(Gson()))
            baseUrl(BuildConfig.API_URL)
            client(okHttpClient)
        }.build()
    }
}