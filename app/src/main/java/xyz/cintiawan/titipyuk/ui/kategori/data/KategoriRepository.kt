package xyz.cintiawan.titipyuk.ui.kategori.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.response.KategoriItemResponse
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toErrorMessage

class KategoriRepository(
        private val api: ApiServiceInterface,
        private val idler: CountingIdlingResource
) {
    val state = MutableLiveData<State>()
    val errorMessage = MutableLiveData<String>()

    fun getKategoris(): Single<KategoriItemResponse> =
            api.getKategoris()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    private fun onStart() {
        idler.increment()
        state.postValue(State.LOADING)
    }

    private fun onSuccess() {
        state.postValue(State.DONE)
    }

    private fun onError(error: Throwable) {
        Log.d("apalah_ini_error", error.message)
        state.postValue(State.ERROR)

        if(error is HttpException) {
            errorMessage.postValue(error.toErrorMessage())
        } else {
            errorMessage.postValue("Telah terjadi kesalahan")
        }
    }

    private fun onFinish() {
        idler.decrement()
    }

}