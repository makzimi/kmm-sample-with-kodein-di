package com.github.jetbrains.rssreader.androidApp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.github.jetbrains.rssreader.androidApp.sync.RefreshWorker
import com.github.jetbrains.rssreader.androidApp.sync.WorkerFactoryWithDI
import com.github.jetbrains.rssreader.di.sharedAndroidDI
import org.kodein.di.Copy
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class App : Application(), Configuration.Provider, DIAware {

    override val di: DI = AppDI(app = this)

    override fun getWorkManagerConfiguration(): Configuration {
        val delegatingWorkerFactory = DelegatingWorkerFactory()
        delegatingWorkerFactory.addFactory(WorkerFactoryWithDI(di))
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(delegatingWorkerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        launchBackgroundSync()
    }

    private fun launchBackgroundSync() {
        RefreshWorker.enqueue(this)
    }
}

object AppDI {
    operator fun invoke(app: App) = DI {
        import(androidXModule(app))
        extend(di = sharedAndroidDI(), copy = Copy.All)
    }
}
