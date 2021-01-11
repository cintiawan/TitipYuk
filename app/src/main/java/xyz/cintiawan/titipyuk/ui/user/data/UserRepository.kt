package xyz.cintiawan.titipyuk.ui.user.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.firebase.FirestoreUtil
import xyz.cintiawan.titipyuk.model.Message
import xyz.cintiawan.titipyuk.model.MessageLogin
import xyz.cintiawan.titipyuk.model.detail.UserDetail
import xyz.cintiawan.titipyuk.model.item.UserFirebaseItem
import xyz.cintiawan.titipyuk.model.item.UserItem
import xyz.cintiawan.titipyuk.model.parameter.AlamatParam
import xyz.cintiawan.titipyuk.model.parameter.AuthParam
import xyz.cintiawan.titipyuk.model.response.AlamatItemResponse
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toErrorMessage

class UserRepository(
        private val api: ApiServiceInterface,
        private val firestore: FirestoreUtil,
        private val idler: CountingIdlingResource
) {
    val state = MutableLiveData<State>()
    val errorMessage = MutableLiveData<String>()

    fun register(auth: AuthParam): Single<Message> =
            api.register(auth.email, auth.uid, auth.name, auth.telepon, auth.password, auth.passwordConf)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { onSuccess() }
                    .doOnError {
                        FirebaseAuth.getInstance().currentUser?.delete()
                        firestore.currentUserDocRef().delete()
                        onError(it)
                    }
                    .doFinally { onFinish() }

    fun login(auth: AuthParam): Single<MessageLogin> =
            api.login(auth.email, auth.password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun registerEmailFirebase(auth: AuthParam, onComplete: () -> Unit) {
        onStart()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(auth.email, auth.password)
                .addOnSuccessListener { storeFirestoreUser(UserFirebaseItem(auth.email, auth.name, ""), onComplete) }
                .addOnFailureListener {
                    onError(it.message.toString())
                    onFinish()
                }
    }

    fun loginEmailFirebase(auth: AuthParam, onComplete: () -> Unit) {
        onStart()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(auth.email, auth.password)
                .addOnSuccessListener { onComplete() }
                .addOnFailureListener {
                    onError(it.message.toString())
                    onFinish()
                }
    }

    fun loginGoogleFirebase(account: GoogleSignInAccount?, onComplete: () -> Unit) {
        onStart()
        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account?.idToken, null))
                .addOnSuccessListener { storeFirestoreUser(UserFirebaseItem(account?.email.toString(), account?.displayName.toString(), ""), onComplete) }
                .addOnFailureListener {
                    onError(it.message.toString())
                    onFinish()
                }
    }

    fun logout(): Single<Message> {
        FirebaseAuth.getInstance().signOut()

        return api.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onStart() }
                .doOnSuccess { onSuccess() }
                .doOnError { onError(it) }
                .doFinally { onFinish() }
    }

    private fun storeFirestoreUser(user: UserFirebaseItem, onComplete: () -> Unit) {
        firestore.currentUserDocRef().get()
                .addOnSuccessListener { doc ->
                    if(!doc.exists()) {
                        firestore.currentUserDocRef().set(user)
                                .addOnSuccessListener { onComplete() }
                                .addOnFailureListener {
                                    onError(it.message.toString())
                                    onSuccess()
                                }
                    } else {
                        onComplete()
                    }
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                    onFinish()
                }
    }

    fun myInfo(): Single<UserDetail> =
            api.myInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun myAlamat(): Single<AlamatItemResponse> =
            api.myAlamat()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun simpanAlamat(data: AlamatParam): Single<Message> =
            api.simpanAlamat(data.kota, data.jalan, data.kodePos)
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

    private fun onError(msg: String) {
        Log.d("apalah_ini_error", msg)
        state.postValue(State.ERROR)

        errorMessage.postValue(msg)
    }

    private fun onFinish() {
        idler.decrement()
    }

}