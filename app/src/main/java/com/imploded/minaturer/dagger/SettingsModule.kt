package com.imploded.minaturer.dagger

import android.content.Context
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsModule {
    @Provides
    @Singleton
    fun provideSettings(context: Context): SettingsInterface = SettingsRepository(context)
}