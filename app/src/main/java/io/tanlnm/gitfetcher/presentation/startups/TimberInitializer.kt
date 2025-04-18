package io.tanlnm.gitfetcher.presentation.startups

import android.content.Context
import androidx.startup.Initializer
import io.tanlnm.gitfetcher.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.tag("GitFetcher").d("Timber Initialized")
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}