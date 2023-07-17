package com.ichsanalfian.elog_pdam.model

import com.google.gson.annotations.SerializedName

data class Barang(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("merk")
    val merk: String,

    @SerializedName("harga")
    val harga: Int,

    @SerializedName("satuan")
    val satuan: String,

    @SerializedName("kode")
    val kode: String,

    @SerializedName("stok")
    val stok: Int,

    @SerializedName("kategori")
    val kategori: String,

    @SerializedName("ukuran")
    val ukuran: String,

    @SerializedName("deskripsi")
    val deskripsi: String
)