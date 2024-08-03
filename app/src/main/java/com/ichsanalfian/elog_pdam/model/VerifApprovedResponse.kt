package com.ichsanalfian.elog_pdam.model

import com.google.gson.annotations.SerializedName

data class VerifApprovedResponse( //TODO tambahan
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("seller")
    val listVerifSellerData: List<VerifSellerData>? =  null
)

data class VerifSellerData (
    @SerializedName("idSeller")
    val idSeller: String? = null,

    @SerializedName("namaPerusahaan")
    val namaPerusahaan: String,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("logo")
    val logo: String,

    @SerializedName("namaDirektur")
    val namaDirektur: String,

    @SerializedName("ktpDirektur")
    val ktpDirektur: String,

    @SerializedName("noTelp")
    val noTelp: String,

    @SerializedName("noWA")
    val noWA: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("npwp")
    val npwp: String,

    @SerializedName("bidangUsaha")
    val bidangUsaha: String,

    @SerializedName("nib")
    val nib: String,

    @SerializedName("kbli")
    val kbli: String,

    @SerializedName("tglSubmit")
    val tglSubmit: String? = null,

    @SerializedName("tglApproved")
    val tglApproved: String? = null
)