package xyz.cintiawan.titipyuk.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.room.Room
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import xyz.cintiawan.titipyuk.base.BaseApp
import xyz.cintiawan.titipyuk.db.TitipYukDatabase
import xyz.cintiawan.titipyuk.di.scope.ApplicationScope
import xyz.cintiawan.titipyuk.di.scope.ControllerScope
import xyz.cintiawan.titipyuk.util.Constants
import javax.inject.Named

@Module
class ApplicationModule(private val baseApp: BaseApp) {
    @ApplicationScope
    @Provides
    fun application(): Application {
        return baseApp
    }

    @ApplicationScope
    @Provides
    @Named(Constants.DI_APPLICATION_CONTEXT)
    fun applicationContext(): Context {
        return baseApp.applicationContext
    }

    @ApplicationScope
    @Provides
    fun sharedPreferences(): SharedPreferences {
        return baseApp.getSharedPreferences(Constants.SP_KEY, Context.MODE_PRIVATE)
    }

    @ApplicationScope
    @Provides
    fun toast(@Named(Constants.DI_APPLICATION_CONTEXT) context: Context): Toast {
        return Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    @ApplicationScope
    @Provides
    fun idler(): CountingIdlingResource {
        return CountingIdlingResource("NETWORK_CALLS")
    }

    @ApplicationScope
    @Provides
    fun db(@Named(Constants.DI_APPLICATION_CONTEXT) context: Context): TitipYukDatabase {
        return Room.databaseBuilder(context, TitipYukDatabase::class.java, "titipyukdb").build()
    }

    @ApplicationScope
    @Provides
    fun googleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
    }

    @ApplicationScope
    @Provides
    fun googleSignInClient(@Named(Constants.DI_APPLICATION_CONTEXT) context: Context, gso: GoogleSignInOptions): GoogleSignInClient {
        return GoogleSignIn.getClient(context, gso)
    }

}