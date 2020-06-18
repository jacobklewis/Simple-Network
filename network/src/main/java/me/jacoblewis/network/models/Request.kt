package me.jacoblewis.network.models

import me.jacoblewis.network.body.Body

class Request(
    val url: String,
    val method: HttpMethod,
    val headers: Map<String, String>,
    val body: Body?,
    val readTimeout: Int = 30_000,
    val connectTimeout: Int = 15_000
)