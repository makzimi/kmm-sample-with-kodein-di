package com.github.jetbrains.rssreader.di

import com.github.jetbrains.rssreader.core.IosFeedParser
import com.github.jetbrains.rssreader.core.IosHttpClient
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.kodein.di.Copy
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import platform.Foundation.NSUserDefaults

fun sharedIosDI() = DI {
    import(sharedIosModule())
    extend(di = sharedCommonDI(), copy = Copy.All)
}

fun sharedIosModule() = DI.Module("sharedIosModule") {
    bind<Settings>() with singleton {
        NSUserDefaultsSettings(
            delegate = NSUserDefaults.standardUserDefaults(),
        )
    }

    bind<HttpClient>() with singleton {
        IosHttpClient(
            withLog = true,
        )
    }

    bind<FeedParser>() with singleton {
        IosFeedParser()
    }
}
