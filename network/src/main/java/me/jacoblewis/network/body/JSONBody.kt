package me.jacoblewis.network.body

import androidx.annotation.Keep
import me.jacoblewis.network.NetworkConstants.APPLICATION_JSON
import me.jacoblewis.network.NetworkConstants.CONTENT_LENGTH
import me.jacoblewis.network.NetworkConstants.CONTENT_TYPE
import java.net.URLConnection

class JSONBody
@Keep constructor(jsonStr: String) : Body {

    val bytes = jsonStr.toByteArray()

    override fun writeBodyRequiredHeaders(connection: URLConnection) {
        connection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON)
        connection.setRequestProperty(CONTENT_LENGTH, bytes.size.toString())
    }

    override fun writeBodyData(connection: URLConnection) {
        connection.outputStream.write(bytes)
    }
}