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

package me.jacoblewis.network.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService


class TaskRunner(private val onError: (Exception) -> Unit) {
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var executor: ExecutorService? = java.util.concurrent.Executors.newCachedThreadPool()
    fun <R> executeAsync(callable: BaseCallable<R>?) {
        try {
            executor?.execute(RunnableTask<R>(handler, callable))
        } catch (e: Exception) {
            onError(e)
        } finally {
            executor?.shutdown()
        }
    }

    fun cancel() {
        handler = null
        executor = null
    }

    inner class RunnableTask<R>(
        private val handler: Handler?,
        private val callable: BaseCallable<R>?
    ) : Runnable {
        override fun run() {
            try {
                val loadingCallback: (Float)->Unit = { loadingValue ->
                    handler?.post {
                        callable?.setLoading(loadingValue)
                    }
                }
                if (callable?.hasLoading == true) {
                    callable.loadingCallback = loadingCallback
                }
                val result: R? = callable?.call()
                handler?.post(RunnableTaskForHandler<R>(callable, result))
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    class RunnableTaskForHandler<R>(
        private val callable: BaseCallable<R>?,
        private val result: R?
    ) : Runnable {
        override fun run() {
            callable?.setDataAfterLoading(result)
        }
    }
}