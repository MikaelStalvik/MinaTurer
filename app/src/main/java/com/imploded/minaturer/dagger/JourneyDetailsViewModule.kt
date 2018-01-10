package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.viewmodel.JourneyDetailViewModel
import com.imploded.minaturer.viewmodel.JourneyDetailViewModelInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class JourneyDetailsViewModule {
    @Provides
    @Singleton
    fun provideJourneyDetailsViewModel(webservice: WebServiceInterface): JourneyDetailViewModelInterface = JourneyDetailViewModel(webservice)
}