/*
 * Copyright (C) 2020 Jacob Lewis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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