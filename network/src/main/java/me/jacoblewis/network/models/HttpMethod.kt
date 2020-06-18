package me.jacoblewis.network.models

import androidx.annotation.Keep

@Keep
enum class HttpMethod(val value: String) {
    GET("GET"),
    POST("POST"),
    PATCH("PATCH"),
    PUT("PUT"),
    DELETE("DELETE")
}