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
    ) :
        Runnable {
        override fun run() {
            try {
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
    ) :
        Runnable {
        override fun run() {
            callable?.setDataAfterLoading(result)
        }
    }
}