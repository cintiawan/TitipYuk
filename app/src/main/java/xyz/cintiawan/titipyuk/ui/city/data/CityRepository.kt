package xyz.cintiawan.titipyuk.ui.city.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.model.response.CityItemResponse
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toErrorMessage

class CityRepository(
        private val api: ApiServiceInterface,
        private val idler: CountingIdlingResource
) {
    val state = MutableLiveData<State>()
    val stateOngkir = MutableLiveData<State>()
    val errorMessage = MutableLiveData<String>()

    fun getCities(): Single<CityItemResponse> =
            api.getCities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun getCitiesOngkir(): Single<List<CityOngkirItem>> =
            api.getCitiesOngkir()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStartOngkir() }
                    .doOnSuccess { onSuccessOngkir() }
                    .doOnError { onErrorOngkir(it) }
                    .doFinally { onFinish() }

    private fun onStart() {
        idler.increment()
        state.postValue(State.LOADING)
    }

    private fun onStartOngkir() {
        idler.increment()
        stateOngkir.postValue(State.LOADING)
    }

    private fun onSuccess() {
        state.postValue(State.DONE)
    }

    private fun onSuccessOngkir() {
        stateOngkir.postValue(State.DONE)
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

    private fun onErrorOngkir(error: Throwable) {
        Log.d("apalah_ini_error", error.message)
        stateOngkir.postValue(State.ERROR)

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