package com.imploded.trippinout.model

import com.google.gson.annotations.SerializedName

data class Token(
        val scope: String = "",
        @SerializedName("token_type") val tokenType: String = "",
        @SerializedName("expires_in") val expiresIn: String = "",
        @SerializedName("access_token") val accessToken: String = ""
)