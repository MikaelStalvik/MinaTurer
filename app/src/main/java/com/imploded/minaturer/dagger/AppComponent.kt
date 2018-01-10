package com.imploded.minaturer.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, WebServiceModule::class, SettingsModule::class, LandingViewModule::class])
interface AppComponent {
    //fun inject(target: MainActivity)
    //fun inject(target: Main2Activity)
    //fun getViewModel1(): ViewModel1

}