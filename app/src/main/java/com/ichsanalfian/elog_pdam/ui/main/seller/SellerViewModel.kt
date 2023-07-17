package com.ichsanalfian.elog_pdam.ui.main.seller

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import com.ichsanalfian.elog_pdam.model.Barang
import kotlinx.coroutines.launch

class SellerViewModel (private val repository: Repository): ViewModel() {
    fun getBarang() : LiveData<List<Barang>> {
        return repository.getLiveBarang()
    }

    fun setBarang(){
        viewModelScope.launch {
            repository.setBarang()
        }
    }
}