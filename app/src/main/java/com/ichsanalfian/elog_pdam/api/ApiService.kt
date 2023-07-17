package com.ichsanalfian.elog_pdam.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("API.php")
    fun getBarang(): Call<JsonObject>
}