package xyz.cintiawan.titipyuk.ui.preorder.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.Message
import xyz.cintiawan.titipyuk.model.parameter.PreOrderConfirmationParam
import xyz.cintiawan.titipyuk.model.parameter.PreOrderParam
import xyz.cintiawan.titipyuk.model.response.PreOrderDetailResponse
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toErrorMessage
import xyz.cintiawan.titipyuk.util.toMultipartBody
import xyz.cintiawan.titipyuk.util.toRequestBody

class PreOrderRepository(
        private val api: ApiServiceInterface,
        private val idler: CountingIdlingResource
) {
    val state = MutableLiveData<State>()
    val errorMessage = MutableLiveData<String>()

    fun getDetail(id: Int): Single<PreOrderDetailResponse> =
            api.getPreOrderDetail(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun post(data: PreOrderParam): Single<Message> =
            api.postPreOrder(
                    data.image1.toMultipartBody("gambar1"),
                    data.image2.toMultipartBody("gambar2"),
                    data.image3.toMultipartBody("gambar3"),
                    data.image4.toMultipartBody("gambar4"),
                    data.image5.toMultipartBody("gambar5"),
                    data.nama.toRequestBody(),
                    data.harga.toRequestBody(),
                    data.deskripsi.toRequestBody(),
                    data.berat.toRequestBody(),
                    data.satuanBerat.toRequestBody(),
                    data.kategori.toRequestBody(),
                    data.dibeliDari.toRequestBody(),
                    data.dikirimDari.toRequestBody(),
                    data.estimasiPengiriman.toRequestBody(),
                    data.expired.toRequestBody(),
                    data.varian.toRequestBody())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun postOffer(data: PreOrderConfirmationParam): Single<Message> =
            api.postPreOrderOffer(
                    data.id,
                    data.varian,
                    data.jumlah)
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