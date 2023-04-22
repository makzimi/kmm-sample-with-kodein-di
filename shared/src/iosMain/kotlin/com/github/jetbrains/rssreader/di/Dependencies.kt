package com.github.jetbrains.rssreader.di

import com.github.jetbrains.rssreader.app.FeedStore
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

interface Dependencies {
    fun provideFeedStore(): FeedStore
}

class DependenciesImpl: Dependencies {

    private val di: DI by lazy { sharedIosDI() }

    override fun provideFeedStore(): FeedStore {
        return di.direct.instance()
    }
}