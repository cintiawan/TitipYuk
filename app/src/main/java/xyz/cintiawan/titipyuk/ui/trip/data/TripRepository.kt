package xyz.cintiawan.titipyuk.ui.trip.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.Message
import xyz.cintiawan.titipyuk.model.parameter.TripConfirmationParam
import xyz.cintiawan.titipyuk.model.parameter.TripParam
import xyz.cintiawan.titipyuk.model.parameter.VerifikasiTripParam
import xyz.cintiawan.titipyuk.model.response.TripDetailResponse
import xyz.cintiawan.titipyuk.model.response.VerifikasiTripItemResponse
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toErrorMessage

class TripRepository(
        private val api: ApiServiceInterface,
        private val idler: CountingIdlingResource
) {
    val state = MutableLiveData<State>()
    val errorMessage = MutableLiveData<String>()

    fun getDetail(id: Int): Single<TripDetailResponse> =
            api.getTripDetail(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun post(data: TripParam): Single<Message> =
            api.postTrip(
                    data.kotaAsal,
                    data.kotaTujuan,
                    data.tanggalBerangkat,
                    data.tanggalKembali,
                    data.rincian,
                    data.estimasiPengiriman,
                    data.dikirimDari,
                    data.expired)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun getMyOffer(): Single<VerifikasiTripItemResponse> =
            api.getMyTripOffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun postOffer(data: TripConfirmationParam): Single<Message> =
            api.postTripOffer(
                    data.id,
                    data.jumlah,
                    data.dikirimKe,
                    data.nama,
                    data.harga,
                    data.deskripsi,
                    data.kategori)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun respondOffer(data: VerifikasiTripParam): Single<Message> =
            api.respondTripOffer(data.id, data.respond)
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