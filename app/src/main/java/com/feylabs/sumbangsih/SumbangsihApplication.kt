package com.feylabs.sumbangsih

import android.app.Application
import com.feylabs.sumbangsih.di.networkModule
import com.feylabs.sumbangsih.di.repositoryModule
import com.feylabs.sumbangsih.di.usecaseModule
import com.feylabs.sumbangsih.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber as Tanaman

class SumbangsihApplication : Application() {

    companion object {
        const val URL_VIDEO = "http://makan-gratis.com/web_files/doc/1638754963.mp4"
    }

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
                    networkModule,
                    repositoryModule,
                    usecaseModule,
                    viewModelModule
                )
            )
        }

    }
}