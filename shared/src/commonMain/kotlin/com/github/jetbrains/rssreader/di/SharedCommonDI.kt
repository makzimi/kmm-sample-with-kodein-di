package com.github.jetbrains.rssreader.di

import com.github.jetbrains.rssreader.app.FeedStore
import com.github.jetbrains.rssreader.core.RssReader
import com.github.jetbrains.rssreader.core.Settings
import com.github.jetbrains.rssreader.core.datasource.network.FeedLoader
import com.github.jetbrains.rssreader.core.datasource.storage.FeedStorage
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun sharedCommonDI() = DI {
    import(sharedCommonModule())
}

private const val FEED_URL = "https://blog.jetbrains.com/kotlin/feed/"

fun sharedCommonModule() = DI.Module("sharedCommonModule") {
    bind<Json>() with singleton {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    }

    bind<FeedStorage>() with singleton {
        FeedStorage(
            settings = instance(),
            json = instance(),
        )
    }

    bind<FeedLoader>() with singleton {
        FeedLoader(
            httpClient = instance(),
            parser = instance(),
        )
    }

    bind<FeedStore>() with singleton {
        FeedStore(
            rssReader = instance(),
        )
    }

    bind<Settings>() with singleton { Settings(setOf(FEED_URL)) }

    bind<RssReader>() with singleton {
        RssReader(
            feedLoader = instance(),
            feedStorage = instance(),
            settings = instance(),
        )
    }
}
