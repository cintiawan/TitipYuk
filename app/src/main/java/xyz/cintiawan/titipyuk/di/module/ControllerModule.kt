package xyz.cintiawan.titipyuk.di.module

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.di.scope.ControllerScope
import xyz.cintiawan.titipyuk.util.Constants
import javax.inject.Named

@Module
class ControllerModule(private val activity: Activity) {
    @ControllerScope
    @Provides
    fun activity(): Activity {
        return activity
    }

    @ControllerScope
    @Provides
    @Named(Constants.DI_ACTIVITY_CONTEXT)
    fun context(): Context {
        return activity
    }

    // VIEWMODELS
    @ControllerScope
    @Provides
    fun viewModelFactory(): ViewModelFactory {
        return ViewModelFactory(activity as AppCompatActivity)
    }

}