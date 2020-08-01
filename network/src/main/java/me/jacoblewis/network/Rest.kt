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

package me.jacoblewis.network

import androidx.annotation.Keep
import me.jacoblewis.network.body.Body
import me.jacoblewis.network.models.HttpMethod
import me.jacoblewis.network.models.Request
import me.jacoblewis.network.models.Response
import me.jacoblewis.network.utils.TaskRunner


@Keep
class Rest
private constructor(private val request: Request) {
    var taskRunner: TaskRunner? = null

    /**
     * Execute immediately on current thread. This is a blocking function
     */
    fun execute(): Response {
        return NetworkJob.run(request, null)
    }

    /**
     * Execute asynchronously on IO thread.
     *
     * @param onComplete
     * @param onProgress
     */
    @JvmOverloads
    fun enqueue(onComplete: (Response) -> Unit, onProgress: ((Float) -> Unit)? = null) {
        taskRunner?.cancel()
        taskRunner = TaskRunner { ex ->
            onComplete(Response(-1, request.url, request.method, mapOf(), null, ex))
        }
        taskRunner?.executeAsync(NetworkTask(request, onComplete = {
            taskRunner?.cancel()
            taskRunner = null
            onComplete(it)
        }, onProgress = onProgress))
    }


    @Keep
    companion object {
        @JvmStatic
        @JvmOverloads
        fun get(
            url: String,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Rest {
            val request = Request(url, HttpMethod.GET, headers, null, readTimeout = timeout)
            return Rest(request)
        }

        @JvmStatic
        @JvmOverloads
        fun post(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Rest {
            val request = Request(url, HttpMethod.POST, headers, body)
            return Rest(request)
        }

        @JvmStatic
        @JvmOverloads
        fun put(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Rest {
            val request = Request(url, HttpMethod.PUT, headers, body)
            return Rest(request)
        }

        @JvmStatic
        @JvmOverloads
        fun patch(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Rest {
            val request = Request(url, HttpMethod.PATCH, headers, body)
            return Rest(request)
        }

        @JvmStatic
        @JvmOverloads
        fun delete(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Rest {
            val request = Request(url, HttpMethod.DELETE, headers, body)
            return Rest(request)
        }

        @JvmStatic
        fun request(
            url: String,
            method: HttpMethod,
            headers: Map<String, String>,
            body: Body?,
            readTimeout: Int = 30_000,
            connectTimeout: Int = 15_000
        ): Rest {
            return Rest(Request(url, method, headers, body, readTimeout, connectTimeout))
        }
    }

}