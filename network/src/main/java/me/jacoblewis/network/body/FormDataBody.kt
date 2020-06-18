package me.jacoblewis.network.body

import androidx.annotation.Keep
import me.jacoblewis.network.NetworkConstants.BOUNDARY
import me.jacoblewis.network.NetworkConstants.CONTENT_DISPOSITION
import me.jacoblewis.network.NetworkConstants.CONTENT_LENGTH
import me.jacoblewis.network.NetworkConstants.CONTENT_TYPE
import me.jacoblewis.network.NetworkConstants.FORM_DATA
import me.jacoblewis.network.NetworkConstants.MULTIPART_FORM_DATA
import me.jacoblewis.network.models.FormItem
import me.jacoblewis.network.utils.ByteArrayBuilder
import java.net.URLConnection
import java.util.*


class FormDataBody
@Keep constructor(parameters: Map<String, FormItem>) : Body {

    private val boundaryId by lazy {
        "Boundary${UUID.randomUUID().toString().takeLast(9)}"
    }

    val bytes by lazy {
        val builder = ByteArrayBuilder()
        for ((k, v) in parameters) {
            builder.append("--${boundaryId}\r\n")
            builder.append("$CONTENT_DISPOSITION: $FORM_DATA; name=\"$k\"")
            when (v) {
                is FormItem.Text -> {
                    builder.append("\r\n\r\n${v.text}\r\n")
                }
                is FormItem.File -> {
                    builder.append("; filename=\"${v.file.name}\"\r\n")
                    builder.append("$CONTENT_TYPE: ${v.type}\r\n\r\n")
                    builder.append(v.file.readBytes())
                    builder.append("\r\n")
                }
            }
        }
        builder.append("--${boundaryId}--\r\n")
        builder.build()
    }

    override fun writeBodyRequiredHeaders(connection: URLConnection) {
        connection.setRequestProperty(
            CONTENT_TYPE,
            "$MULTIPART_FORM_DATA; $BOUNDARY=\"$boundaryId\""
        )
        connection.setRequestProperty(CONTENT_LENGTH, bytes.size.toString())
    }

    override fun writeBodyData(connection: URLConnection) {
        connection.outputStream.write(bytes)
    }

}