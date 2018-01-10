package com.imploded.minaturer.dagger

import android.app.Application
import android.content.Context
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app
}