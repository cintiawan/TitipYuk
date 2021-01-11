package xyz.cintiawan.titipyuk.di.component

import dagger.Component
import xyz.cintiawan.titipyuk.di.module.ApplicationModule
import xyz.cintiawan.titipyuk.di.module.ControllerModule
import xyz.cintiawan.titipyuk.di.module.NetworkModule
import xyz.cintiawan.titipyuk.di.module.ViewModelModule
import xyz.cintiawan.titipyuk.di.scope.ApplicationScope

@ApplicationScope
@Component(modules = [
    ApplicationModule::class,
    NetworkModule::class
])
interface ApplicationComponent {
    fun controllerComponent(module: ControllerModule): ControllerComponent
    fun viewModelComponent(module: ViewModelModule): ViewModelComponent
}