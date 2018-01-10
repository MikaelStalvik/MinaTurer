package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.viewmodel.DeparturesViewModel
import com.imploded.minaturer.viewmodel.DeparturesViewModelInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeparturesViewModule {
    @Provides
    @Singleton
    fun provideDeparturesViewModel(webservice: WebServiceInterface, settings: SettingsInterface): DeparturesViewModelInterface = DeparturesViewModel(webservice, settings)
}