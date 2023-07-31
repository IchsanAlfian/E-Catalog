package com.ichsanalfian.elog_pdam.model

import com.google.gson.annotations.SerializedName

//TODO kelas tambahan
data class MessageResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)