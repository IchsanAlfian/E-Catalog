package com.ichsanalfian.elog_pdam.api

import com.google.gson.JsonObject
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.GetBarangResponse
import com.ichsanalfian.elog_pdam.model.LoginResponse
import com.ichsanalfian.elog_pdam.model.MessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("get_barang_auth.php") //TODO Diganti, dipakai di buyer view
    fun getBarangAuth(
        @Header("Authorization") id : String? = UserPreferences.user.id,
    ): Call<JsonObject>

    @GET("get_barang_by_id.php") //TODO Tambahan, dipakai di seller view
    fun getBarangbySellerID(
        @Query("id_seller") id : String? = UserPreferences.user.id,
    ): Call<GetBarangResponse>

    @GET("get_barang.php") //TODO Diganti, dipakai di buyer view
    fun getBarang() : Call<JsonObject>

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

    @GET("delete_barang.php")
    fun deleteBarang(
        @Query("id") id: Int
    ): Call<MessageResponse>

    @Multipart
    @POST("update_barang.php")
    fun updateBarang(
        @Part("id") id: Int,
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<MessageResponse>
}