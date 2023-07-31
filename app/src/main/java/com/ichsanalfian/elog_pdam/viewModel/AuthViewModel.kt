package com.ichsanalfian.elog_pdam.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichsanalfian.elog_pdam.di.Repository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository): ViewModel() { //TODO Tambahan

    fun register(username: String, password: String, perusahaan: String){
        viewModelScope.launch {
            repository.register(username, password, perusahaan)
        }
    }

    fun login(username: String, password: String){
        viewModelScope.launch {
            repository.login(username, password)
        }
    }

    fun getDatabyLogin() = repository.getLiveLoginRepsonse()
}