package com.ichsanalfian.elog_pdam.di

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.ichsanalfian.elog_pdam.api.ApiConfig
import com.ichsanalfian.elog_pdam.api.ApiService
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.model.GetBarangResponse
import com.ichsanalfian.elog_pdam.model.LoginResponse
import com.ichsanalfian.elog_pdam.model.MessageResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Repository(private val api : ApiService) {

    fun register(username: String, password: String, perusahaan: String){ //TODO Tambahan
        val data = JsonObject().apply {
            addProperty("username", username)
            addProperty("pass", password)
            addProperty("perusahaan", perusahaan)
        }
        val client = ApiConfig.getApiService().register(data)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo register merespon", "Msg: ${response.body().toString()}")
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.toString(), MessageResponse::class.java)
                    response.errorBody()?.close()

                    Log.e("Repo register gagal", "Error: $error")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }
        })
    }

    fun login(username: String, password: String){ //TODO Tambahan
        val data = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }

        val client = ApiConfig.getApiService().login(data)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo login merespon", "Msg: ${response.body().toString()}")
                    if (response.body() != null){
                        loginResponse.postValue(response.body())
                    }
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.toString(), LoginResponse::class.java)
                    response.errorBody()?.close()

                    Log.e("Repo login gagal", "Error: $error")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }
        })
    }

    val loginResponse = MutableLiveData<LoginResponse>() //TODO Tambahan
    fun getLiveLoginRepsonse(): LiveData<LoginResponse> { //TODO Tambahan
        return loginResponse
    }

    val barang = MutableLiveData<List<Barang>?>()

    fun getLiveBarang(): LiveData<List<Barang>?> {
        return barang
    }

    fun setBarangBuyer() {
        val client = ApiConfig.getApiService().getBarangAuth()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                if (response.isSuccessful) {
                    val getBarangResponse = response.body()
                    if (getBarangResponse != null && !getBarangResponse.error) {
                        val barangList = getBarangResponse.listBarang
                        if (barangList != null) {
                            barang.value = barangList
                            println(barang.value.toString())
                        } else{
                            println("NULL NGAB")
                        }
                    } else {
                        Log.e("Repository setBarang", "Error: ${response.message()}")
                    }
                } else {
                    Log.e("Repository setBarang", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun setBarang() {
        val client = ApiConfig.getApiService().getBarangbySellerID()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                if (response.isSuccessful) {
                    val getBarangResponse = response.body()
                    if (getBarangResponse != null && !getBarangResponse.error) {
                        val barangList = getBarangResponse.listBarang
                        if (barangList != null) {
                            barang.value = barangList
                            println(barang.value.toString())
                        } else{
                            println("NULL NGAB")
                        }
                    } else {
                        Log.e("Repository setBarang", "Error: ${response.message()}")
                    }
                } else {
                    Log.e("Repository setBarang", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }

        })
    }

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
            addProperty("id_seller", barang.idSeller)
        }

        val req = Gson().toJson(data).toRequestBody("application/json".toMediaType())
        // Convert the image file to a RequestBody
        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

        // Send the data and image in the multipart request
        val client = ApiConfig.getApiService().postBarang(data = req, image = imagePart)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo postBarang sukses", "Msg: ${response.body().toString()}")
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.toString(), MessageResponse::class.java)
                    response.errorBody()?.close()

                    Log.e("Repo postBarang gagal", "Error: $error")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
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
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo deleteBarang", "Msg: ${response.body().toString()}")
                    // Handle successful deletion, for example, show a success dialog or perform other actions
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.string(), MessageResponse::class.java)
                    response.errorBody()?.close()
                    Log.e("Repo deleteBarang", "Error: $error")
                    // Handle error in deletion, for example, show an error dialog or perform other actions
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
                // Handle failure in deletion, for example, show a failure dialog or perform other actions
            }
        })
    }

    fun updateBarang(id: Int, barang: Barang, imageFile: File?) {
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
            addProperty("id_seller", barang.idSeller)
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
        val client = ApiConfig.getApiService().updateBarang(id, req, imagePart)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Log.e("Repo updateBarang", "Msg: ${response.body().toString()}")
                    // Handle successful update, for example, show a success dialog or perform other actions
                } else {
                    val gson = GsonBuilder().setLenient().create()
                    val error = gson.fromJson(response.errorBody()?.string(), MessageResponse::class.java)
                    response.errorBody()?.close()
                    Log.e("Repo updateBarang", "Error: $error")
                    // Handle error in update, for example, show an error dialog or perform other actions
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
                // Handle failure in update, for example, show a failure dialog or perform other actions
            }
        })
    }

    fun addToCart(idBarang: Int, jumlah: Int, id_user: String, onResponse: (Boolean, String?) -> Unit) {
        val client = ApiConfig.getApiService().addToCart(idBarang, jumlah,
            id_user
        )
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    val messageResponse = response.body()
                    if (messageResponse != null && !messageResponse.error) {
                        onResponse(true, "Item added to cart successfully.")
                    } else {
                        onResponse(false, messageResponse?.message)
                    }
                } else {
                    onResponse(false, "Failed to add item to cart.")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                onResponse(false, "Failed to add item to cart.")
            }
        })
    }

    fun update_jumlah_barang (idBarang: Int, jumlah: Int, id_user: String, onResponse: (Boolean, String) -> Unit) {
        val client = ApiConfig.getApiService().update_jumlah_keranjang(idBarang, jumlah, id_user)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    val messageResponse = response.body()
                    if (messageResponse != null && !messageResponse.error) {
                        onResponse(true, "Item added to cart successfully.")
                    } else {
                        onResponse(false, messageResponse?.message.toString())
                    }
                } else {
                    onResponse(false, "Failed to add item to cart.")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                onResponse(false, "Failed to add item to cart.")
            }
        })
    }

    private val _liveKeranjang = MutableLiveData<List<Barang>?>()
    val liveKeranjang: LiveData<List<Barang>?> = _liveKeranjang


    fun setBarangKeranjang() {
        val client = ApiConfig.getApiService().getKeranjang()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                if (response.isSuccessful) {
                    val getBarangResponse = response.body()
                    if (getBarangResponse != null && !getBarangResponse.error) {
                        val barangList = getBarangResponse.listBarang
                        if (barangList != null) {
                            barang.value = barangList
                            println(barang.value.toString())
                        } else{
                            println("NULL NGAB")
                        }
                    } else {
                        Log.e("Repository setBarang", "Error: ${response.message()}")
                    }
                } else {
                    Log.e("Repository setBarang", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }

        })
    }
    private val _moveToHistoryResult = MutableLiveData<Pair<Boolean, String>>()
    val moveToHistoryResult: LiveData<Pair<Boolean, String>> = _moveToHistoryResult
    fun moveToHistory(id_user: String, onResponse: (Boolean, String) -> Unit) {
        val client = ApiConfig.getApiService().riwayat(id_user)
        client.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    val messageResponse = response.body()
                    if (messageResponse != null && !messageResponse.error) {
                        onResponse(true, "Items from cart successfully moved to history.")
                    } else {
                        onResponse(false, "Failed to move items to history.")
                    }
                } else {
                    onResponse(false, "Failed to move items to history.")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                onResponse(false, "Failed to move items to history.")
            }
        })
    }

    fun setBarangRiwayat() {
        val client = ApiConfig.getApiService().getRiwayat()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                if (response.isSuccessful) {
                    val getBarangResponse = response.body()
                    if (getBarangResponse != null && !getBarangResponse.error) {
                        val barangList = getBarangResponse.listBarang
                        if (barangList != null) {
                            barang.value = barangList
                            println(barang.value.toString())
                        } else{
                            println("NULL NGAB")
                        }
                    } else {
                        Log.e("Repository setBarang", "Error: ${response.message()}")
                    }
                } else {
                    Log.e("Repository setBarang", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun setBarangRequestSeller() {
        val client = ApiConfig.getApiService().getSellerRequestByIdSeller()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                if (response.isSuccessful) {
                    val getBarangResponse = response.body()
                    if (getBarangResponse != null && !getBarangResponse.error) {
                        val barangList = getBarangResponse.listBarang
                        if (barangList != null) {
                            barang.value = barangList
                            println(barang.value.toString())
                        } else{
                            println("NULL NGAB")
                        }
                    } else {
                        Log.e("Repository setBarang", "Error: ${response.message()}")
                    }
                } else {
                    Log.e("Repository setBarang", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.d("Fail", t.message.toString())
                t.printStackTrace()
            }

        })
    }
}



