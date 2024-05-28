package com.example.offgrid.chillMusic.DI

import com.example.offgrid.DI.AppModuleDI
import com.example.offgrid.chillMusic.view.utils.MusicNavigationUtility
import com.example.offgrid.chillMusic.view.utils.MusicUtilityFunc
import com.example.offgrid.chillMusic.view.utils.impl.MusicNavigationUtilityImpl
import com.example.offgrid.chillMusic.view.utils.impl.MusicUtilityFuncImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MyMusicEntryPoint {

    @Provides
    @Singleton
     fun provideMyMusicNavigationUtilty(): MusicNavigationUtility {
        return MusicNavigationUtilityImpl()
    }

    @Singleton
    @Provides
    fun provideMusicUtilityFunc(): MusicUtilityFunc{
        return MusicUtilityFuncImpl()
    }

}