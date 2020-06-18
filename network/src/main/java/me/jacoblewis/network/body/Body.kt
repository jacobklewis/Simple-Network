package me.jacoblewis.network.body

import androidx.annotation.Keep
import java.net.URLConnection


@Keep
interface Body {
    fun writeBodyRequiredHeaders(connection: URLConnection)
    fun writeBodyData(connection: URLConnection)
}