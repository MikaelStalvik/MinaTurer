package com.imploded.minaturer.application

import android.app.Application
import com.imploded.minaturer.dagger.AppComponent
import com.imploded.minaturer.dagger.AppModule
import com.imploded.minaturer.dagger.DaggerAppComponent
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.FilteredDepartures
import com.imploded.minaturer.repository.SettingsRepository

class MinaTurerApp : Application() {

    companion object {
        lateinit var prefs: SettingsInterface
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        prefs = SettingsRepository(applicationContext)

        FilteredDepartures.loadData(prefs)

        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: MinaTurerApp): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}