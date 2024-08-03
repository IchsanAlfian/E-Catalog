package com.ichsanalfian.elog_pdam.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.model.VerifSellerData
import kotlinx.coroutines.launch
import java.io.File

class AdminViewModel(private val repository: Repository): ViewModel() {
    fun isLoad(): LiveData<Boolean> {
        return repository.isLoading
    }

    fun getVerifList() = repository.getLiveVerifList()

    fun setVerifList(){
        viewModelScope.launch {
            repository.setVerifList()
        }
    }

    fun getApprovedList() = repository.getLiveApprovedList()

    fun setApprovedList(){
        viewModelScope.launch {
            repository.setApprovedList()
        }
    }

    fun approving(id : String?) = repository.approving(id)

    fun rejecting(id: String?, alasan: String?) = repository.rejecting(id, alasan)
}