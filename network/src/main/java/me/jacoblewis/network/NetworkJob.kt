package me.jacoblewis.network

import me.jacoblewis.network.models.Request
import me.jacoblewis.network.models.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.math.max

object NetworkJob {

    internal fun run(request: Request, onProgress: ((Float) -> Unit)?): Response {
        var response: Response?
        try {
            val url = URL(request.url)
            val connection = url.openConnection() as HttpsURLConnection
            // Set Method
            connection.requestMethod = request.method.value
            // Set Timeouts
            connection.readTimeout = request.readTimeout
            connection.connectTimeout = request.connectTimeout
            // Set Headers
            for ((headerKey, headerValue) in request.headers) {
                connection.setRequestProperty(headerKey, headerValue)
            }
            connection.doInput = true
            // Set Body
            val body = request.body
            if (body != null) {
                connection.doOutput = true
                body.writeBodyRequiredHeaders(connection)
                body.writeBodyData(connection)
            }

            connection.connect()

            // Read Body
            // Read Response Code
            val responseCode = connection.responseCode
            val responseHeaders =
                connection.headerFields.mapValues { it.value.joinToString(", ") }

            var responseBody: ByteArray? = null
            var exception: Exception? = null
            try {
                val contentLength = connection.contentLength.toFloat()
                val buffer = ByteArray(1024)
                val os = ByteArrayOutputStream()
                var len = connection.inputStream.read(buffer)
                var readBytes = len
                var percent: Float
                while (len != -1) {
                    os.write(buffer, 0, len)
                    // Publish progress
                    if (onProgress != null && contentLength != -1f) {
                        readBytes += len
                        percent = max((readBytes / contentLength), 1f)
                        onProgress(percent)
                    }
                    len = connection.inputStream.read(buffer)
                }
                responseBody = os.toByteArray()
            } catch (e: Exception) {
                exception = e
            }



            response =
                Response(
                    responseCode,
                    request.url,
                    request.method,
                    responseHeaders,
                    responseBody,
                    exception
                )
        } catch (e: IOException) {
            response = Response(-1, request.url, request.method, mapOf(), null, e)
        }

        return response ?: throw Exception("Response null")
    }
}