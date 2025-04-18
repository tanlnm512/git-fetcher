package io.tanlnm.gitfetcher

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import io.tanlnm.gitfetcher.core.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@KoinExperimentalAPI
class AppLauncher : Application(), KoinStartup, SingletonImageLoader.Factory {

    override fun onKoinStartup() = koinConfiguration {
        androidLogger()
        androidContext(this@AppLauncher)
        modules(listOf(networkModule))
    }

    override fun newImageLoader(context: Context) =
        ImageLoader(context)
            .newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .weakReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.02)
                    .directory(context.cacheDir.resolve("image_cache"))
                    .build()
            }
            .build()

    override fun onCreate() {
        super.onCreate()
    }
}