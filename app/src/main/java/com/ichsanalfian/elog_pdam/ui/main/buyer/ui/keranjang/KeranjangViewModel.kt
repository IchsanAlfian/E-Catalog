package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.keranjang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeranjangViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is keranjang Fragment"
    }
    val text: LiveData<String> = _text
}