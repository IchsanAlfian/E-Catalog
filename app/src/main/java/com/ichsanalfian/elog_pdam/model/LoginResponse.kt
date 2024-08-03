package com.ichsanalfian.elog_pdam.model

import com.google.gson.annotations.SerializedName

data class LoginResponse( //TODO tambahan
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val userData: UserData? = null
)

data class UserData( //TODO tambahan

    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("username")
    var username: String? = null,

    @field:SerializedName("role")
    var role: String? = null,

    @field:SerializedName("isVerified")
    var isVerified: Int? = null
)