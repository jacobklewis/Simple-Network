package me.jacoblewis.network.body

import androidx.annotation.Keep
import me.jacoblewis.network.NetworkConstants.CONTENT_LENGTH
import me.jacoblewis.network.NetworkConstants.CONTENT_TYPE
import me.jacoblewis.network.NetworkConstants.UTF_8
import me.jacoblewis.network.NetworkConstants.X_WWW_FORM_URLENCODED
import java.net.URLConnection
import java.net.URLEncoder

class FormURLEncodedBody
@Keep constructor(parameters: Map<String, String>) : Body {

    val bytes by lazy {
        fun urlEncoded(plain: String): String {
            return URLEncoder.encode(plain, UTF_8)
        }

        val queryString = StringBuilder()
        for ((k, v) in parameters) {
            if (queryString.isNotBlank()) {
                queryString.append("&")
            }
            queryString.append("${urlEncoded(k)}=${urlEncoded(v)}")
        }
        queryString.toString().toByteArray()
    }

    override fun writeBodyRequiredHeaders(connection: URLConnection) {
        connection.setRequestProperty(CONTENT_TYPE, X_WWW_FORM_URLENCODED)
        connection.setRequestProperty(CONTENT_LENGTH, bytes.size.toString())
    }

    override fun writeBodyData(connection: URLConnection) {
        connection.outputStream.write(bytes)
    }
}