package com.ichsanalfian.elog_pdam.api

import com.google.gson.JsonObject
import com.ichsanalfian.elog_pdam.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("get_barang.php")
    fun getBarang(): Call<JsonObject>

    //TODO Diganti
    @Multipart
    @POST("post_barang.php")
    fun postBarang(
        @Part("data") data: RequestBody, // Data barang dalam bentuk RequestBody
        @Part image: MultipartBody.Part? // Gambar yang diunggah
    ): Call<UploadResponse>

    //TODO ABAIKAN AJA
    @POST("post_barang2.php")
    fun postBarang2(
        @Body data: RequestBody, // Data barang dalam bentuk JsonObject
    ): Call<UploadResponse>
}