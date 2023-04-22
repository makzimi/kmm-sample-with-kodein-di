package com.github.jetbrains.rssreader.di

object DependenciesFactory {
    fun create(): Dependencies = DependenciesImpl()
}