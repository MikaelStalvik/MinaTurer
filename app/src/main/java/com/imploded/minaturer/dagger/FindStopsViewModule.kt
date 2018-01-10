package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.FindStopsViewModelInterface
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.viewmodel.FindStopsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FindStopsViewModule {
    @Provides
    @Singleton
    fun provideFindStopsViewModel(webservice: WebServiceInterface, settings: SettingsInterface): FindStopsViewModelInterface = FindStopsViewModel(webservice, settings)
}