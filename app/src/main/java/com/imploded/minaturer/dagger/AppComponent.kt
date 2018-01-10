package com.imploded.minaturer.dagger

import com.imploded.minaturer.fragments.DeparturesFragment
import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.JourneyDetailsFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, WebServiceModule::class, SettingsModule::class, LandingViewModule::class, FindStopsViewModule::class, DeparturesViewModule::class, JourneyDetailsViewModule::class])
interface AppComponent {
    fun inject(target: LandingPageFragment)
    fun inject(target: FindStopFragment)
    fun inject(target: DeparturesFragment)
    fun inject(target: JourneyDetailsFragment)
}