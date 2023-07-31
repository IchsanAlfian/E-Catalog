package com.ichsanalfian.elog_pdam.model

import com.google.gson.annotations.SerializedName

data class GetBarangResponse( //TODO tambahan
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("barang")
    val listBarang: List<Barang>? = null
)