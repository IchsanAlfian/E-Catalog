package com.ichsanalfian.elog_pdam.ui.main.seller.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import com.ichsanalfian.elog_pdam.model.Barang
import kotlinx.coroutines.launch
import java.io.File

class SellerViewModel (private val repository: Repository): ViewModel() {

    fun searchBarang(query: String): LiveData<List<Barang>> {
        return repository.searchBarang(query)
    }
    fun getBarang() : LiveData<List<Barang>> {
        return repository.getLiveBarang()
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
}