package xyz.cintiawan.titipyuk.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.di.scope.ViewModelScope

@Module
class ViewModelModule {
    @ViewModelScope
    @Provides
    fun subscriptions(): CompositeDisposable {
        return CompositeDisposable()
    }
}