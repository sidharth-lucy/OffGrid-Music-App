package com.example.offgrid.DI

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModuleDI {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

    @Singleton
    @Provides
    @MainDispatcher
    fun provideMainDispatcher():CoroutineDispatcher{
        return Dispatchers.Main
    }

    @Singleton
    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher():CoroutineDispatcher{
        return Dispatchers.Default
    }

    @Singleton
    @Provides
    @IoDispatcher
    fun provideIoDispatcher():CoroutineDispatcher{
        return Dispatchers.IO
    }

}