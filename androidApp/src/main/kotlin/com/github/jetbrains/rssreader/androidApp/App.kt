package com.github.jetbrains.rssreader.androidApp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.github.jetbrains.rssreader.androidApp.sync.RefreshWorker
import com.github.jetbrains.rssreader.androidApp.sync.WorkerFactoryWithDI
import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.create
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.BuildConfig
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

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
        import(domainModule())
    }
}

fun domainModule() = DI.Module(name = "domainModule") {
    bind<RssReader>() with singleton {
        RssReader.create(
            ctx = instance(),
            withLog = BuildConfig.DEBUG,
        )
    }

    bind<FeedStore>() with singleton {
        FeedStore(
            rssReader = instance(),
        )
    }
}
