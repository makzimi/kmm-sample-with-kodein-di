package com.github.jetbrains.rssreader.di

import android.content.Context
import android.content.SharedPreferences
import com.github.jetbrains.rssreader.core.AndroidFeedParser
import com.github.jetbrains.rssreader.core.AndroidHttpClient
import com.github.jetbrains.rssreader.core.datasource.network.FeedParser
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.HttpClient
import org.kodein.di.Copy.All
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton

fun sharedAndroidDI() = DI {
    import(sharedAndroidModule())
    extend(di = sharedCommonDI(), copy = All)
}

private const val RSS_READER_PREF_KEY = "rss_reader_pref"

fun sharedAndroidModule() = DI.Module("sharedAndroidModule") {
    bind<SharedPreferences>() with factory { name: String ->
        instance<Context>().getSharedPreferences(
            name,
            Context.MODE_PRIVATE,
        )
    }
    bind<Settings>() with singleton {
        SharedPreferencesSettings(
            delegate = instance(arg = RSS_READER_PREF_KEY),
        )
    }

    bind<HttpClient>() with singleton {
        AndroidHttpClient(
            withLog = true,
        )
    }

    bind<FeedParser>() with singleton {
        AndroidFeedParser()
    }
}
