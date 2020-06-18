package me.jacoblewis.network.body

import androidx.annotation.Keep
import me.jacoblewis.network.NetworkConstants.CONTENT_LENGTH
import me.jacoblewis.network.NetworkConstants.CONTENT_TYPE
import me.jacoblewis.network.NetworkConstants.TEXT_PLAIN
import java.net.URLConnection

class PlainTextBody
@Keep constructor(text: String) : Body {

    val bytes = text.toByteArray()

    override fun writeBodyRequiredHeaders(connection: URLConnection) {
        connection.setRequestProperty(CONTENT_TYPE, TEXT_PLAIN)
        connection.setRequestProperty(CONTENT_LENGTH, bytes.size.toString())
    }

    override fun writeBodyData(connection: URLConnection) {
        connection.outputStream.write(bytes)
    }
}