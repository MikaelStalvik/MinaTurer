package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.repository.WebServiceRepository
import com.imploded.minaturer.viewmodel.FindStopsViewModel
import com.imploded.minaturer.viewmodel.FindStopsViewModelInterface
import com.imploded.minaturer.viewmodel.LandingViewModel
import com.imploded.minaturer.viewmodel.LandingViewModelInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FindStopsViewModule {
    @Provides
    @Singleton
    fun provideFindStopsViewModel(webservice: WebServiceInterface, settings: SettingsInterface): FindStopsViewModelInterface = FindStopsViewModel(webservice, settings)
}