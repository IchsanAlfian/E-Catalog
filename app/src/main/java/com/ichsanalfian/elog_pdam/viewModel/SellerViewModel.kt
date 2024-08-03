package com.ichsanalfian.elog_pdam.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.model.VerifSellerData
import kotlinx.coroutines.launch
import java.io.File

class SellerViewModel (private val repository: Repository): ViewModel() {
    fun getBarang() : LiveData<List<Barang>?> {
        return repository.getLiveBarang()
    }

    fun setBarangBuyer(){
        viewModelScope.launch {
            repository.setBarangBuyer()
        }
    }

    fun setBarang(){
        viewModelScope.launch {
            repository.setBarang()
        }
    }
    fun postBarang(barang: Barang, imageFile: File) {
        viewModelScope.launch {
            repository.postBarang(barang, imageFile)
        }
    }

    fun deleteBarang(id: Int) {
        viewModelScope.launch {
            repository.deleteBarang(id)
        }
    }
    fun updateBarang(barang: Barang, imageFile: File?) {
        repository.updateBarang(barang, imageFile)
    }

    fun searchBarang(query: String): LiveData<List<Barang>> {
        return repository.searchBarang(query)
    }

    fun isLoad(): LiveData<Boolean> {
        return repository.isLoading
    }

    fun postVerif(verifSellerData: VerifSellerData, logo: File, ktp: File){
        viewModelScope.launch {
            repository.postVerif(verifSellerData, logo, ktp)
        }
    }

    fun getReasonReject(){
        viewModelScope.launch {
            repository.getReasonReject()
        }
    }

    fun getLiveReason() = repository.getLiveReason()

    //digantii besuk ja
    private val _addToCartResult = MutableLiveData<Pair<Boolean, String>>()
    val addToCartResult: LiveData<Pair<Boolean, String>> get() = _addToCartResult

    fun addToCart(idBarang: Int, jumlah: Int, id_user :String) {
        viewModelScope.launch {
            repository.addToCart(idBarang, jumlah, id_user) { success, message ->
                if (success) {
                    _addToCartResult.value = Pair(success, "Produk berhasil ditambahkan ke keranjang!")
                } else {
                    _addToCartResult.value = Pair(success, message ?: "Failed to add item to cart.")
                }
            }
        }
    }

    private val _update = MutableLiveData<Pair<Boolean, String>>()
    val update: LiveData<Pair<Boolean, String>> get() = _update

    fun update_jumlah_barang(idBarang: Int, jumlah: Int, id_user :String) {
        viewModelScope.launch {
            repository.update_jumlah_barang(idBarang, jumlah, id_user) { success, message ->
                if (success) {
                    _update.value = Pair(success, "Produk berhasil diupdate ke keranjang!")
                } else {
                    _update.value = Pair(success, message)
                }
            }
        }
    }

    fun getRequestSeller() {
        viewModelScope.launch {
            repository.setBarangRequestSeller()
        }
    }

    fun getRiwayat() {
        repository.setBarangRiwayat()
    }

    fun sending(idSeller : String?, id: String?){
        viewModelScope.launch {
            repository.sending(idSeller, id)
        }
    }

    fun diterima(idSeller : String?, id: String?){
        viewModelScope.launch {
            repository.diterima(idSeller, id)
        }
    }
}