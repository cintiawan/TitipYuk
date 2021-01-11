package xyz.cintiawan.titipyuk.di.component

import dagger.Subcomponent
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.di.module.ViewModelModule
import xyz.cintiawan.titipyuk.di.scope.ViewModelScope

@ViewModelScope
@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(viewModelFactory: ViewModelFactory)
}