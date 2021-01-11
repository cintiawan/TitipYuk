package xyz.cintiawan.titipyuk.di.module

import android.content.Context
import android.content.SharedPreferences
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.cintiawan.titipyuk.BuildConfig
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.di.scope.ApplicationScope
import xyz.cintiawan.titipyuk.util.Constants
import xyz.cintiawan.titipyuk.util.SPUtil
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetworkModule {
    @ApplicationScope
    @Provides
    fun api(retrofit: Retrofit): ApiServiceInterface {
        return retrofit.create(ApiServiceInterface::class.java)
    }

    @ApplicationScope
    @Provides
    fun retrofitInterface(okHttpClient: OkHttpClient): Retrofit {
        return retrofit2.Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .build()
    }

    @ApplicationScope
    @Provides
    fun picasso(okHttp3Downloader: OkHttp3Downloader, @Named(Constants.DI_APPLICATION_CONTEXT) context: Context): Picasso {
        return Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build()
    }

    @ApplicationScope
    @Provides
    fun okHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @ApplicationScope
    @Provides
    fun okHttpClient(sp: SharedPreferences): OkHttpClient {
        val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.protocols(mutableListOf(Protocol.HTTP_1_1))
        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor {
            val request= it.request().newBuilder()
                    .addHeader("Accept", "application/json")
            if(SPUtil.hasAccessToken(sp)) request.addHeader("Authorization", "Bearer " + SPUtil.getAccessToken(sp))

            it.proceed(request.build())
        }

        return httpClient.build()
    }

    companion object {
        const val REQUEST_TIMEOUT = 60L
    }

}