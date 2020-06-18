package me.jacoblewis.network.models

import androidx.annotation.Keep

@Keep
class Response(
    val code: Int,
    val url: String,
    val method: HttpMethod,
    val headers: Map<String, String>,
    val body: ByteArray?,
    val error: Exception? = null
) {

    val bodyAsString: String?
        get() = body?.toString(Charsets.UTF_8)
}