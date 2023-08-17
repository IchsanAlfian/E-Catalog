package com.ichsanalfian.elog_pdam.model

import com.google.gson.annotations.SerializedName
import com.ichsanalfian.elog_pdam.local.UserPreferences

data class Barang(
    @SerializedName("id")
    val id: Int? = null,

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
    val deskripsi: String,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("idSeller")
    val idSeller: String? = null,

    @SerializedName("jumlah")
    val jumlah: Int? = null,

    //riwayat
    @SerializedName("tanggal_checkout")
    val tanggal_checkout: String? = null,

    @SerializedName("idUser")
    val idUser: String? = null,

)