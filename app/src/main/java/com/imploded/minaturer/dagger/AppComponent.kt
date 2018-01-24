package com.imploded.minaturer.dagger

import com.imploded.minaturer.fragments.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    WebServiceModule::class,
    SettingsModule::class,
    LandingViewModule::class,
    FindStopsViewModule::class,
    DeparturesViewModule::class,
    JourneyDetailsViewModule::class,
    SettingsViewModule::class])

interface AppComponent {
    fun inject(target: LandingPageFragment)
    fun inject(target: FindStopFragment)
    fun inject(target: DeparturesFragment)
    fun inject(target: JourneyDetailsFragment)
    fun inject(target: SettingsPageFragment)
}