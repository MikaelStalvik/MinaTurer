package com.imploded.minaturer.dagger

import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.repository.WebServiceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WebServiceModule {
    @Provides
    @Singleton
    fun provideWebService(): WebServiceInterface = WebServiceRepository()
}