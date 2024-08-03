package com.ichsanalfian.elog_pdam.api

import com.google.gson.JsonObject
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.GetBarangResponse
import com.ichsanalfian.elog_pdam.model.LoginResponse
import com.ichsanalfian.elog_pdam.model.MessageResponse
import com.ichsanalfian.elog_pdam.model.VerifApprovedResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("get_barang_auth.php") //TODO Diganti, dipakai di buyer view
    fun getBarangAuth(
        @Query("id") id : String? = UserPreferences.user.id,
    ): Call<GetBarangResponse>

    @GET("get_barang_by_id.php") //TODO Tambahan, dipakai di seller view
    fun getBarangbySellerID(
        @Query("id_seller") id : String? = UserPreferences.user.id,
    ): Call<GetBarangResponse>

//    @GET("get_barang.php") //TODO Diganti, dipakai di buyer view
//    fun getBarang() : Call<JsonObject>

    @Multipart
    @POST("post_barang_auth.php")
    fun postBarang(
        @Part("data") data: RequestBody, // Data barang dalam bentuk RequestBody
        @Part image: MultipartBody.Part? // Gambar yang diunggah
    ): Call<MessageResponse>

    //TODO Tambahan
    @POST("register.php")
    fun register(
        @Body data: JsonObject
    ): Call<MessageResponse>

    //TODO Tambahan
    @POST("login.php")
    fun login(
        @Body data: JsonObject
    ): Call<LoginResponse>

    @GET("delete.php")
    fun deleteBarang(
        @Query("id") id: Int
    ): Call<MessageResponse>

    @Multipart
    @POST("update_barang.php")
    fun updateBarang(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<MessageResponse>

    @GET("get_verif_list.php") //TODO Tambahan, dipakai di seller view
    fun getVerifList(
        @Query("id") id : String? = UserPreferences.user.id,
    ): Call<VerifApprovedResponse>

    @GET("get_approved_list.php") //TODO Tambahan, dipakai di seller view
    fun getApprovedList(
        @Query("id") id : String? = UserPreferences.user.id,
    ): Call<VerifApprovedResponse>

    @GET("approving.php")
    fun approving(
        @Query("id") id : String?
    ): Call<MessageResponse>

    @GET("rejecting.php")
    fun rejecting(
        @Query("id") id : String?,
        @Query("alasan") alasan: String?
    ): Call<MessageResponse>

    @GET("sending_barang.php")
    fun sending(
        @Query("idSeller") idSeller : String?,
        @Query("id") id : String?
    ): Call<MessageResponse>

    @GET("diterima.php")
    fun diterima(
        @Query("idSeller") idSeller : String?,
        @Query("id") id : String?
    ): Call<MessageResponse>

    @GET("reason_reject.php")
    fun getReasonReject(
        @Query("id") id : String? = UserPreferences.user.id
    ): Call<MessageResponse>

    @Multipart
    @POST("post_verif.php")
    fun postVerif(
        @Part("data") data: RequestBody, // Data barang dalam bentuk RequestBody
        @Part logo: MultipartBody.Part?, // Gambar yang diunggah
        @Part ktp: MultipartBody.Part?, // Gambar yang diunggah
    ): Call<MessageResponse>

    @FormUrlEncoded
    @POST("add_cart.php")
    fun addToCart(
        @Field("id_barang") idBarang: Int,
        @Field("jumlah") jumlah: Int,
        @Field("id_user") id_user: String,
    ): Call<MessageResponse>

    @FormUrlEncoded
    @POST("update_jumlah_keranjang.php")
    fun update_jumlah_keranjang(
        @Field("id_barang") idBarang: Int,
        @Field("jumlah") jumlah: Int,
        @Field("id_user") id_user: String,
    ): Call<MessageResponse>

    @GET("get_keranjang.php")
    fun getKeranjang(
        @Query("id") id: String? = UserPreferences.user.id,
    ): Call<GetBarangResponse>

    @FormUrlEncoded
    @POST("riwayat.php")
    fun riwayat(
        @Field("id_user") id_user: String
    ): Call<MessageResponse>

    @GET("get_riwayat.php")
    fun getRiwayat(
        @Query("id") id: String? = UserPreferences.user.id,
    ): Call<GetBarangResponse>

    @GET("seller_request.php")
    fun getSellerRequestByIdSeller(
        @Query("id_seller") id_seller: String? = UserPreferences.user.id, // Menggunakan "id_seller" sebagai parameter
    ): Call<GetBarangResponse>

}