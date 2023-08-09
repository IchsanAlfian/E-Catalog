package com.ichsanalfian.elog_pdam.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ichsanalfian.elog_pdam.di.Injection
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerViewModel

class ViewModelFactory(): ViewModelProvider.Factory { //TODO Ganti nama jadi viewModelFactory

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerViewModel::class.java)) {
            return SellerViewModel(Injection.provideRepository()) as T
        } else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) { //TODO Tambahan
            return AuthViewModel(Injection.provideRepository()) as T
        }
        else if (modelClass.isAssignableFrom(BuyerViewModel::class.java)) { //TODO Tambahan
            return BuyerViewModel(Injection.provideRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory()
        }.also {
            instance = it
        }
    }
}