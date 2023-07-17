package com.ichsanalfian.elog_pdam.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.ichsanalfian.elog_pdam.api.ApiConfig
import com.ichsanalfian.elog_pdam.api.ApiService
import com.ichsanalfian.elog_pdam.model.Barang
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val api : ApiService) {
    val barang = MutableLiveData<List<Barang>>()

    fun setBarang() {
        val client = ApiConfig.getApiService().getBarang()
        client.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()?.getAsJsonArray("Barang")
                    if (jsonArray != null) {
                        val gson = Gson()
                        val type = object : TypeToken<List<Barang>>() {}.type
                        val barangList = gson.fromJson<List<Barang>>(jsonArray, type)
                        barang.postValue(barangList)
                    }
                } else {
                    Log.e("UserViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }

        })

    }

    fun getLiveBarang(): LiveData<List<Barang>> {
        return barang
    }
}