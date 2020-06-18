package me.jacoblewis.network.body

import androidx.annotation.Keep
import me.jacoblewis.network.NetworkConstants.CONTENT_LENGTH
import me.jacoblewis.network.NetworkConstants.CONTENT_TYPE
import java.io.File
import java.net.URLConnection


class FileBody
@Keep constructor(val file: File, val type: String) : Body {

    val bytes by lazy {
        file.readBytes()
    }

    override fun writeBodyRequiredHeaders(connection: URLConnection) {
        connection.setRequestProperty(CONTENT_TYPE, type)
        connection.setRequestProperty(CONTENT_LENGTH, bytes.size.toString())
    }

    override fun writeBodyData(connection: URLConnection) {
        connection.outputStream.write(bytes)
    }
}