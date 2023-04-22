package com.github.jetbrains.rssreader.androidApp.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.kodein.di.DIAware

class WorkerFactoryWithDI(private val diAware: DIAware) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            RefreshWorker::class.java.name -> RefreshWorker(appContext, workerParameters, diAware)
            else -> null
        }
    }
}
