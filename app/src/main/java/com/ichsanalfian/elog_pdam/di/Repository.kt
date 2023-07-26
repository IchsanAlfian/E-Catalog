package com.ichsanalfian.elog_pdam.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.ichsanalfian.elog_pdam.api.ApiConfig
import com.ichsanalfian.elog_pdam.api.ApiService
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.model.UploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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
                    Log.e("Repository setBarang", "Error: ${response.message()}")
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

    //TODO Diganti
    fun postBarang(barang: Barang, imageFile: File) {
        val data = JsonObject().apply {
            addProperty("nama", barang.nama)
            addProperty("merk", barang.merk)
            addProperty("harga", barang.harga)
            addProperty("satuan", barang.satuan)
            addProperty("kode", barang.kode)
            addProperty("stok", barang.stok)
            addProperty("kategori", barang.kategori)
            addProperty("ukuran", barang.ukuran)
            addProperty("deskripsi", barang.deskripsi)
            addProperty("gambar", barang.gambar)
        }

        println("Ini nama yang diparsing ${barang.gambar}")
        val req = Gson().toJson(data).toRequestBody("application/json".toMediaType())
        // Convert the image file to a RequestBody
        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

        // Send the data and image in the multipart request
        val client = ApiConfig.getApiService().postBarang(req, imagePart)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo postBarang sukses", "Msg: ${response.body().toString()}")
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.toString(), UploadResponse::class.java)
                    response.errorBody()?.close()

                    Log.e("Repo postBarang gagal", "Error: $error")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }
        })
    }
    fun searchBarang(query: String): LiveData<List<Barang>> {
        val filteredList = MutableLiveData<List<Barang>>()
        barang.value?.let { list ->
            val filtered = list.filter { barang ->
                barang.nama.contains(query, ignoreCase = true)
            }
            filteredList.postValue(filtered)
        }
        return filteredList
    }
    fun deleteBarang(id: Int) {
        val client = ApiConfig.getApiService().deleteBarang(id)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo deleteBarang", "Msg: ${response.body().toString()}")
                    // Handle successful deletion, for example, show a success dialog or perform other actions
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.string(), UploadResponse::class.java)
                    response.errorBody()?.close()
                    Log.e("Repo deleteBarang", "Error: $error")
                    // Handle error in deletion, for example, show an error dialog or perform other actions
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
                // Handle failure in deletion, for example, show a failure dialog or perform other actions
            }
        })
    }

    fun updateBarang(barang: Barang, imageFile: File?) {
        val data = JsonObject().apply {
            addProperty("id", barang.id) // ID produk yang akan diupdate
            addProperty("nama", barang.nama)
            addProperty("merk", barang.merk)
            addProperty("harga", barang.harga)
            addProperty("satuan", barang.satuan)
            addProperty("kode", barang.kode)
            addProperty("stok", barang.stok)
            addProperty("kategori", barang.kategori)
            addProperty("ukuran", barang.ukuran)
            addProperty("deskripsi", barang.deskripsi)
            addProperty("gambar", barang.gambar)
        }

        // Konversi data menjadi RequestBody
        val req = Gson().toJson(data).toRequestBody("application/json".toMediaType())

        // Jika ada perubahan gambar, tambahkan gambar dalam bentuk MultipartBody.Part
        val imagePart = if (imageFile != null) {
            val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
        } else {
            null
        }

        // Panggil endpoint updateBarang di ApiService
        val client = ApiConfig.getApiService().updateBarang(barang.id!!, req, imagePart)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo updateBarang", "Msg: ${response.body().toString()}")
                    // Handle successful update, for example, show a success dialog or perform other actions
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.string(), UploadResponse::class.java)
                    response.errorBody()?.close()
                    Log.e("Repo updateBarang", "Error: $error")
                    // Handle error in update, for example, show an error dialog or perform other actions
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
                // Handle failure in update, for example, show a failure dialog or perform other actions
            }
        })
    }

}