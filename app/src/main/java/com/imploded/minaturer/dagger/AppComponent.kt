package com.imploded.minaturer.dagger

import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.viewmodel.LandingViewModelInterface
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, WebServiceModule::class, SettingsModule::class, LandingViewModule::class, FindStopsViewModule::class])
interface AppComponent {
    //fun landingViewModel(): LandingViewModelInterface
    //fun settings(): SettingsInterface
    fun inject(target: LandingPageFragment)
    fun inject(target: FindStopFragment)
    //fun settings(): SettingsInterface
    //fun inject(target: MainActivity)
    //fun inject(target: Main2Activity)
    //fun getViewModel1(): ViewModel1

}