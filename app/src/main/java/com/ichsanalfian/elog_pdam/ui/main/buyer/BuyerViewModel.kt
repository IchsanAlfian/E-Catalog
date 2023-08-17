package com.ichsanalfian.elog_pdam.ui.main.buyer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import com.ichsanalfian.elog_pdam.model.Barang
import kotlinx.coroutines.launch

class BuyerViewModel (private val repository: Repository): ViewModel() { //TODO Tambahan

    private val _addToCartResult = MutableLiveData<Pair<Boolean, String>>()
    val addToCartResult: LiveData<Pair<Boolean, String>> get() = _addToCartResult



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

    fun searchBarang(query: String): LiveData<List<Barang>> {
        return repository.searchBarang(query)
    }


    private val _keranjang = MutableLiveData<List<Barang>?>()
    val keranjang: LiveData<List<Barang>?> get() = _keranjang

    fun getKeranjang() {
        repository.setBarangKeranjang()
    }

    fun getRiwayat() {
        repository.setBarangRiwayat()
    }
    private val _moveToHistoryResult = MutableLiveData<Pair<Boolean, String>>()
    val moveToHistoryResult: LiveData<Pair<Boolean, String>> = _moveToHistoryResult

    fun moveToHistory(id_user: String) {

        viewModelScope.launch {
            repository.moveToHistory(id_user) { success, message ->
                if (success) {
                _moveToHistoryResult.value = Pair(success, "Produk telah berhasil Checkout")
                } else {
                    _addToCartResult.value = Pair(success, message ?: "Failed to add item to cart.")
                }
            }
        }
    }
}