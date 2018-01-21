package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.JourneyDetailViewModelInterface
import com.imploded.minaturer.viewmodel.JourneyDetailViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class JourneyDetailsViewModule {
    @Provides
    @Singleton
    fun provideJourneyDetailsViewModel(): JourneyDetailViewModelInterface = JourneyDetailViewModel()
}