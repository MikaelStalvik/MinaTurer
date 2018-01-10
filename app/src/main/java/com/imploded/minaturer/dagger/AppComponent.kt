package com.imploded.minaturer.dagger

import com.imploded.minaturer.fragments.DeparturesFragment
import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, WebServiceModule::class, SettingsModule::class, LandingViewModule::class, FindStopsViewModule::class, DeparturesViewModule::class])
interface AppComponent {
    //fun landingViewModel(): LandingViewModelInterface
    //fun settings(): SettingsInterface
    fun inject(target: LandingPageFragment)
    fun inject(target: FindStopFragment)
    fun inject(target: DeparturesFragment)
    //fun settings(): SettingsInterface
    //fun inject(target: MainActivity)
    //fun inject(target: Main2Activity)
    //fun getViewModel1(): ViewModel1

}