package xyz.cintiawan.titipyuk.base

import androidx.multidex.MultiDexApplication
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.di.component.ApplicationComponent
import xyz.cintiawan.titipyuk.di.component.DaggerApplicationComponent
import xyz.cintiawan.titipyuk.di.module.ApplicationModule
import xyz.cintiawan.titipyuk.di.module.NetworkModule

class BaseApp : MultiDexApplication() {
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Proxima_Nova-%s.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())

        component = DaggerApplicationComponent.builder()
                .networkModule(NetworkModule())
                .applicationModule(ApplicationModule(this))
                .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

}