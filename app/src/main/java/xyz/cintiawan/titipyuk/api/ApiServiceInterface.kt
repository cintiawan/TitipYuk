package xyz.cintiawan.titipyuk.api

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import xyz.cintiawan.titipyuk.model.*
import xyz.cintiawan.titipyuk.model.detail.UserDetail
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.model.response.*

interface ApiServiceInterface {
    @FormUrlEncoded
    @POST("api/register")
    fun register(
            @Field("email") email: String,
            @Field("uid") uid: String?,
            @Field("name") name: String,
            @Field("telepon") telepon: String,
            @Field("password") password: String,
            @Field("password_confirmation") passwordConf: String
    ): Single<Message>
    @FormUrlEncoded
    @POST("api/login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String
    ): Single<MessageLogin>
    @POST("api/logout")
    fun logout(): Single<Message>
    @Multipart
    @POST("api/save/image")
    fun uploadImage(@Part gambar: MultipartBody.Part?): Single<Message>

    @GET("api/user")
    fun myInfo(): Single<UserDetail>
    @GET("api/my/alamat")
    fun myAlamat(): Single<AlamatItemResponse>
    @FormUrlEncoded
    @POST("api/my/alamat")
    fun simpanAlamat(
            @Field("kota_rajaongkir") kota: String,
            @Field("jalan") jalan: String,
            @Field("kode_pos") kodePos: String
    ): Single<Message>

    @GET("api/get/newPromo")
    fun getPromos(): Single<PromoItemResponse>

    @GET("api/get/preorder")
    fun getPreOrders(
            @Query("page") page: Int
    ): Single<PreOrderItemResponse>
    @GET("api/my/preorder")
    fun getUserPreOrders(
            @Query("page") page: Int
    ): Single<PreOrderItemResponse>
    @GET("api/get/preorder/{id}")
    fun getPreOrderDetail(
            @Path("id") id: Int
    ): Single<PreOrderDetailResponse>
    @Multipart
    @POST("api/post/preorder")
    fun postPreOrder(
            @Part gambar1: MultipartBody.Part?,
            @Part gambar2: MultipartBody.Part?,
            @Part gambar3: MultipartBody.Part?,
            @Part gambar4: MultipartBody.Part?,
            @Part gambar5: MultipartBody.Part?,
            @Part("nama") nama: RequestBody,
            @Part("harga") harga: RequestBody,
            @Part("deskripsi") deskripsi: RequestBody,
            @Part("berat") berat: RequestBody,
            @Part("satuan_berat") satuanBerat: RequestBody,
            @Part("kategori_id") kategori: RequestBody,
            @Part("dibeli_dari") dibeliDari: RequestBody,
            @Part("dikirim_dari") dikirimDari: RequestBody,
            @Part("estimasi_pengiriman") estimasiPengiriman: RequestBody,
            @Part("expired") expired: RequestBody,
            @Part("varian") varian: RequestBody
    ): Single<Message>
    @FormUrlEncoded
    @POST("api/post/offer/preorder")
    fun postPreOrderOffer(
            @Field("preorder_id") id: Int,
            @Field("varian_id") varianId: Int,
            @Field("jumlah") jumlah: Int
    ): Single<Message>

    @GET("api/get/requesting")
    fun getRequests(
            @Query("page") page: Int
    ): Single<RequestItemResponse>
    @GET("api/my/requesting")
    fun getUserRequests(
            @Query("page") page: Int
    ): Single<RequestItemResponse>
    @GET("api/get/requesting/{id}")
    fun getRequestDetail(
            @Path("id") id: Int
    ): Single<RequestDetailResponse>
    @Multipart
    @POST("api/post/requesting")
    fun postRequest(
            @Part gambar1: MultipartBody.Part?,
            @Part gambar2: MultipartBody.Part?,
            @Part gambar3: MultipartBody.Part?,
            @Part gambar4: MultipartBody.Part?,
            @Part gambar5: MultipartBody.Part?,
            @Part("nama") nama: RequestBody,
            @Part("harga") harga: RequestBody,
            @Part("deskripsi") deskripsi: RequestBody,
            @Part("kategori_id") kategori: RequestBody,
            @Part("jumlah") jumlah: RequestBody,
            @Part("dibeli_dari") dibeliDari: RequestBody,
            @Part("dikirim_ke") dikirimKe: RequestBody,
            @Part("expired") expired: RequestBody
    ): Single<Message>
    @FormUrlEncoded
    @POST("api/post/offer/requesting")
    fun postRequestOffer(
            @Field("requesting_id") id: Int,
            @Field("dibeli_dari") dibeliDari: Int,
            @Field("dikirim_dari") dikirimDari: String
    ): Single<Message>

    @GET("api/get/trip")
    fun getTrips(
            @Query("page") page: Int
    ): Single<TripItemResponse>
    @GET("api/my/trip")
    fun getUserTrips(
            @Query("page") page: Int
    ): Single<TripItemResponse>
    @GET("api/get/trip/{id}")
    fun getTripDetail(
            @Path("id") id: Int
    ): Single<TripDetailResponse>
    @FormUrlEncoded
    @POST("api/post/trip")
    fun postTrip(
            @Field("kota_asal") kotaAsal: Int,
            @Field("kota_tujuan") kotaTujuan: Int,
            @Field("tanggal_berangkat") tanggalBerangkat: Long,
            @Field("tanggal_kembali") tanggalKembali: Long,
            @Field("rincian") rincian: String,
            @Field("estimasi_pengiriman") estimasiPengiriman: Long,
            @Field("dikirim_dari") dikirimDari: String,
            @Field("expired") expired: Long
    ): Single<Message>
    @GET("api/get/offer/trip")
    fun getMyTripOffer(): Single<VerifikasiTripItemResponse>
    @FormUrlEncoded
    @POST("api/post/offer/trip")
    fun postTripOffer(
            @Field("trip_id") id: Int,
            @Field("jumlah") jumlah: Int,
            @Field("dikirim_ke") dikirimKe: Int,
            @Field("nama") nama: String,
            @Field("harga") harga: Double,
            @Field("deskripsi") deskripsi: String,
            @Field("kategori_id") kategori: Int
    ): Single<Message>
    @FormUrlEncoded
    @POST("api/respond/offer/trip")
    fun respondTripOffer(
            @Field("do_trip_id") id: Int,
            @Field("respond") respond: Int
    ): Single<Message>

    @GET("api/get/negara")
    fun getNegaras(): Single<NegaraItemResponse>
    @GET("api/get/kota")
    fun getCities(): Single<CityItemResponse>
    @GET("api/get/kota/rajaongkir")
    fun getCitiesOngkir(): Single<List<CityOngkirItem>>
    @GET("api/get/search/kota/{name}")
    fun getCitiesByName(
            @Path("name") name: String
    ): Single<CityItemResponse>

    @GET("api/get/kategori")
    fun getKategoris(): Single<KategoriItemResponse>

    @GET("api/get/review/{user_id}")
    fun getReviews(
            @Path("user_id") user_id: Int,
            @Query("page") page: Int
    ): Single<ReviewItemResponse>
    @GET("api/my/review")
    fun getUserReviews(
            @Query("page") page: Int
    ): Single<ReviewItemResponse>

}