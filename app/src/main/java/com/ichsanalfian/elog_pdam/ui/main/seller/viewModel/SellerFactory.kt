package com.ichsanalfian.elog_pdam.ui.main.seller.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ichsanalfian.elog_pdam.di.Injection

class SellerFactory(): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerViewModel::class.java)) {
            return SellerViewModel(Injection.provideRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SellerFactory? = null
        fun getInstance(): SellerFactory = instance ?: synchronized(this) {
            instance ?: SellerFactory()
        }.also {
            instance = it
        }
    }
}