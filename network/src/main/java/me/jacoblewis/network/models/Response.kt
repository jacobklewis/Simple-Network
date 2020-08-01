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