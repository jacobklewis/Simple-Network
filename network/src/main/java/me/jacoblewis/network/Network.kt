package me.jacoblewis.network

import androidx.annotation.Keep
import me.jacoblewis.network.body.Body
import me.jacoblewis.network.models.HttpMethod
import me.jacoblewis.network.models.Request
import me.jacoblewis.network.models.Response
import me.jacoblewis.network.utils.TaskRunner


@Keep
class Network
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
        ): Network {
            val request = Request(url, HttpMethod.GET, headers, null, readTimeout = timeout)
            return Network(request)
        }

        @JvmStatic
        @JvmOverloads
        fun post(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Network {
            val request = Request(url, HttpMethod.POST, headers, body)
            return Network(request)
        }

        @JvmStatic
        @JvmOverloads
        fun put(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Network {
            val request = Request(url, HttpMethod.PUT, headers, body)
            return Network(request)
        }

        @JvmStatic
        @JvmOverloads
        fun patch(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Network {
            val request = Request(url, HttpMethod.PATCH, headers, body)
            return Network(request)
        }

        @JvmStatic
        @JvmOverloads
        fun delete(
            url: String,
            body: Body? = null,
            headers: Map<String, String> = mapOf(),
            timeout: Int = 30_000
        ): Network {
            val request = Request(url, HttpMethod.DELETE, headers, body)
            return Network(request)
        }

        @JvmStatic
        fun request(
            url: String,
            method: HttpMethod,
            headers: Map<String, String>,
            body: Body?,
            readTimeout: Int = 30_000,
            connectTimeout: Int = 15_000
        ): Network {
            return Network(Request(url, method, headers, body, readTimeout, connectTimeout))
        }
    }

}