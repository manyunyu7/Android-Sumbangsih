package com.feylabs.sumbangsih

import android.app.Application
import com.feylabs.sumbangsih.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber as Tanaman

class SumbangsihApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Plant Timber for logging
        // imported as POHON 😂
        Tanaman.plant(Tanaman.DebugTree())

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@SumbangsihApplication)
            modules(
                listOf(
                    networkModule
                )
            )
        }

    }
}