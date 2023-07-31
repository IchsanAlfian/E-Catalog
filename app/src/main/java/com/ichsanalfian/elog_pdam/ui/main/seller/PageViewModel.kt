package com.ichsanalfian.elog_pdam.ui.main.seller

import androidx.lifecycle.*

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
//    val text: LiveData<String> = Transformations.map(_index) {
//        "Hello world from section: $it"
//    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}
