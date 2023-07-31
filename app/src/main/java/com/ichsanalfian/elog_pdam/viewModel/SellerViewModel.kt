package com.ichsanalfian.elog_pdam.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import com.ichsanalfian.elog_pdam.model.Barang
import kotlinx.coroutines.launch
import java.io.File

class SellerViewModel (private val repository: Repository): ViewModel() {
    fun getBarang() : LiveData<List<Barang>?> {
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

    fun deleteBarang(id: Int) {
        viewModelScope.launch {
            repository.deleteBarang(id)
        }
    }
    fun updateBarang(id: Int, barang: Barang, imageFile: File?) {
        repository.updateBarang(id ,barang, imageFile)
    }

    fun searchBarang(query: String): LiveData<List<Barang>> {
        return repository.searchBarang(query)
    }
}